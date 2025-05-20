package builder;

import ru.suvorov.utilities.collection.enums.Climate;
import ru.suvorov.utilities.collection.enums.Government;
import ru.suvorov.utilities.collection.enums.StandardOfLiving;
import ru.suvorov.utilities.collection.model.City;
import ru.suvorov.utilities.collection.model.Coordinates;
import ru.suvorov.utilities.collection.model.Human;

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
