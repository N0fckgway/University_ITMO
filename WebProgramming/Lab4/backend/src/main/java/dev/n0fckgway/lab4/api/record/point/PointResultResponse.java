package dev.n0fckgway.lab4.api.record.point;

public record PointResultResponse(
        Long id,
        Double x,
        Double y,
        Double r,
        boolean hit,
        String checkedAt
) {
}
