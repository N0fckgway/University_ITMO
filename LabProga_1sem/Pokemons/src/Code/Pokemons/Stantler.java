package Code.Pokemons;

import Code.Moves.SpecialMove.ChargeBeam;
import Code.Moves.StatusMove.CalmMind;
import Code.Moves.StatusMove.ThunderWave;
import Code.Moves.StatusMove.WorkUp;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Stantler extends Pokemon {
    public Stantler(String name, int level) {
        super(name, level);
        setStats(73, 95, 62, 85, 65, 85);
        setType(Type.NORMAL);
        setMove(new ThunderWave(), new WorkUp(), new ChargeBeam(), new CalmMind());
    }


}
