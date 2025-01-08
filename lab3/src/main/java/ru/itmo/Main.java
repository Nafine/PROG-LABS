package ru.itmo;

import ru.itmo.exceptions.NoServantsException;
import ru.itmo.exceptions.NoServantsHomeException;
import ru.itmo.persons.Ponchik;
import ru.itmo.persons.Servants;
import ru.itmo.world.Place;

public class Main {
    public static void main(String[] args) {
        Ponchik ponchik = new Ponchik("Пончик", RandomStart.randGender(), RandomStart.randLifeQuality(),
                new Place("гостиница"));
        Servants servants = new Servants();

        ponchik.live();
        ponchik.named();
        ponchik.relocate(new Place("собственный дом"));
        ponchik.ownServants(servants);
        try{
            servants.serveMaster();
            servants.cleanHome();
            if(ponchik.checkServeQuality() == Servants.ServeQuality.BAD
                    || ponchik.checkServeQuality() == Servants.ServeQuality.EXTRA_BAD) {
                ponchik.punishServants();
            }
            servants.lookAfterHome();
        }
        catch (NoServantsException | NoServantsHomeException e) {
            System.out.println(e.getMessage());
        }
        ponchik.wasteTime(new Place("берег залива"), new Place("ИТМО"));
        ponchik.eat(new Place("рестораны"), new Place("KFC"));
        ponchik.haveFun(new Place("чертово колесо"), new Place("морской параболоид"));
    }
}