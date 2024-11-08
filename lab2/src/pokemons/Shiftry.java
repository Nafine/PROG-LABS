package pokemons;

import moves.status.NastyPlot;
import ru.ifmo.se.pokemon.Type;

public class Shiftry extends Nuzleaf{
    public Shiftry(String name, int level) {
        super(name, level);
        setStats(90,100,60,90,60,80);
        addMove(new NastyPlot());
    }
}
