package code.pokemons;

import code.moves.physical.XScissor;
import code.moves.status.Confide;
import code.moves.status.IronDefense;
import ru.ifmo.se.pokemon.Type;

public class Charjabug extends Grubbin {
    public Charjabug(String name, int level) {
        super(name, level);
        addType(Type.ELECTRIC);
        setStats(57, 82, 95, 55, 75, 36);
        addMove(new IronDefense());
    }
}
