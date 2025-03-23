package ru.suvorov.common.util;


import lombok.*;

import java.time.ZonedDateTime;

@Data
public abstract class CollectionElement implements Comparable<CollectionElement>, Validtable {
    private long id;
    abstract public Long setId();
    abstract public ZonedDateTime setDate();
}

