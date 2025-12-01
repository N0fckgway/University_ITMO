package dev.n0fckgway.lab2.model;

public class AreaChecker {

    public static boolean checkHit(double x, double y, double r) {
        boolean inRectangle = x >= -r / 2 && x <= 0 && y >= 0 && y <= r;

        boolean inCircle = x <= 0 && y <= 0 && x * x + y * y <= (r * r) / 4.0;

        boolean inTriangle = x >= 0 && y <= 0 && y >= x - r;

        return inRectangle || inCircle || inTriangle;

    }

}
