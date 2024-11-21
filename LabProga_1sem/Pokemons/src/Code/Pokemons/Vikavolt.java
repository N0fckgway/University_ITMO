package Code.Pokemons;

import Code.Moves.PhysicalMove.PoisonJab;
import Code.Moves.PhysicalMove.XScissor;
import Code.Moves.StatusMove.Confide;
import Code.Moves.StatusMove.IronDefense;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Vikavolt extends Pokemon {
    public Vikavolt(String name, int level) {
        super(name, level);
        setType(Type.BUG, Type.ELECTRIC);
        setStats(77, 70, 90, 145, 75, 43);
        setMove(new XScissor(), new Confide(), new IronDefense(), new PoisonJab());
    }
}
