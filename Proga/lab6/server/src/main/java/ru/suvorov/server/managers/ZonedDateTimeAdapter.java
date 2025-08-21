package ru.suvorov.server.managers;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(dateTimeFormatter.format(zonedDateTime.toLocalDateTime()));

    }

    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
        return ZonedDateTime.parse(jsonElement.getAsString());
    }


}
