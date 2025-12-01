package dev.n0fckgway.lab2.model;

import java.time.LocalDateTime;

public record HitResult(double x, double y, double r, boolean hit, LocalDateTime nowTime, long execTime) {
    public String toJson() {
        return """
               {
                    "x": %s
                    "y": %s
                    "r": %s
                    "hit": %s
                    "nowTime": %s
                    "execTime": %d
               }
               """.formatted(x(), y(), r(), hit(), nowTime(), execTime());
    }


}
