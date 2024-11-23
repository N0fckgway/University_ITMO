package code.moves.physical;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.PhysicalMove;

import java.lang.Math;

/*
 * Poison Jab наносит урон и с вероятностью 30% отравляет цель.
 */


public final class PoisonJab extends PhysicalMove {
    public PoisonJab() {
        super(Type.POISON, 80, 100);

    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.3) {
            Effect.poison(def);
        }
    }

    @Override
    protected String describe() {
        return "нахимичил ядком";
    }
}
