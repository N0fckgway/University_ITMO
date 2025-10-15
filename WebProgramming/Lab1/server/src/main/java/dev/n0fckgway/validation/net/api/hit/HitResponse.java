package dev.n0fckgway.validation.net.api.hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record HitResponse(Boolean hit, LocalDateTime dateTime, long execTime) {
    public String json() {
        return """
                {"hit": %b, "requestTime": "%s", "execTime": %d}"""
                .formatted(hit, dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), execTime);

    }
}
