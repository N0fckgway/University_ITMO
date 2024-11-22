package code.pokemons;

import code.moves.physical.RockTomb;
import code.moves.physical.Stomp;
import code.moves.special.MudSlap;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Mudbray extends Pokemon {
    public Mudbray(String name, int level) {
        super(name, level);
        setType(Type.GROUND);
        setStats(70, 100, 70, 45, 55, 45);
        setMove(new MudSlap(), new Stomp(), new RockTomb());

    }

}
