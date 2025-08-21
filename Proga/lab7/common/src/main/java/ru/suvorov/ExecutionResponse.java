package ru.suvorov;

import java.io.Serializable;

public class ExecutionResponse implements Serializable {
    private final boolean exitCode;
    private final String message;

    public ExecutionResponse(boolean exitCode, String message) {
        this.exitCode = exitCode;
        this.message = message;
    }

    public ExecutionResponse(String mess) {
        this(true, mess);
    }

    public String getMessage() {
        return message;
    }

    public boolean getExitCode() {
        return exitCode;
    }

    @Override
    public String toString() {
        return exitCode + ";" + message + ";";

    }
}

