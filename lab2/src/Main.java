import pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Spiritomb("Shlepa", 1);
        Pokemon p2 = new Watchog("Abunga", 1);
        Pokemon p3 = new Nuzleaf("Trava", 1);
        Pokemon p4 = new Shiftry("Hui", 1);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addFoe(p3);
        b.addFoe(p4);
        b.go();
    }
}
