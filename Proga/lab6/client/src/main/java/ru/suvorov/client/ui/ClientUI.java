package ru.suvorov.client.ui;

import ru.suvorov.client.ClientConnection;
import ru.suvorov.CommandRequest;
import ru.suvorov.ExecutionResponse;
import ru.suvorov.client.util.StadartConsole;
import java.util.NoSuchElementException;
import java.util.List;
import ru.suvorov.model.City;
import ru.suvorov.model.Coordinates;
import ru.suvorov.model.Human;
import ru.suvorov.enums.Climate;
import ru.suvorov.enums.Government;
import ru.suvorov.enums.StandardOfLiving;
import java.time.ZonedDateTime;

public class ClientUI {
    private final StadartConsole console;
    private final ClientConnection connection;
    private boolean running;

    public ClientUI(ClientConnection connection) {
        this.console = new StadartConsole();
        this.connection = connection;
        this.running = true;
    }

    public void start() {
        console.println("Клиент запущен. Введите 'help' для получения списка команд.");

        while (running) {
            try {
                String input = console.readln();
                if (input == null || input.trim().isEmpty()) {
                    continue;
                }

                String[] parts = input.trim().split("\\s+", 2);
                String commandName = parts[0].toLowerCase();
                String argument = parts.length > 1 ? parts[1] : null;

                if (commandName.isEmpty()) {
                    console.printError("Пустая команда. Введите команду.\n");
                    continue;
                }

                if (commandName.equals("save")) {
                    console.printError("Команда save недоступна в клиентском приложении\n");
                    continue;
                }

                if (commandName.equals("exit")) {
                    running = false;
                    console.println("Клиент завершает работу.");
                    break;
                }

                if (commandName.equals("add")) {
                    City city = readCityFromConsole(console);
                    CommandRequest request = new CommandRequest(commandName, city);
                    System.out.println("DEBUG: Отправляю объект: " + request);
                    Object response = connection.sendRequest(request);
                    System.out.println("DEBUG: Получен ответ: " + response + " (" + (response != null ? response.getClass() : "null") + ")");
                    if (response == null) {
                        console.printError("Нет ответа от сервера\n");
                    } else if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    } else {
                        console.printError("Неизвестный тип ответа от сервера\n");
                    }
                    continue;
                }

                if (commandName.equals("add_if_min")) {
                    City city = readCityFromConsole(console);
                    CommandRequest request = new CommandRequest(commandName, city);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("update_id")) {
                    console.print("Введите id для обновления: ");
                    String id = console.readln();
                    City city = readCityFromConsole(console);
                    CommandRequest request = new CommandRequest(commandName, id + "," + city.toString());
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("generate_random_obj")) {
                    String count = argument;
                    if (count == null || count.trim().isEmpty()) {
                        console.print("Введите количество случайных объектов (int): ");
                        count = console.readln();
                    }
                    CommandRequest request = new CommandRequest(commandName, count);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("remove_by_id")) {
                    console.print("Введите id для удаления: ");
                    String id = console.readln();
                    CommandRequest request = new CommandRequest(commandName, id);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("filter_less_than_governor")) {
                    console.print("Введите данные governor (имя возраст рост день_рождения): ");
                    String arg = console.readln();
                    CommandRequest request = new CommandRequest(commandName, arg);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("remove_greater")) {
                    City city = readCityFromConsole(console);
                    CommandRequest request = new CommandRequest(commandName, city);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("print_ascending") || commandName.equals("sum_of_meters_above_sea_level") || commandName.equals("info") || commandName.equals("clear") || commandName.equals("remove_first") || commandName.equals("help") || commandName.equals("exit")) {
                    CommandRequest request = new CommandRequest(commandName, null);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                if (commandName.equals("help")) {
                    console.println("help - вывести справку по доступным командам");
                    console.println("show - вывести все элементы коллекции");
                    console.println("add - добавить новый элемент в коллекцию");
                    console.println("add_if_min - добавить новый элемент, если он меньше минимального");
                    console.println("update_id - обновить элемент по id");
                    console.println("remove_by_id - удалить элемент по id");
                    console.println("remove_greater - удалить элементы, превышающие заданный");
                    console.println("clear - очистить коллекцию");
                    console.println("info - вывести информацию о коллекции");
                    console.println("print_ascending - вывести элементы в порядке возрастания");
                    console.println("sum_of_meters_above_sea_level - сумма значений metersAboveSeaLevel");
                    console.println("filter_less_than_governor - вывести элементы с governor меньше заданного");
                    console.println("generate_random_obj - добавить случайные элементы");
                    console.println("execute_script - выполнить команды из скрипта");
                    console.println("exit - завершить работу клиента");
                    continue;
                }

                if (commandName.equals("execute_script")) {
                    String filename = argument;
                    if (filename == null || filename.trim().isEmpty()) {
                        console.print("Введите имя файла скрипта: ");
                        filename = console.readln();
                    }
                    CommandRequest request = new CommandRequest(commandName, filename);
                    Object response = connection.sendRequest(request);
                    if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    }
                    continue;
                }

                try {
                    CommandRequest request = new CommandRequest(commandName, argument);
                    System.out.println("DEBUG: Отправляю объект: " + request);
                    Object response = connection.sendRequest(request);
                    System.out.println("DEBUG: Получен ответ: " + response + " (" + (response != null ? response.getClass() : "null") + ")");
                    if (response == null) {
                        console.printError("Нет ответа от сервера\n");
                    } else if (response instanceof ExecutionResponse) {
                        console.println(((ExecutionResponse) response).getMessage());
                    } else if (response instanceof List) {
                        console.println("Коллекция:");
                        for (Object obj : (List<?>) response) {
                            console.println(obj);
                        }
                    } else {
                        console.printError("Неизвестный тип ответа от сервера\n");
                    }
                } catch (IllegalArgumentException e) {
                    console.printError(e.getMessage() + "\n");
                }

            } catch (NoSuchElementException e) {
                console.printError("Ввод завершён. Клиент закрывается.\n");
                break;
            } catch (Exception e) {
                console.printError("Ошибка: " + e.getMessage() + "\n");
            }
        }
        connection.close();
    }

    // Метод для интерактивного ввода City с повтором при ошибке
    private City readCityFromConsole(StadartConsole console) {
        console.println("Ввод нового города:");
        City city = new City();
        // name
        String name;
        while (true) {
            console.print("Имя города: ");
            name = console.readln();
            if (name != null && !name.isEmpty()) break;
            console.printError("Имя не может быть пустым!\n");
        }
        city.setName(name);
        // coordinates
        long x;
        while (true) {
            console.print("Координата X (long): ");
            try {
                x = Long.parseLong(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        Long y;
        while (true) {
            console.print("Координата Y (Long > -583): ");
            try {
                y = Long.parseLong(console.readln());
                if (y > -583) break;
                else console.printError("Y должен быть больше -583!\n");
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        city.setCoordinates(new Coordinates(x, y));
        // area
        int area;
        while (true) {
            console.print("Площадь (целое > 0): ");
            try {
                area = Integer.parseInt(console.readln());
                if (area > 0) break;
                else console.printError("Площадь должна быть > 0!\n");
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        city.setArea(area);
        // population
        long population;
        while (true) {
            console.print("Население (long > 0): ");
            try {
                population = Long.parseLong(console.readln());
                if (population > 0) break;
                else console.printError("Население должно быть > 0!\n");
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        city.setPopulation(population);
        // metersAboveSeaLevel
        double metersAboveSeaLevel;
        while (true) {
            console.print("Высота над уровнем моря (double): ");
            try {
                metersAboveSeaLevel = Double.parseDouble(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        city.setMetersAboveSeaLevel(metersAboveSeaLevel);
        // climate
        Climate climate = null;
        while (true) {
            console.print("Климат (RAIN_FOREST, MONSOON, HUMIDSUBTROPICAL): ");
            try {
                climate = Climate.valueOf(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите одно из значений: RAIN_FOREST, MONSOON, HUMIDSUBTROPICAL!\n");
            }
        }
        city.setClimate(climate);
        // government
        Government government = null;
        while (true) {
            console.print("Правительство (KLEPTOCRACY, PLUTOCRACY, REPUBLIC, TIMOCRACY): ");
            try {
                government = Government.valueOf(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите одно из значений: KLEPTOCRACY, PLUTOCRACY, REPUBLIC, TIMOCRACY!\n");
            }
        }
        city.setGovernment(government);
        // standardOfLiving
        StandardOfLiving sol = null;
        while (true) {
            console.print("Уровень жизни (HIGH, LOW, VERY_LOW, ULTRA_LOW): ");
            try {
                sol = StandardOfLiving.valueOf(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите одно из значений: HIGH, LOW, VERY_LOW, ULTRA_LOW!\n");
            }
        }
        city.setStandardOfLiving(sol);
        // governor
        String govName;
        while (true) {
            console.print("Имя губернатора: ");
            govName = console.readln();
            if (govName != null && !govName.isEmpty()) break;
            console.printError("Имя не может быть пустым!\n");
        }
        int govAge;
        while (true) {
            console.print("Возраст губернатора (int > 0): ");
            try {
                govAge = Integer.parseInt(console.readln());
                if (govAge > 0) break;
                else console.printError("Возраст должен быть > 0!\n");
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        float govHeight;
        while (true) {
            console.print("Рост губернатора (float > 0): ");
            try {
                govHeight = Float.parseFloat(console.readln());
                if (govHeight > 0) break;
                else console.printError("Рост должен быть > 0!\n");
            } catch (Exception e) {
                console.printError("Введите корректное число!\n");
            }
        }
        ZonedDateTime govBirthday;
        while (true) {
            console.print("День рождения губернатора (например, 2000-01-01T00:00:00+03:00[Europe/Moscow]): ");
            try {
                govBirthday = ZonedDateTime.parse(console.readln());
                break;
            } catch (Exception e) {
                console.printError("Введите дату в правильном формате!\n");
            }
        }
        city.setGovernor(new Human(govName, govAge, govHeight, govBirthday));
        return city;
    }
} 