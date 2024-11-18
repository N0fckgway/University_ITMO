package Code.Moves;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class WorkUp extends PhysicalMove {
    public WorkUp(){
        super(Type.FIGHTING, 10, 19);

    }

    protected String describe(){
        return "Жоска поработал";
    }
}
