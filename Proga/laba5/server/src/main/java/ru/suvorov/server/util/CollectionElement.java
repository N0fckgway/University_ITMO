package ru.suvorov.server.util;




import java.time.ZonedDateTime;



public abstract class CollectionElement implements Comparable<CollectionElement>, Validtable {
    public long id;
    abstract public Long setId();
    abstract public ZonedDateTime setDate();
    abstract public Long getId();
}

