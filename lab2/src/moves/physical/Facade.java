package moves.physical;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade(){
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def){
        this.power *= 2;
        double baseDamage2 = super.calcBaseDamage(att, def);
        this.power /= 2;
        return baseDamage2;
    }

    @Override
    protected String describe(){
        return "бьет лопатой по затылку";
    }
}
