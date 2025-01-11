package code.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;

/*
 * Rock Tomb наносит урон и снижает скорость цели на одну ступень.
 * Статы могут быть снижены минимум на -6 стадий каждый.
 */


public final class RockTomb extends PhysicalMove {
    public RockTomb() {
        super(Type.ROCK, 60, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        def.setMod(Stat.SPEED, -1);
    }


    @Override
    protected String describe() {
        return "От рок томбил";
    }
}
