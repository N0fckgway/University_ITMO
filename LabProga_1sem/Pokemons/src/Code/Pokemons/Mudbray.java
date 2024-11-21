package Code.Pokemons;

import Code.Moves.PhysicalMove.RockTomb;
import Code.Moves.PhysicalMove.Stomp;
import Code.Moves.SpecialMove.MudSlap;
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
