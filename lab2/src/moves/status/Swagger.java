package moves.status;

import ru.ifmo.se.pokemon.*;

public class Swagger extends SpecialMove {
    public Swagger() {
        super(Type.NORMAL,0,100);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.ATTACK, 2);
        p.confuse();
    }

    @Override
    public String describe() {
        return "Cringger";
    }
}
