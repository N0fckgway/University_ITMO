package ru.itmo.ode;

/** Right-hand sides and analytical solutions of the same initial-value problem. */
public enum Equation {
    LINEAR("linear", "y′ = y − x² + 1", "y = (x + 1)² + (y₀ − (x₀ + 1)²)e^(x − x₀)") {
        public double f(double x, double y) { return y - x*x + 1; }
        public double exact(double x, double x0, double y0) {
            double c = y0 - (x0 + 1)*(x0 + 1);
            return (x + 1)*(x + 1) + (c == 0 ? 0 : c * Math.exp(x - x0));
        }
    },
    SINE("sine", "y′ = cos(x)", "y = y₀ + sin(x) − sin(x₀)") {
        public double f(double x, double y) { return Math.cos(x); }
        public double exact(double x, double x0, double y0) { return y0 + Math.sin(x) - Math.sin(x0); }
    },
    SQUARE("square", "y′ = y²", "y = y₀ / (1 − y₀(x − x₀))") {
        public double f(double x, double y) { return y*y; }
        public double exact(double x, double x0, double y0) { return y0 == 0 ? 0 : y0 / (1 - y0*(x - x0)); }
    };

    public final String id, formula, solution;
    Equation(String id, String formula, String solution) {
        this.id = id; this.formula = formula; this.solution = solution;
    }
    public abstract double f(double x, double y);
    public abstract double exact(double x, double x0, double y0);
    public static Equation byId(String id) {
        for (Equation e : values()) if (e.id.equals(id)) return e;
        throw new IllegalArgumentException("Выберите одно из предложенных уравнений.");
    }
}
