package dev.n0fckgway.lab4.api.record.point;

import jakarta.validation.constraints.NotNull;

public record PointCheckRequest(
        @NotNull Double x,
        @NotNull Double y,
        @NotNull Double r,
        @NotNull Boolean fromGraph
) {
}
