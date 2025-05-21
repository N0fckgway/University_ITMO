package ru.suvorov.server.builder;


import ru.suvorov.enums.Climate;
import ru.suvorov.enums.Government;
import ru.suvorov.enums.StandardOfLiving;
import ru.suvorov.model.City;
import ru.suvorov.model.Coordinates;
import ru.suvorov.model.Human;

public interface BuilderCity {
    BuilderCity setId();
    BuilderCity setName(String name);
    BuilderCity setCoordinates(Coordinates coordinates);
    BuilderCity setCreationDate();
    BuilderCity setArea(Integer area);
    BuilderCity setPopulation(long population);
    BuilderCity setMetersAboveSeaLevel(double metersAboveSeaLevel);
    BuilderCity setClimate(Climate climate);
    BuilderCity setGovernment(Government government);
    BuilderCity setStandardOfLiving(StandardOfLiving standardOfLiving);
    BuilderCity setGovernor(Human governor);
    City getResult();

}
