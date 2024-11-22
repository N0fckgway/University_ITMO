package code.pokemons;

import code.moves.special.ChargeBeam;
import code.moves.status.CalmMind;
import code.moves.status.ThunderWave;
import code.moves.status.WorkUp;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Stantler extends Pokemon {
    public Stantler(String name, int level) {
        super(name, level);
        setStats(73, 95, 62, 85, 65, 85);
        setType(Type.NORMAL);
        setMove(new ThunderWave(), new WorkUp(), new ChargeBeam(), new CalmMind());
    }


}
