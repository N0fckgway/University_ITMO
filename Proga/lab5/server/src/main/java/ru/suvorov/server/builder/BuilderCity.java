package ru.suvorov.server.builder;

import ru.suvorov.server.collection.enums.Climate;
import ru.suvorov.server.collection.enums.Government;
import ru.suvorov.server.collection.enums.StandardOfLiving;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.collection.model.Coordinates;
import ru.suvorov.server.collection.model.Human;

public interface BuilderCity {
    BuilderCity setId();
    BuilderCity setName(String name);
    BuilderCity setCoordinates(Coordinates coordinates);
    BuilderCity setCreationDate(java.time.ZonedDateTime creationDate);
    BuilderCity setArea(Integer area);
    BuilderCity setPopulation(long population);
    BuilderCity setMetersAboveSeaLevel(double metersAboveSeaLevel);
    BuilderCity setClimate(Climate climate);
    BuilderCity setGovernment(Government government);
    BuilderCity setStandardOfLiving(StandardOfLiving standardOfLiving);
    BuilderCity setGovernor(Human governor);
    City getResult();

}
