package dev.n0fckgway.lab2.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Point {
    private double x;
    private double y;
    private double r;

    public Point(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;

    }

    public static boolean validation(double x, double y, double r) {
        if (x <= -5 || x >= 5 || y <= -6 || y >= 4 || r <= 0 || r >= 6) {
            throw new IllegalArgumentException("Значения вне допустимого диапазона");
        }
        return true;

    }

}
