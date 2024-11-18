package Code.Moves;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;

public class ChargeBeam extends SpecialMove {
    public ChargeBeam(){
        super(Type.FAIRY, 32, 35);

    }

    protected String describe(){
        return "Дал лучом по ебалу";
    }
}
