package ru.suvorov.server.collection.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.suvorov.common.exception.InvalidDataException;
import ru.suvorov.common.util.Validtable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Coordinates implements Validtable {
    private long x;
    private Long y; //Значение поля должно быть больше -583, Поле не может быть null

    public Coordinates(long x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void validate() throws InvalidDataException {
        if (y == null || y > -583) {
            throw new InvalidDataException(this, "Invalid coordinate y or coordinate y not in set");
        }

    }

}
