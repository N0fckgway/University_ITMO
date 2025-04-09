package ru.suvorov.server.builder;

import lombok.Getter;
import ru.suvorov.server.collection.model.Human;

import java.time.ZonedDateTime;


@Getter
public class ConcreateHumanBuilder implements BuilderHuman{
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Integer age; //Значение поля должно быть больше 0
    private float height; //Значение поля должно быть больше 0
    private java.time.ZonedDateTime birthday;

    @Override
    public BuilderHuman setName(String name) {
        this.name = name;
        return this;

    }

    @Override
    public BuilderHuman setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public BuilderHuman setHeight(float height) {
        this.height = height;
        return this;
    }

    @Override
    public BuilderHuman setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
        return this;
    }

    @Override
    public Human getHuman() {
        return new Human(name, age, height, birthday);
    }






}
