package ru.suvorov.server.builder;

import ru.suvorov.server.collection.enums.Climate;
import ru.suvorov.server.collection.enums.Government;
import ru.suvorov.server.collection.enums.StandardOfLiving;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.collection.model.Coordinates;
import ru.suvorov.server.collection.model.Human;
import ru.suvorov.server.util.CollectionElement;

import java.time.ZonedDateTime;

public final class DirectorBuilder {
    public DirectorBuilder() {

    }

    public static CollectionElement constructCityBuilder(ConcreteCityBuilder builderCity) {
        builderCity.setId();
        builderCity.setName("C");
        builderCity.setCoordinates(new Coordinates(123, 123L));
        builderCity.setCreationDate();
        builderCity.setArea(10000);
        builderCity.setPopulation(56780000);
        builderCity.setMetersAboveSeaLevel(12340);
        builderCity.setClimate(Climate.MONSOON);
        builderCity.setGovernment(Government.PLUTOCRACY);
        builderCity.setStandardOfLiving(StandardOfLiving.LOW);
        builderCity.setGovernor(new Human("Станислав", 19, 172, ZonedDateTime.now()));
        City result = builderCity.getResult();
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
