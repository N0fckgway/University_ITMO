package ru.suvorov;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class CommandRequest implements Serializable {
    private Long id;
    private String commandName;
    private Object argument;
    private String username;
    private String password;
    public RequestType requestType;

    public CommandRequest(String commandName, Object argument, String username, String password) {
        this.commandName = commandName;
        this.argument = argument;
        this.username = username;
        this.password = password;
    }

    public CommandRequest(Long id, Object argument) {
        this.id = id;
        this.argument = argument;
    }

    public CommandRequest(String commandName, Object argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public CommandRequest(String username, String password, RequestType requestType) {
        this.username = username;
        this.password = password;
        this.requestType = requestType;
    }

    public CommandRequest(String commandName, Long id, Object object) {
        this.commandName = commandName;
        this.id = id;
        this.argument = object;
    }

    public CommandRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "CommandRequest{" +
                "commandName='" + commandName + '\'' +
                ", argument=" + argument +
                '}';
    }

} 