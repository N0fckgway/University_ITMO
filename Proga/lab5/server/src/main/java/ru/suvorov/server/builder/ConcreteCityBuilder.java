package ru.suvorov.server.builder;

import java.time.ZonedDateTime;


import lombok.Getter;
import ru.suvorov.server.collection.enums.Climate;
import ru.suvorov.server.collection.enums.Government;
import ru.suvorov.server.collection.enums.StandardOfLiving;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.collection.model.Coordinates;
import ru.suvorov.server.collection.model.Human;


@Getter
public class ConcreteCityBuilder implements BuilderCity {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer area; //Значение поля должно быть больше 0, Поле не может быть null
    private long population; //Значение поля должно быть больше 0
    private double metersAboveSeaLevel;
    private Climate climate; //Поле может быть null
    private Government government; //Поле может быть null
    private StandardOfLiving standardOfLiving; //Поле может быть null
    private Human governor; //Поле может быть null

    @Override
    public BuilderCity setId() {
        City city = getResult();
        this.id = city.setId();
        return this;

    }

    @Override
    public BuilderCity setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public BuilderCity setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;

    }

    @Override
    public BuilderCity setCreationDate() {
        City city = getResult();
        this.creationDate = city.setDate();
        return this;

    }

    @Override
    public BuilderCity setArea(Integer area) {
        this.area = area;
        return this;

    }

    @Override
    public BuilderCity setPopulation(long population) {
        this.population = population;
        return this;

    }

    @Override
    public BuilderCity setMetersAboveSeaLevel(double metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        return this;

    }


    @Override
    public BuilderCity setClimate(Climate climate) {
        this.climate = climate;
        return this;


    }


    @Override
    public BuilderCity setGovernment(Government government) {
        this.government = government;
        return this;


    }

    @Override
    public BuilderCity setStandardOfLiving(StandardOfLiving standardOfLiving) {
        this.standardOfLiving = standardOfLiving;
        return this;

    }

    @Override
    public BuilderCity setGovernor(Human governor) {
        this.governor = governor;
        return this;


    }

    @Override
    public City getResult() {
        return new City(id, name, coordinates, creationDate, area, population,
                metersAboveSeaLevel, climate, government, standardOfLiving, governor);
    }



}
