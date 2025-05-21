package ru.suvorov.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.suvorov.exception.InvalidDataException;
import ru.suvorov.util.Validtable;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Human implements Validtable, Serializable, Comparable<Human> {
    private static final long serialVersionUID = 1L;
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
        if (age < 0) {
            throw new InvalidDataException(this, "Invalid age");
        }
        if (height < 0) {
            throw new InvalidDataException(this, "Invalid height");
        }
    }

    @Override
    public int compareTo(Human o) {
        int nameCompare = this.name.compareToIgnoreCase(o.name);
        if (nameCompare != 0) return nameCompare;

        int ageCompare = this.age.compareTo(o.age);
        if (ageCompare != 0) return ageCompare;

        int heightCompare = Float.compare(this.height, o.height);
        if (heightCompare != 0) return heightCompare;

        return this.birthday.compareTo(o.birthday);
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", birthday=" + birthday +
                '}';
    }
}
