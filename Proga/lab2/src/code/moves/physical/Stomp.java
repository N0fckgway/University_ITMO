package code.moves.physical;

import ru.ifmo.se.pokemon.Effect;
import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

import java.lang.Math;

/*
 * Stomp наносит урон и с вероятностью 30% заставляет цель вздрогнуть (если цель еще не двигалась).
 */


public final class Stomp extends PhysicalMove {
    public Stomp() {
        super(Type.NORMAL, 65, 100);
    }

    private boolean underStomp = false;

    @Override
    protected void applyOppEffects(Pokemon def) {
        if (Math.random() <= 0.3) {
            Effect.flinch(def);
            underStomp = true;
        }

    }

    @Override
    protected String describe() {
        if (underStomp) {
            return "Застанил";
        }
        return "Дефолтная атака";
    }
}
