package ru.suvorov.server.managers;

import lombok.EqualsAndHashCode;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public abstract class Command {
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
}
