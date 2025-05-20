package builder;

import ru.suvorov.utilities.collection.model.Coordinates;

public interface BuilderCoordinates {
    BuilderCoordinates setX(long x);
    BuilderCoordinates setY(Long y);
    Coordinates getCoordinates();
}
