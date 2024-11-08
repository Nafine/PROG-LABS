package moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class NastyPlot extends SpecialMove {
    public NastyPlot(){
        super(Type.DARK, 0, 100);
    }

    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.SPECIAL_ATTACK, 2);
    }

    @Override
    protected String describe(){
        return "банфул себя на +2 спешл атак";
    }
}
