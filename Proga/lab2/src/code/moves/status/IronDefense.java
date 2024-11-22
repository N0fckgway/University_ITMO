package code.moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

public final class IronDefense extends StatusMove {
    public IronDefense() {
        super(Type.STEEL, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon att) {
        att.setMod(Stat.DEFENSE, 2);
    }

    @Override
    protected String describe() {
        return "надел железную броню";
    }
}
