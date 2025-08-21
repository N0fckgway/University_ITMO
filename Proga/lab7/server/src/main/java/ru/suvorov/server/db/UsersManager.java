package ru.suvorov.server.db;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.server.exception.DataBaseException;


import java.util.HashMap;
@Getter
@Slf4j
public class UsersManager {
    public static DBManager dbManager;
    public static HashMap<String, String> userData;

    public UsersManager(DBManager dbManager, HashMap<String, String> userData) {
        UsersManager.dbManager = dbManager;
        UsersManager.userData = userData;
    }

    public ExecutionResponse registerUser(CommandRequest commandRequest) {
        try {
            if (!dbManager.checkUsersExistence(commandRequest.getUsername())) {
                dbManager.addUser(commandRequest.getUsername(), commandRequest.getPassword());
                userData.put(commandRequest.getUsername(), commandRequest.getPassword());
                return new ExecutionResponse(true, "Регистрация прошла успешно, пользователь добавлен");
            } else {
                return new ExecutionResponse(false, "Пользователь с таким логином уже существует в системе");
            }
        } catch (DataBaseException e) {
            throw new DataBaseException("При регистрации пользователя произошла ошибка: " + e.getMessage());
        }
    }
   public ExecutionResponse logInUser(CommandRequest commandRequest) {
        try {
            if (dbManager.checkUsersExistence(commandRequest.getUsername())) {
                userData.put(commandRequest.getUsername(), commandRequest.getPassword());
                return new ExecutionResponse(true, "Вход успешно выполнен: Добро пожаловать " + commandRequest.getUsername());

            } else {
                userData.put(commandRequest.getUsername(), commandRequest.getPassword());
                return new ExecutionResponse(false, "Этого имени пользователя не существует.");

            }
        } catch (DataBaseException e) {
            return new ExecutionResponse(false, e.getMessage());
        }
   }

}
