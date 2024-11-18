package Code.Moves;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class ThunderWave extends SpecialMove {
    public ThunderWave() {
        super(Type.NORMAL, 7, 19);


    }

    protected String describe() {
        return "Ебнул грозой";
    }

}
