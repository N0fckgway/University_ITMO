package ru.suvorov.server.managers;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode
@ToString
public class Command {
    public String name;
    public String description;


    public Command(String name, String description) {
        this.name = name;
        this.description = description;

    }
    public Command() {

    }
    public static Command getCommand() {
        return new Command();
    }

}
