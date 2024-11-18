package Code.Moves;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;


public class CalmMind extends StatusMove {
    public CalmMind() {
        super(Type.GRASS, 11, 82);

    }
    protected String describe(){
        return "дал покурить траву";

    }

}

