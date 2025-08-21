package ru.suvorov.server.commands.interfaces;

import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

public interface Executable {
    ExecutionResponse apply(Object arg, CommandRequest commandRequest) throws Exception;
}
