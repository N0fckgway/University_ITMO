package Code.Pokemons;

import Code.Moves.PhysicalMove.RockTomb;
import Code.Moves.PhysicalMove.Stomp;
import Code.Moves.SpecialMove.FocusBlast;
import Code.Moves.SpecialMove.MudSlap;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Mudsdale extends Pokemon {
    public Mudsdale(String name, int level) {
        super(name, level);
        setType(Type.GROUND);
        setStats(100, 125, 100, 55, 85, 35);
        setMove(new MudSlap(), new Stomp(), new RockTomb(), new FocusBlast());

    }
}
