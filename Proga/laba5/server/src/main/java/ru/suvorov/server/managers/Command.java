package ru.suvorov.server.managers;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.suvorov.server.ExecutionResponse;
import ru.suvorov.server.commands.interfaces.Executable;


@EqualsAndHashCode
@ToString
public abstract class Command implements Executable {
    public String name;
    public String description;


    public Command(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public ExecutionResponse apply(String s) throws Exception {
        return new ExecutionResponse(true, "Элемент добавлен");
    }
}
