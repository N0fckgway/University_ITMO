package dev.n0fckgway.lab4.api.record.auth;

public record RegisterResponse(String message, boolean success, String token) {
    public RegisterResponse(String message, boolean success) {
        this(message, success, null);
    }
}
