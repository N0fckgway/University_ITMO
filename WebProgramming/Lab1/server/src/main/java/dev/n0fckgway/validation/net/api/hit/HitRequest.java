package dev.n0fckgway.validation.net.api.hit;

import java.util.Locale;

public record HitRequest(double x, double y, double r) {
    public String json() {
        return String.format(Locale.US, """
                {"x": %f, "y": %f, "r": %f}""", x, y, r);

    }

}
