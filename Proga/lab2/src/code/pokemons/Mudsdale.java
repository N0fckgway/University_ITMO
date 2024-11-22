package code.pokemons;

import code.moves.physical.RockTomb;
import code.moves.physical.Stomp;
import code.moves.special.FocusBlast;
import code.moves.special.MudSlap;
import ru.ifmo.se.pokemon.Type;

public final class Mudsdale extends Mudbray {
    public Mudsdale(String name, int level) {
        super(name, level);
        setType(Type.GROUND);
        setStats(100, 125, 100, 55, 85, 35);
        setMove(new MudSlap(), new Stomp(), new RockTomb(), new FocusBlast());

    }
}
