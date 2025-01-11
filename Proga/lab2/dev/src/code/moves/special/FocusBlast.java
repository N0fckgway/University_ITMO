package code.moves.special;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

import java.lang.Math;

/*
 * Focus Blast наносит урон и с вероятностью 10% снижает специальную защиту цели на одну ступень.
 * Статы могут быть снижены минимум на -6 ступеней каждый.
 */


public final class FocusBlast extends SpecialMove {
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
