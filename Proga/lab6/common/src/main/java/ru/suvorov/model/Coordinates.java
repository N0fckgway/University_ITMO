package ru.suvorov.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import ru.suvorov.exception.InvalidDataException;
import ru.suvorov.util.Validtable;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Coordinates implements Validtable, Serializable {
    private static final long serialVersionUID = 1L;
    private long x;
    private Long y; //Значение поля должно быть больше -583, Поле не может быть null

    public Coordinates(long x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void validate() throws InvalidDataException {
        if (y == null) {
            throw new InvalidDataException(this, "Координата y не может быть null");
        }
        if (y <= -583) {
            throw new InvalidDataException(this, "Координата y должна быть больше -583");
        }
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
