package Code.Pokemons;

import Code.Moves.PhysicalMove.XScissor;
import Code.Moves.StatusMove.CalmMind;
import Code.Moves.StatusMove.Confide;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Grubbin extends Pokemon {
    public Grubbin(String name, int level) {
        super(name, level);
        setType(Type.BUG);
        setStats(47, 62, 45, 55, 45, 46);
        setMove(new XScissor(), new Confide());
    }
}
