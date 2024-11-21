package Code.Pokemons;

import Code.Moves.PhysicalMove.XScissor;
import Code.Moves.StatusMove.Confide;
import Code.Moves.StatusMove.IronDefense;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Charjabug extends Pokemon {
    public Charjabug(String name, int level) {
        super(name, level);
        setType(Type.BUG, Type.ELECTRIC);
        setStats(57, 82, 95, 55, 75, 36);
        setMove(new XScissor(), new Confide(), new IronDefense());
    }
}
