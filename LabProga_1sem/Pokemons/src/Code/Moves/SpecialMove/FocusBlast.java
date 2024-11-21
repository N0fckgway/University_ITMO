package Code.Moves.SpecialMove;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

import java.lang.Math;

public class FocusBlast extends SpecialMove {
    public FocusBlast() {
        super(Type.FIGHTING, 120, 70);
    }

    private boolean underFocusBlast = false;

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.1) {
            def.setMod(Stat.SPECIAL_DEFENSE, -1);
            underFocusBlast = true;
        }

    }

    @Override
    protected String describe() {
        if (underFocusBlast) {
            return "Поймал Фокус";
        }
        return "дефолтный дамаг";
    }

}
