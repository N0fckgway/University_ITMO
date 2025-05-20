package builder;

import lombok.Getter;
import ru.suvorov.utilities.collection.model.Coordinates;

@Getter
public class ConcreateCoordinatesBuilder implements BuilderCoordinates{
    private long x;
    private Long y; //Значение поля должно быть больше -583, Поле не может быть null

    @Override
    public BuilderCoordinates setX(long x) {
        this.x = x;
        return this;
    }

    @Override
    public BuilderCoordinates setY(Long y) {
        this.y = y;
        return this;
    }

    @Override
    public Coordinates getCoordinates() {
        return new Coordinates(x, y);
    }

}
