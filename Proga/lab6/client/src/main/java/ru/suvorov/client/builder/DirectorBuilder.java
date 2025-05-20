package builder;


import ru.suvorov.server.Collection.CollectionElement;
import ru.suvorov.utilities.collection.enums.Climate;
import ru.suvorov.utilities.collection.enums.Government;
import ru.suvorov.utilities.collection.enums.StandardOfLiving;
import ru.suvorov.utilities.collection.model.Coordinates;
import ru.suvorov.utilities.collection.model.Human;
import ru.suvorov.server.Collection.CollectionElement;

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
