package ru.itmo.ode;

import java.util.*;

public final class Solver {
    public static final int MAX_STEPS = 131072;
    public record Result(Method method, int steps, int refinements, double h, double estimate,
                         double exactError, double[] values, List<Map<String, Object>> history) {}
    public static int baseSteps(Input p) {
        double count = Math.ceil((p.xn() - p.x0()) / p.h());
        if (count > MAX_STEPS / 2) throw new IllegalArgumentException("Начальная сетка слишком велика. Увеличьте h: допустимо до " + (MAX_STEPS/2) + " интервалов до проверки Рунге.");
        return Math.max(p.methods().stream().anyMatch(m -> m.multistep) ? 4 : 1, (int)count);
    }
    public static Result solve(Input p, Method m, int base) {
        int n = base, refinements = 0;
        List<Map<String, Object>> history = new ArrayList<>();
        String lastFailure = "";
        while (n <= (m.multistep ? MAX_STEPS : MAX_STEPS/2)) {
            try {
                double[] coarse = NumericalMethods.integrate(p, m, n);
                double[] accepted = coarse;
                int acceptedN = n;
                double error;
                if (m.multistep) {
                    error = exactError(p, coarse);
                } else {
                    accepted = NumericalMethods.integrate(p, m, n * 2);
                    acceptedN = n * 2;
                    error = 0;
                    for (int i = 0; i <= n; i++)
                        error = Math.max(error, Math.abs(accepted[2*i] - coarse[i]) / (Math.pow(2, m.order) - 1));
                }
                if (!Double.isFinite(error)) throw new ArithmeticException("Переполнение при оценке погрешности.");
                double actual = exactError(p, accepted);
                double scale = Arrays.stream(accepted).map(Math::abs).max().orElse(1);
                if (p.epsilon() < 8*Math.ulp(scale))
                    throw new IllegalArgumentException("Заданная ε меньше надёжного разрешения double при этом масштабе y. Увеличьте ε или измените масштаб.");
                history.add(Map.of("steps", acceptedN, "h", (p.xn()-p.x0())/acceptedN, "estimate", error));
                if (error <= p.epsilon()) return new Result(m, acceptedN, refinements,
                        (p.xn()-p.x0())/acceptedN, error, actual, accepted, history);
                lastFailure = "Последняя оценка погрешности: " + error + ".";
            } catch (ArithmeticException ex) {
                lastFailure = ex.getMessage();
                history.add(Map.of("steps", n, "message", lastFailure));
            }
            n *= 2;
            refinements++;
        }
        throw new IllegalArgumentException("Точность не достигнута в пределах " + MAX_STEPS + " шагов. " + lastFailure + " Увеличьте ε или уменьшите интервал.");
    }
    private static double exactError(Input p, double[] y) {
        double max = 0;
        for (int i = 0; i < y.length; i++) {
            double e = Math.abs(p.equation().exact(NumericalMethods.x(p, i, y.length-1), p.x0(), p.y0()) - y[i]);
            if (!Double.isFinite(e)) throw new ArithmeticException("Точное решение или его погрешность выходит за диапазон double.");
            max = Math.max(max, e);
        }
        return max;
    }
    public static Map<String, Object> response(Input p) {
        int base = baseSteps(p);
        List<Object> results = new ArrayList<>();
        for (Method m : p.methods()) {
            Map<String, Object> out = new LinkedHashMap<>();
            out.put("id", m.id); out.put("title", m.title); out.put("order", m.order);
            out.put("criterion", m.multistep ? "exact" : "runge");
            try {
                Result r = solve(p, m, base);
                out.put("status", "ok"); out.put("steps", r.steps()); out.put("h", r.h());
                out.put("estimate", r.estimate()); out.put("exactError", r.exactError());
                out.put("refinements", r.refinements()); out.put("history", r.history());
                List<Object> rows = new ArrayList<>();
                for (int i = 0; i <= r.steps(); i++) {
                    double x = NumericalMethods.x(p, i, r.steps());
                    double exact = p.equation().exact(x, p.x0(), p.y0());
                    rows.add(new double[]{x, r.values()[i], exact, Math.abs(r.values()[i] - exact)});
                }
                out.put("rows", rows);
            } catch (IllegalArgumentException ex) {
                out.put("status", "error"); out.put("message", ex.getMessage());
            }
            results.add(out);
        }
        return Map.of("equation", p.equation().formula, "solution", p.equation().solution,
                "baseSteps", base, "baseH", (p.xn()-p.x0())/base, "results", results,
                "input", Map.of("x0", p.x0(), "xn", p.xn(), "y0", p.y0(), "h", p.h(), "epsilon", p.epsilon()));
    }
}
