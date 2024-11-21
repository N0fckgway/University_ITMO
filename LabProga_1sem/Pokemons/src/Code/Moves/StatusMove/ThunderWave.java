package Code.Moves.StatusMove;

import ru.ifmo.se.pokemon.*;

public class ThunderWave extends StatusMove {
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
