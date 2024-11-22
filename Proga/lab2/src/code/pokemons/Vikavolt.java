package code.pokemons;

import code.moves.physical.PoisonJab;
import code.moves.physical.XScissor;
import code.moves.status.Confide;
import code.moves.status.IronDefense;
import ru.ifmo.se.pokemon.Type;

public final class Vikavolt extends Charjabug {
    public Vikavolt(String name, int level) {
        super(name, level);
        setType(Type.BUG, Type.ELECTRIC);
        setStats(77, 70, 90, 145, 75, 43);
        setMove(new XScissor(), new Confide(), new IronDefense(), new PoisonJab());
    }
}
