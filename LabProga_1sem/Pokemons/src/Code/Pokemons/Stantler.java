package Code.Pokemons;

import Code.Moves.*;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Stantler extends Pokemon {
    public Stantler(String name, int level) {
        super(name, level);
        setStats(19, 97, 66, 24, 69, 8);
        setType(Type.FIGHTING);
        setMove(new ThunderWave(), new WorkUp(), new ChargeBeam(), new CalmMind());
    }



}
