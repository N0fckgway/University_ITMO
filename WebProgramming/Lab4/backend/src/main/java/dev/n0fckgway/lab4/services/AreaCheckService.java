package dev.n0fckgway.lab4.services;

import jakarta.ejb.Stateless;

@Stateless
public class AreaCheckService {

    public boolean isHit(double x, double y, double r) {
        if (r <= 0) {
            return false;
        }

        boolean inRectangle = x >= 0 && y >= 0 && x <= r && y <= r;
        boolean inTriangle = x >= 0 && y <= 0 && x <= r && y >= (x / 2.0 - r / 2.0);
        boolean inQuarterCircle = x <= 0 && y <= 0 && (x * x + y * y) <= (r * r);

        return inTriangle || inRectangle || inQuarterCircle;
    }
}
