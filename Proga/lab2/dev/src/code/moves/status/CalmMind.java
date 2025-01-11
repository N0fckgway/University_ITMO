package code.moves.status;

import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Stat;

/*
 * Calm Mind повышает специальную атаку и специальную защиту пользователя на одну ступень каждую.
 * Максимально можно повысить статы на +6 ступеней каждую.
 */


public final class CalmMind extends StatusMove {
    public CalmMind() {
        super(Type.PSYCHIC, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon att) {
        att.setMod(Stat.SPECIAL_ATTACK, 1);
        att.setMod(Stat.SPECIAL_DEFENSE, 1);
    }

    @Override
    protected String describe() {
        return "Заюзал мув CalmMind";
    }

}

