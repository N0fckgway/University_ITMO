package ru.itmo.ode;

public enum Method {
    EULER("euler", "Эйлер", 1, false),
    RK4("rk4", "Рунге–Кутта 4", 4, false),
    ADAMS("adams", "Адамс", 4, true);
    public final String id, title;
    public final int order;
    public final boolean multistep;
    Method(String id, String title, int order, boolean multistep) {
        this.id = id; this.title = title; this.order = order; this.multistep = multistep;
    }
    public static Method byId(String id) {
        for (Method m : values()) if (m.id.equals(id)) return m;
        throw new IllegalArgumentException("Неизвестный численный метод: " + id);
    }
}
