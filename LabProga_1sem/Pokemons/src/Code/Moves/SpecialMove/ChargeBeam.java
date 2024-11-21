package Code.Moves.SpecialMove;

import java.lang.Math;

import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;

public class ChargeBeam extends SpecialMove {
    public ChargeBeam() {
        super(Type.ELECTRIC, 50, 90);
    }

    private boolean underChargeBeam = false;

    @Override
    protected void applySelfEffects(Pokemon att) {
        if (Math.random() <= 0.7) {
            att.setMod(Stat.SPECIAL_ATTACK, 1);
            underChargeBeam = true;
        }
    }

    @Override
    protected String describe() {
        if (underChargeBeam) {
            return "Закинулся ChargeBeams";
        }
        return "Дефолтная атака";
    }
}
