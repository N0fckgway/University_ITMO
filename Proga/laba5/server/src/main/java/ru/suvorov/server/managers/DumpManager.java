package ru.suvorov.server.managers;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.suvorov.server.util.StadartConsole;
import ru.suvorov.server.collection.model.City;

import java.lang.reflect.Type;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

public class DumpManager {
    public final String fileName;
    public final StadartConsole console;
    Gson builder = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .create();

    public DumpManager(String fileName, StadartConsole console) {
        if (!(new File(fileName).exists())) {
            fileName = "../" + fileName;
        }
        this.fileName = fileName;
        this.console = console;
    }

    public void writeCollection(LinkedList<City> collection) {
        try (OutputStream os = new FileOutputStream(fileName)) {
            Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            builder.toJson(collection, writer);
            console.println(fileName + "сохранен в коллекцию");
        } catch (FileNotFoundException e) {
            console.printError("УПППС ВОТ ЭТО ПОВОРОТ, ЗАГРУЗОЧНЫЙ ФАЙЛ НЕ ОПРЕДЕЛИЛСЯ");
        } catch (IOException e) {
            console.printError("Ошибка в записи в файл: " + e.getMessage());

        }
    }

    public Collection<City> readCollection(LinkedList<City> collection) {
        if (fileName == null || fileName.isEmpty()) {
            console.printError("Аргумент командной строки с загрузочным файлом не найден!");
            return new PriorityQueue<>();
        }

        try (Scanner scanner = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");

            String jsonString = scanner.hasNext() ? scanner.next().trim() : "[]";

            if (jsonString.isEmpty()) {
                jsonString = "[]";
            }

            Type collectionType = new TypeToken<PriorityQueue<City>>() {}.getType();
            PriorityQueue<City> collect = builder.fromJson(jsonString, collectionType);

            console.println("Коллекция успешно загружена!");
            return collection != null ? collection : new PriorityQueue<>();

        } catch (FileNotFoundException e) {
            console.printError("Загрузочный файл не найден!");
        } catch (NoSuchElementException e) {
            console.printError("Загрузочный файл пуст!");
        } catch (JsonParseException e) {
            console.printError("В загрузочном файле не обнаружена необходимая коллекция!");
        } catch (IOException e) {
            console.printError("Ошибка чтения файла: " + e.getMessage());
        } catch (Exception e) {
            console.printError("Непредвиденная ошибка: " + e.getMessage());
        }

        return new PriorityQueue<>();
    }

}

