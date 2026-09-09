package ru.itmo.ode;

import java.util.*;
import org.junit.jupiter.api.Test;

/** Formula, convergence and input checks, executed by Gradle test/check. */
public final class SolverTest {
    private static int checks;
    @Test
    void numericalMethodsAndValidation() { main(new String[0]); }

    @Test
    void threeExamplesForVariant15() {
        for (Input p : List.of(
                new Input(Equation.LINEAR, 0, 2, 0.5, 0.2, 0.001, List.of(Method.values())),
                new Input(Equation.SINE, 0, 6, 0, 0.3, 0.001, List.of(Method.values())),
                new Input(Equation.SQUARE, 0, 1, -1, 0.1, 0.001, List.of(Method.values())))) {
            for (Method m : Method.values()) {
                Solver.Result r = Solver.solve(p, m, Solver.baseSteps(p));
                check(r.estimate() <= p.epsilon(), "preset error " + p.equation() + m);
                check(r.values().length == r.steps() + 1, "complete table " + m);
                check(r.values()[0] == p.y0(), "preset initial value " + m);
                System.out.printf(Locale.ROOT, "%s | %s | %d | %.10g | %.10g%n",
                        p.equation(), m, r.steps(), r.estimate(), r.exactError());
            }
        }
    }
    public static void main(String[] args) {
        near(Input.number(" 0,12345678901234567 ", "test"), Double.parseDouble("0.12345678901234567"), 0, "comma precision");
        near(Input.number("1e-12", "test"), 1e-12, 0, "scientific");
        for (String s : new String[]{"", " ", "NaN", "Infinity", "1e999", "1e-999", "1,2.3", "0x10", "12abc", "--1"})
            rejects(() -> Input.number(s,"test"), "bad number " + s);
        for (Equation e : Equation.values()) {
            double y0 = e == Equation.SQUARE ? -1 : 0.5;
            double x0 = 0.3;
            near(e.exact(x0,x0,y0), y0, 1e-15, "initial condition " + e);
            double x = 0.6, d = 1e-5;
            double derivative = (e.exact(x+d,x0,y0)-e.exact(x-d,x0,y0))/(2*d);
            near(derivative,e.f(x,e.exact(x,x0,y0)),1e-8,"analytic derivative " + e);
            Input p = new Input(e,x0,1.3,y0,0.2,1e-4,List.of(Method.values()));
            int base = Solver.baseSteps(p);
            for (Method m : Method.values()) {
                Solver.Result r = Solver.solve(p,m,base);
                check(r.estimate() <= p.epsilon(), "accuracy criterion " + e + m);
                check(r.exactError() < 2*p.epsilon(), "independent actual error " + e + m);
                near(r.values()[0],y0,0,"initial numerical");
                near(NumericalMethods.x(p,r.steps(),r.steps()),p.xn(),0,"right endpoint");
                if (!m.multistep) {
                    double[] coarse = NumericalMethods.integrate(p,m,r.steps()/2);
                    double independent = 0;
                    for (int i=0;i<coarse.length;i++) independent = Math.max(independent,Math.abs(coarse[i]-r.values()[2*i])/(Math.pow(2,m.order)-1));
                    near(r.estimate(),independent,0,"Runge same nodes");
                }
            }
        }
        Input growth = new Input(Equation.LINEAR,0,1,0.5,0.1,1e-6,List.of(Method.values()));
        near(NumericalMethods.euler(Equation.LINEAR,0,0.5,0.2),0.8,1e-15,"Euler first step");
        near(NumericalMethods.rk4(Equation.LINEAR,0,0.5,0.2),0.8292933333333334,1e-15,"RK4 textbook step");
        for (Method m : Method.values()) {
            double coarse = maxError(growth,NumericalMethods.integrate(growth,m,20));
            double fine = maxError(growth,NumericalMethods.integrate(growth,m,40));
            check(coarse/fine > Math.pow(2,m.order)*0.65, "observed convergence order " + m + ": " + coarse/fine);
        }
        Input uneven = new Input(Equation.SINE,0,1,0,0.3,0.001,List.of(Method.values()));
        check(Solver.baseSteps(uneven)==4,"nondivisible h");
        check(Solver.baseSteps(new Input(Equation.SINE,0,1,0,2,0.01,List.of(Method.ADAMS)))==4,"large h multistep startup");
        Input zero = new Input(Equation.SQUARE,0,2,0,0.5,1e-12,List.of(Method.values()));
        for (Method m : Method.values()) near(Solver.solve(zero,m,4).exactError(),0,0,"zero solution " + m);
        rejects(() -> new Input(Equation.SQUARE,0,2,1,0.1,0.01,List.of(Method.RK4)),"interior pole");
        rejects(() -> new Input(Equation.SQUARE,0,1,1,0.1,0.01,List.of(Method.RK4)),"endpoint pole");
        rejects(() -> new Input(Equation.SINE,1,0,0,0.1,0.01,List.of(Method.RK4)),"reversed interval");
        rejects(() -> new Input(Equation.SINE,0,1,0,0,0.01,List.of(Method.RK4)),"zero step");
        rejects(() -> new Input(Equation.SINE,0,1,0,0.1,-1,List.of(Method.RK4)),"negative tolerance");
        rejects(() -> new Input(Equation.SINE,0,1,0,0.1,0.01,List.of()),"empty methods");
        rejects(() -> Solver.baseSteps(new Input(Equation.SINE,0,1,0,1e-10,0.01,List.of(Method.RK4))),"node cap");
        rejects(() -> Solver.solve(new Input(Equation.LINEAR,0,1,0.5,0.1,1e-20,List.of(Method.RK4)),Method.RK4,10),"unattainable precision");
        rejects(() -> Solver.solve(new Input(Equation.SINE,1e16,1e16+4,0,1,0.1,List.of(Method.RK4)),Method.RK4,4),"unrepresentable grid");
        check(Json.encode(Map.of("s","\"\n\\\t")).contains("\\u000a"),"JSON controls");
        check(Arrays.equals(Method.values(), new Method[]{Method.EULER, Method.RK4, Method.ADAMS}), "variant 15 = 1, 3, 4");
        rejects(() -> Method.byId("heun"), "method outside variant 15");
        rejects(() -> Method.byId("milne"), "method outside variant 15");
        System.out.println("PASS: " + checks + " checks (formulas, convergence, Runge, analytic solutions, invalid input)");
    }
    private static double maxError(Input p,double[] y) {
        double max=0;
        for (int i=0;i<y.length;i++) max=Math.max(max,Math.abs(y[i]-p.equation().exact(NumericalMethods.x(p,i,y.length-1),p.x0(),p.y0())));
        return max;
    }
    private static void check(boolean ok,String name) { checks++; if (!ok) throw new AssertionError(name); }
    private static void near(double a,double b,double eps,String name) { check(Math.abs(a-b)<=eps,name+": "+a+" vs "+b); }
    private static void rejects(Runnable task,String name) {
        try { task.run(); } catch (IllegalArgumentException ex) { checks++; return; }
        throw new AssertionError("Expected rejection: " + name);
    }
}
