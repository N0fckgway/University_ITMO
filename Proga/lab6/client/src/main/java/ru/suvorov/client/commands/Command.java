package ru.suvorov.client.commands;

import java.io.Serializable;

public class Command implements Serializable {
    private String name;
    private Object argument;

    public Command(String name, Object argument) {
        this.name = name;
        this.argument = argument;
    }

    public Command() {
        this.name = "";
        this.argument = null;
    }

    public String getName() {
        return name;
    }

    public Object getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        if (argument != null && argument instanceof String && !((String) argument).isEmpty()) {
            return name + " " + argument;
        } else if (argument != null && !(argument instanceof String)) {
            return name + " " + argument;
        } else {
            return name;
        }
    }
} 