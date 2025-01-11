package code.moves.status;

import ru.ifmo.se.pokemon.*;

/*
 * Волна грома парализует противника.
 * Парализованные покемоны с вероятностью 25% не могут атаковать, а их скорость снижается на 50%.
 */

public final class ThunderWave extends StatusMove {


    public ThunderWave() {
        super(Type.ELECTRIC, 0, 90);
    }

    @Override
    protected void applyOppEffects(Pokemon def) {
        Effect.paralyze(def);
    }

    @Override
    protected String describe() {
        return "Заюзал мув ThunderWave";
    }

}
