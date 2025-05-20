package ru.suvorov.utilities.collection.commands.interfaces;

import ru.suvorov.server.ExecutionResponse;


public interface Executable {
    ExecutionResponse apply(String arg) throws Exception;
}
