package code.moves.special;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

/*
 * Грязевой шлепок наносит урон и снижает Точность цели на одну ступень.
 * Статы могут быть снижены минимум на -6 ступеней каждый.
 */


public final class MudSlap extends SpecialMove {
    public MudSlap() {
        super(Type.GROUND, 20, 100);

    }

    @Override
    protected void applySelfEffects(Pokemon att) {
        att.setMod(Stat.ACCURACY, -1);
    }

    @Override
    protected String describe() {
        return "Отмудслэпил";
    }

}
