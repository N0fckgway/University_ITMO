package ru.suvorov.server.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.suvorov.model.City;
import ru.suvorov.server.util.StadartConsole;


import java.io.*;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DumpManager {
    private final String filename;
    private final StadartConsole console;
    private final Gson gson;

    public DumpManager(String filename, StadartConsole console) {
        this.filename = filename;
        this.console = console;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public List<City> readCollection() {
        File file = new File(filename);
        if (!file.exists()) {
            console.println("Файл не найден. Создана пустая коллекция.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<ArrayList<City>>(){}.getType();
            List<City> cities = gson.fromJson(reader, type);
            if (cities == null) {
                console.println("Файл пуст. Создана пустая коллекция.");
                return new ArrayList<>();
            }
            console.println("Коллекция успешно загружена из файла.");
            return cities;
        } catch (IOException e) {
            console.printError("Ошибка при чтении файла: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            console.printError("Ошибка при разборе JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void writeCollection(List<City> collection) {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(collection, writer);
            console.println("Коллекция успешно сохранена в файл.");
        } catch (IOException e) {
            console.printError("Ошибка при записи в файл: " + e.getMessage());
        }
    }
} 