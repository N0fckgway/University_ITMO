package ru.suvorov.util;

import java.time.ZonedDateTime;

public abstract class CollectionElement implements Validtable {

    abstract public Long setId();
    abstract public ZonedDateTime setDate();
    abstract public Long getId();

}

