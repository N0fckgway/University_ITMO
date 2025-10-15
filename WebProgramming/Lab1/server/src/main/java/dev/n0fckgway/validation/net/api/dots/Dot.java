package dev.n0fckgway.validation.net.api.dots;

import dev.n0fckgway.validation.net.api.hit.HitRequest;
import dev.n0fckgway.validation.net.api.hit.HitResponse;

public record Dot(HitRequest req, HitResponse res) {
    public String json() {
        return """
                {"req": %s, "resp": %s}""".formatted(req.json(), res.json());
    }
}
