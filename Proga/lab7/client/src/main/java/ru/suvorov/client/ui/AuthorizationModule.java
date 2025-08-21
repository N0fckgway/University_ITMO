package ru.suvorov.client.ui;


import java.util.*;

import org.checkerframework.checker.units.qual.A;
import ru.suvorov.CommandRequest;
import ru.suvorov.RequestType;
import ru.suvorov.client.util.App;
import ru.suvorov.server.db.DBManager;
import ru.suvorov.server.db.UsersManager;
import ru.suvorov.server.db.DBConnector;

import ru.suvorov.server.util.Sha_512;
import ru.suvorov.server.util.StadartConsole;

public class AuthorizationModule {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean authorizationDone = false;
    private String login;
    private String password;
    private static HashMap<String, String> userData = new HashMap<>();







    public static CommandRequest askForRegistration() {

        StadartConsole stadartConsole = new StadartConsole();
        stadartConsole.print("У вас есть аккаунт? [y/n]");
        while (true) {
            try {
                String s = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
                if ("y".equals(s)) {
                    return loginUser();
                } else if ("n".equals(s)) {
                    return registerUser();
                } else {
                    stadartConsole.printError("Вы ввели недопустимый символ, попробуйте еще раз.");
                }
            } catch (NoSuchElementException e) {
                stadartConsole.printError("Принудительное завершение работы.");
                System.exit(1);
            }
        }
    }


//    public static void validateRegistration(Response response) {
//        StadartConsole stadartConsole = new StadartConsole();
//
//    }
    private static CommandRequest loginUser() throws NoSuchElementException {
        StadartConsole stadartConsole = new StadartConsole();
        DBManager dbManager = new DBManager(new DBConnector());
        UsersManager usersManager = new UsersManager(dbManager, userData);
        stadartConsole.print("Добро пожаловать в авторизацию! ");
        String password;
        String login;
        while (true) {
            stadartConsole.print("Введите имя пользователя, которое вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
            while (true) {
                login = scanner.nextLine().trim();
                if (login.length() < 5) {
                    stadartConsole.printError("Имя пользователя слишком короткое, попробуйте еще раз.");
                    continue;
                }
                stadartConsole.print("Введите пароль, который вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
                password = scanner.nextLine().trim();
                if (password.length() < 5) {
                    stadartConsole.print("Пароль слишком короткий, попробуйте еще раз.");
                } else if (!(dbManager.checkUsersExistence(login))) {
                    stadartConsole.println("Пользователь не существует");
                    continue;
                }

                break;
            }
            usersManager.logInUser(new CommandRequest(login, password, RequestType.LOGIN));
            setAuthorizationDone(true);
            App.currentUsername = login;
            App.currentUserPass = password;
            return new CommandRequest(login, password, RequestType.LOGIN);

        }
    }

    private static CommandRequest registerUser() throws NoSuchElementException {
        DBConnector dbConnector = new DBConnector();
        StadartConsole stadartConsole = new StadartConsole();
        stadartConsole.print("Добро пожаловать в регистрации!");
        String login;
        String password;
        Sha_512 sha512 = new Sha_512();
        DBManager dbManager = new DBManager(dbConnector);
        UsersManager usersManager = new UsersManager(dbManager, userData);
        while (true) {
            while (true) {
                stadartConsole.print("Введите имя пользователя, которое вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
                login = scanner.nextLine().trim();
                if (login.length() < 5) {
                    stadartConsole.print("Имя пользователя слишком короткое, попробуйте еще раз. Или пользователь уже существует");
                } else if (dbManager.checkUsersExistence(login)) {
                    stadartConsole.println("Пользователь существует");
                    continue;
                }
                break;
            }
            stadartConsole.print("Введите пароль, который вы будете использовать для работы с приложением (должно содержать не менее 5 символов).");
            while (true) {
                try {
                    password = scanner.nextLine().trim();
                    if (password.length() < 5) {
                        stadartConsole.printError("Пароль слишком короткий, попробуйте еще раз.");
                        continue;
                    }
                    password = sha512.encode_512(password);
                    usersManager.registerUser(new CommandRequest(login, password, RequestType.REGISTER));
                    setAuthorizationDone(true);
                    App.currentUsername = login;
                    App.currentUserPass = password;
                    return new CommandRequest(login, password, RequestType.REGISTER);
                } catch (NoSuchElementException | IllegalStateException e) {
                    stadartConsole.printError("Ошибка при работе с консолью.");
                    System.exit(1);
                }
            }
        }
    }

    public HashMap<String, String> getUserData() {
        return userData;
    }



    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Метод, возвращающий информацию о том, выполнена ли авторизация в приложении.
     * @return true, если авторизация была выполнена успешно, иначе false.
     */
    public static boolean isAuthorizationDone() {
        return authorizationDone;
    }

    /**
     * Метод, устанавливающий флаг выполнения авторизации в приложении.
     * @param authorizationDone флаг выполнения авторизации.
     */
    public static void setAuthorizationDone(boolean authorizationDone) {
        AuthorizationModule.authorizationDone = authorizationDone;
    }
}
