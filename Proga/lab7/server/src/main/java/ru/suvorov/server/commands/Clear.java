package ru.suvorov.server.commands;


import com.github.javafaker.App;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;

import ru.suvorov.server.commands.interfaces.Executable;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.managers.CollectionManager;
import ru.suvorov.server.managers.Command;

public class Clear extends Command implements Executable {
    private final CollectionManager collectionManager;
    private final DBManager dbManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
        this.dbManager = new DBManager(new DBConnector());
    }

    @Override
    public ExecutionResponse apply(Object arg, CommandRequest commandRequest) {
        // Проверяем авторизацию пользователя
        String username = commandRequest.getUsername();
        String password = commandRequest.getPassword();
        
        if (username == null || password == null) {
            return new ExecutionResponse(false, "Ошибка: не указаны логин или пароль");
        }
        
        try {
            int countBefore = collectionManager.show().stream()
                    .filter(city -> username.equals(city.getOwnerUsername()))
                    .mapToInt(city -> 1)
                    .sum();
            
            // Удаляем только объекты пользователя
            collectionManager.clearUserObjects(username);
            
            return new ExecutionResponse(true, 
                "Успешно удалено " + countBefore + " объектов пользователя " + username);
                
        } catch (Exception e) {
            return new ExecutionResponse(false, "Ошибка при очистке коллекции: " + e.getMessage());
        }
    }
}
