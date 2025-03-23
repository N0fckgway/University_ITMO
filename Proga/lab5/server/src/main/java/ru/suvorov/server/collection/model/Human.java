package ru.suvorov.server.collection.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.suvorov.common.exception.InvalidDataException;
import ru.suvorov.common.util.Validtable;

import java.time.ZonedDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Human implements Validtable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer age; //Значение поля должно быть больше 0
    private float height; //Значение поля должно быть больше 0
    private java.time.ZonedDateTime birthday;

    public Human(String name, Integer age, float height, ZonedDateTime birthday) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.birthday = birthday;
    }

    @Override
    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException(this, "Invalid name");
        }
        if (age > 0) {
            throw new InvalidDataException(this, "Invalid age");
        }
        if (height > 0) {
            throw new InvalidDataException(this, "Invalid height");
        }
    }

}
