package pokemons;

import moves.special.FocusBlast;

public class Watchog extends Patrat {
    public Watchog(String name, int level) {
        super(name, level);
        setStats(60,85,69,60,69,77);
        addMove(new FocusBlast());
    }
}
