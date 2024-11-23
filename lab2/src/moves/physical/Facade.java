package moves.physical;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade(){
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def){
        power *= 2;
        double baseDamage = super.calcBaseDamage(att, def);
        power /= 2;
        return baseDamage;
    }

    @Override
    protected String describe(){
        return "бьет лопатой по затылку";
    }
}
