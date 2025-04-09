package ru.suvorov.server.builder;

import ru.suvorov.server.collection.model.Coordinates;

public interface BuilderCoordinates {
    BuilderCoordinates setX(long x);
    BuilderCoordinates setY(Long y);
    Coordinates getCoordinates();
}
