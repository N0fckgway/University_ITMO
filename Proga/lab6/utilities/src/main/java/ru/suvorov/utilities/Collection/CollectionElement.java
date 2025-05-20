package ru.suvorov.server.Collection;




import ru.suvorov.utilities.util.Validtable;

import java.time.ZonedDateTime;



public abstract class CollectionElement implements Comparable<CollectionElement>, Validtable {
    public long id;
    abstract public Long setId();
    abstract public ZonedDateTime setDate();
    abstract public Long getId();
}

