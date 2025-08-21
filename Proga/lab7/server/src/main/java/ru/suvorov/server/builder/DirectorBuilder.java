package ru.suvorov.server.builder;



import com.github.javafaker.Faker;
import ru.suvorov.enums.Climate;
import ru.suvorov.enums.Government;
import ru.suvorov.enums.StandardOfLiving;
import ru.suvorov.model.Coordinates;
import ru.suvorov.model.Human;
import ru.suvorov.util.CollectionElement;


import java.time.ZonedDateTime;
import java.util.UUID;

public final class DirectorBuilder {
    public DirectorBuilder() {

    }

    public static CollectionElement constructCityBuilder(ConcreteCityBuilder builderCity) {
        Faker faker = new Faker();
        builderCity.setId();
        builderCity.setName(faker.address().cityName() + UUID.randomUUID());
        builderCity.setCoordinates(new Coordinates(faker.number().numberBetween(1, 10000), (long) faker.number().numberBetween(-582, 100000)));
        builderCity.setCreationDate();
        builderCity.setArea(faker.number().numberBetween(1, 100000));
        builderCity.setPopulation(faker.number().numberBetween(1, 10000000));
        builderCity.setMetersAboveSeaLevel(faker.number().randomDouble(2, 0, 9000));
        builderCity.setClimate(Climate.MONSOON);
        builderCity.setGovernment(Government.PLUTOCRACY);
        builderCity.setStandardOfLiving(StandardOfLiving.LOW);
        builderCity.setGovernor(new Human("Станислав", 19, 172, ZonedDateTime.now()));
        CollectionElement result = builderCity.getResult();
        result.validate();
        return result;
    }

    public static void constructHumanBuilder(ConcreateHumanBuilder builderHuman) {
        builderHuman.setName("Станислав");
        builderHuman.setAge(18);
        builderHuman.setHeight(173);
        builderHuman.setBirthday(ZonedDateTime.now());
        builderHuman.getHuman().validate();
        builderHuman.getHuman();
    }

    public static void constructCoordinatesBuilder(ConcreateCoordinatesBuilder builderCoordinates) {
        builderCoordinates.setX(182);
        builderCoordinates.setY(235L);
        builderCoordinates.getCoordinates().validate();
        builderCoordinates.getCoordinates();
    }



}
