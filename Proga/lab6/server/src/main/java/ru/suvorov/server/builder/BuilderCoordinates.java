package ru.suvorov.server.builder;


import ru.suvorov.model.Coordinates;

public interface BuilderCoordinates {
    BuilderCoordinates setX(long x);
    BuilderCoordinates setY(Long y);
    Coordinates getCoordinates();
}
