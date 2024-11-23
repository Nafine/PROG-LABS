package moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

public class FoulPlay extends PhysicalMove {
    public FoulPlay() {
        super(Type.DARK, 95, 100);
    }

    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def){
        double tempAttack = att.getStat(Stat.ATTACK);
        double tempSpecAttack = att.getStat(Stat.SPECIAL_ATTACK);

        att.setStats(att.getStat(Stat.HP), def.getStat(Stat.ATTACK), att.getStat(Stat.DEFENSE),
                     def.getStat(Stat.SPECIAL_ATTACK), att.getStat(Stat.SPECIAL_DEFENSE), att.getStat(Stat.SPEED));

        double baseDamage = super.calcBaseDamage(att, def);

        att.setStats(att.getStat(Stat.HP), tempAttack, att.getStat(Stat.DEFENSE),
                tempSpecAttack, att.getStat(Stat.SPECIAL_DEFENSE), att.getStat(Stat.SPEED));

        return baseDamage;
    }

    @Override
    protected String describe() {
        return "жестоко обманул(пока неизвестно кого)";
    }
}
