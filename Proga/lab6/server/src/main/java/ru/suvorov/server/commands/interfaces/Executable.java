package ru.suvorov.server.commands.interfaces;

import ru.suvorov.ExecutionResponse;

public interface Executable {
    ExecutionResponse apply(Object arg) throws Exception;
}
