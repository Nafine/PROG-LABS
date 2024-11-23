import pokemons.*;
import ru.ifmo.se.pokemon.Battle;
import ru.ifmo.se.pokemon.Pokemon;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Pokemon p1 = new Spiritomb("Shlepa", 1);
        Pokemon p2 = new Watchog("Abunga", 1);
        Pokemon p3 = new Nuzleaf("Travyanoi", 1);
        Pokemon p4 = new Shiftry("Imba", 1);
        Pokemon p5 = new Seedot("Melkiy chudik", 2);
        Pokemon p6 = new Patrat("ZOV", 2);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p5);
        b.addFoe(p3);
        b.addFoe(p4);
        b.addFoe(p6);
        b.go();
    }
}
