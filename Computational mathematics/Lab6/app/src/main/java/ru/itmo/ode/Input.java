package ru.itmo.ode;

import java.util.*;
import java.math.BigDecimal;

public record Input(Equation equation, double x0, double xn, double y0, double h,
                    double epsilon, List<Method> methods) {
    public static double number(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Заполните поле «" + label + "».");
        String s = value.strip().replace(',', '.');
        if (!s.matches("[+-]?(?:[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+)(?:[eE][+-]?[0-9]+)?"))
            throw new IllegalArgumentException("«" + label + "»: введите число, например 0,1 или 1e-6.");
        try {
            double n = Double.parseDouble(s);
            if (!Double.isFinite(n) || (n == 0 && new BigDecimal(s).signum() != 0))
                throw new NumberFormatException();
            return n;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("«" + label + "»: число вне диапазона double. Измените масштаб данных.");
        }
    }
    public static Input from(Map<String, String> p) {
        String ids = p.getOrDefault("methods", "");
        if (ids.isBlank()) throw new IllegalArgumentException("Выберите хотя бы один метод.");
        List<Method> methods = Arrays.stream(ids.split(",", -1)).map(Method::byId).distinct().toList();
        return new Input(Equation.byId(p.get("equation")), number(p.get("x0"), "x₀"),
                number(p.get("xn"), "xₙ"), number(p.get("y0"), "y₀"),
                number(p.get("h"), "h"), number(p.get("epsilon"), "ε"), methods);
    }
    public Input {
        methods = List.copyOf(methods);
        if (equation == null || methods.isEmpty()) throw new IllegalArgumentException("Выберите уравнение и методы.");
        for (double v : new double[]{x0, xn, y0, h, epsilon})
            if (!Double.isFinite(v)) throw new IllegalArgumentException("Все исходные числа должны быть конечными.");
        if (!(xn > x0) || !Double.isFinite(xn - x0)) throw new IllegalArgumentException("Правая граница xₙ должна быть больше x₀; длина интервала должна быть конечной.");
        if (!(h > 0)) throw new IllegalArgumentException("Шаг h должен быть положительным.");
        if (!(epsilon > 0)) throw new IllegalArgumentException("Точность ε должна быть положительной.");
        if (equation == Equation.SQUARE && y0 > 0 && xn - x0 >= 1 / y0)
            throw new IllegalArgumentException("На интервале есть разрыв решения: x = x₀ + 1/y₀. Уменьшите xₙ так, чтобы он был левее разрыва.");
        if (!Double.isFinite(equation.exact(x0, x0, y0)) || !Double.isFinite(equation.exact(xn, x0, y0)))
            throw new IllegalArgumentException("Точное решение выходит за диапазон double. Уменьшите интервал или масштаб начальных данных.");
    }
}
