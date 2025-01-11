package code.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;

/*
 * Work Up повышает атаку и специальную атаку пользователя на одну ступень.
 * Статы могут быть повышены максимум на +6 ступеней каждый.
 */


public final class WorkUp extends StatusMove {
    public WorkUp() {
        super(Type.NORMAL, 0, 100);

    }

    @Override
    protected void applySelfEffects(Pokemon att) {
        att.setMod(Stat.SPECIAL_ATTACK, 1);
        att.setMod(Stat.ATTACK, 1);
    }

    @Override
    protected String describe() {
        return "Жоска поработал";
    }

}
