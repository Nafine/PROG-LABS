package pokemons;

import moves.physical.Facade;
import moves.physical.FoulPlay;
import moves.special.ShadowBall;
import moves.status.Smokescreen;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Spiritomb extends Pokemon {
    public Spiritomb(String name, int level) {
        super(name, level);
        setType(Type.GHOST, Type.DARK);
        setStats(50, 92.0, 108,92,108, 35);
        setMove(new Smokescreen(), new Facade(), new FoulPlay(), new ShadowBall());
    }
}
