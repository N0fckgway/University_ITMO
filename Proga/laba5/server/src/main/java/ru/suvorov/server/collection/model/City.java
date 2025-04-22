package ru.suvorov.server.collection.model;


import com.github.javafaker.Faker;
import ru.suvorov.server.exception.InvalidDataException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.suvorov.server.util.CollectionElement;
import ru.suvorov.server.util.Validtable;
import ru.suvorov.server.collection.enums.Climate;
import ru.suvorov.server.collection.enums.Government;
import ru.suvorov.server.collection.enums.StandardOfLiving;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@ToString
@Setter
public class City extends CollectionElement implements Validtable, Serializable {
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

    public City(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer area, long population, double metersAboveSeaLevel, Climate climate, Government government, StandardOfLiving standardOfLiving, Human governor) {
        this.id = setId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = setDate();
        this.area = area;
        this.population = population;
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.climate = climate;
        this.government = government;
        this.standardOfLiving = standardOfLiving;
        this.governor = governor;

    }

    public City() {

    }

    public Human getGovernor() {
        return governor;
    }

    public double getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    @Override
    public void validate() throws InvalidDataException {
        if (setId() <= 0) {
            throw new InvalidDataException(this, "Invalid ID");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException(this, "Invalid Name");
        }
        if (coordinates == null) {
            throw new InvalidDataException(this, "Invalid Coordinates");
        }
        if (creationDate == null) {
            throw new InvalidDataException(this, "Invalid Date");
        }
        if (area == null || area <= 0) {
            throw new InvalidDataException(this, "Invalid Area");
        }
        if (population <= 0) {
            throw new InvalidDataException(this, "Invalid Population");
        }


    }

    @Override
    public Long setId() {
        UUID newID = UUID.randomUUID();
        this.id = newID.getMostSignificantBits() & Long.MAX_VALUE;
        return id;
    }

    @Override
    public ZonedDateTime setDate() {
        Faker faker = new Faker();
        Date randomDate = faker.date().birthday(0, 100);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(randomDate.toInstant(), ZoneId.systemDefault());

        if (dateTime.isAfter(ZonedDateTime.now())) {
            throw new InvalidDataException(this, "Дата создания города не может быть в будущем\n");
        }
        return dateTime;
    }

    /**
     * @return
     */
    @Override
    public Long getId() {
        return id;
    }


    @Override
    public int compareTo(CollectionElement element) {
        return Long.compare(this.id, element.getId());
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