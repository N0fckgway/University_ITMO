package dev.n0fckgway.lab3.model;

public class AreaChecker {

    private AreaChecker() {

    }

    public static boolean isHit(Double x, Double y, Double r) {
        return (x >= -r && x <= 0 && y >= 0 && y <= r / 2.0) ||
                (x >= 0 && y >= 0 && (x * x + y * y) <= r * r) ||
                (x >= 0 && y <= 0 && y >= (2 * x - r));
    }

}
