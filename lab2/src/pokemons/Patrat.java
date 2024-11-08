package pokemons;

import moves.special.ShadowBall;
import moves.status.Confide;
import moves.status.Swagger;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Patrat extends Pokemon {
    public Patrat(String name, int level) {
        super(name, level);
        setType(Type.NORMAL);
        setStats(45, 55, 39,35,39,42);
        setMove(new Confide(), new ShadowBall(), new Swagger());
    }
}
