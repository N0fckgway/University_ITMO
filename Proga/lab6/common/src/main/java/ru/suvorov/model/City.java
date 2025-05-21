package ru.suvorov.model;

import com.github.javafaker.Faker;
import lombok.Getter;
import ru.suvorov.enums.Climate;
import ru.suvorov.enums.Government;
import ru.suvorov.enums.StandardOfLiving;
import ru.suvorov.exception.InvalidDataException;
import lombok.Setter;
import lombok.ToString;
import ru.suvorov.util.CollectionElement;
import ru.suvorov.util.Validtable;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@Setter
public class City extends CollectionElement implements Validtable, Serializable, Comparable<City> {
    private static final long serialVersionUID = 1L;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля генерируется автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля генерируется автоматически
    private Integer area; //Значение поля должно быть больше 0
    private long population; //Значение поля должно быть больше 0
    private double metersAboveSeaLevel;
    private boolean capital;
    private Climate climate; //Поле может быть null
    private Government government; //Поле не может быть null
    private StandardOfLiving standardOfLiving; //Поле не может быть null
    private Human governor; //Поле не может быть null

    public City(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer area, long population, double metersAboveSeaLevel, Climate climate, Government government, StandardOfLiving standardOfLiving, Human governor) {
        this.id = generateId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = generateDate();
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.climate = climate;
        this.government = government;
        this.standardOfLiving = standardOfLiving;
        this.governor = governor;
    }

    public City() {
        this.id = generateId();
        this.creationDate = generateDate();
    }

    public Human getGovernor() {
        return governor;
    }

    public double getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    @Override
    public void validate() throws InvalidDataException {
        if (id == null || id <= 0) {
            throw new InvalidDataException(this, "ID должен быть больше 0");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException(this, "Имя не может быть пустым");
        }
        if (coordinates == null) {
            throw new InvalidDataException(this, "Координаты не могут быть null");
        }
        if (creationDate == null) {
            throw new InvalidDataException(this, "Дата создания не может быть null");
        }
        if (area == null || area <= 0) {
            throw new InvalidDataException(this, "Площадь должна быть больше 0");
        }
        if (population <= 0) {
            throw new InvalidDataException(this, "Население должно быть больше 0");
        }
        if (government == null) {
            throw new InvalidDataException(this, "Тип правительства не может быть null");
        }
        if (standardOfLiving == null) {
            throw new InvalidDataException(this, "Уровень жизни не может быть null");
        }
        if (governor == null) {
            throw new InvalidDataException(this, "Губернатор не может быть null");
        }
    }

    private Long generateId() {
        UUID newID = UUID.randomUUID();
        return newID.getMostSignificantBits() & Long.MAX_VALUE;
    }

    private ZonedDateTime generateDate() {
        Faker faker = new Faker();
        Date randomDate = faker.date().birthday(18, 100);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(randomDate.toInstant(), ZoneId.systemDefault());

        if (dateTime.isAfter(ZonedDateTime.now())) {
            throw new InvalidDataException(this, "Дата создания города не может быть в будущем");
        }
        return dateTime;
    }

    @Override
    public Long setId() {
        return generateId();
    }

    @Override
    public ZonedDateTime setDate() {
        return generateDate();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public int compareTo(City other) {
        return this.getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", population=" + population +
                ", metersAboveSeaLevel=" + metersAboveSeaLevel +
                ", climate=" + climate +
                ", government=" + government +
                ", standardOfLiving=" + standardOfLiving +
                ", governor=" + governor +
                '}';
    }
}