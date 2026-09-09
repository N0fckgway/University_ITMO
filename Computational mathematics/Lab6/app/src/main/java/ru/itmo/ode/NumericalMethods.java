package ru.itmo.ode;

public final class NumericalMethods {
    private NumericalMethods() {}
    public static double euler(Equation e, double x, double y, double h) {
        return y + h * e.f(x, y);
    }
    public static double rk4(Equation e, double x, double y, double h) {
        double k1 = h * e.f(x, y);
        double k2 = h * e.f(x + h/2, y + k1/2);
        double k3 = h * e.f(x + h/2, y + k2/2);
        double k4 = h * e.f(x + h, y + k3);
        return y + (k1 + 2*k2 + 2*k3 + k4)/6;
    }
    public static double[] integrate(Input p, Method method, int n) {
        double h = (p.xn() - p.x0()) / n;
        double[] y = new double[n + 1];
        double[] f = method.multistep ? new double[n + 1] : null;
        y[0] = p.y0();
        if (f != null) f[0] = p.equation().f(p.x0(), y[0]);
        for (int i = 1; i <= n; i++) {
            double prevX = x(p, i - 1, n), nextX = x(p, i, n);
            if (nextX <= prevX || (method != Method.EULER && prevX + h/2 == prevX))
                throw new ArithmeticException("Шаг меньше разрешения double для этих x. Измените масштаб интервала.");
            if (method.multistep && i >= 4) {
                y[i] = adams(p, i, nextX, h, y, f);
            } else {
                y[i] = switch (method) {
                    case EULER -> euler(p.equation(), prevX, y[i - 1], h);
                    default -> rk4(p.equation(), prevX, y[i - 1], h);
                };
            }
            finite(y[i]);
            if (f != null) { f[i] = p.equation().f(nextX, y[i]); finite(f[i]); }
        }
        return y;
    }
    private static double adams(Input p, int i, double x, double h, double[] y, double[] f) {
        double value = y[i-1] + h/24 * (55*f[i-1] - 59*f[i-2] + 37*f[i-3] - 9*f[i-4]);
        finite(value);
        // Iteration tolerance is tighter than the requested global error.
        double tolerance = p.epsilon() * 0.01 / y.length;
        for (int iteration = 0; iteration < 100; iteration++) {
            double next = y[i-1] + h/24 * (9*p.equation().f(x, value) + 19*f[i-1] - 5*f[i-2] + f[i-3]);
            finite(next);
            if (Math.abs(next - value) <= Math.max(tolerance, 4*Math.ulp(next))) return next;
            value = next;
        }
        throw new ArithmeticException("Корректор не сошёлся за 100 итераций.");
    }
    public static double x(Input p, int i, int n) {
        return i == n ? p.xn() : p.x0() + (p.xn() - p.x0()) * ((double)i/n);
    }
    private static void finite(double value) {
        if (!Double.isFinite(value)) throw new ArithmeticException("Переполнение при вычислении. Уменьшите шаг или интервал.");
    }
}
