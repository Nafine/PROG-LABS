package ru.itmo.persons;

import ru.itmo.enums.Gender;
import ru.itmo.enums.LifeQuality;
import ru.itmo.exceptions.NoServantsHomeException;
import ru.itmo.interfaces.Servant;
import ru.itmo.world.Place;

import java.util.Objects;

/*
завел себе слуг, которые одевали его и раздевали, убирали у него в комнатах, смотрели за домом.
 */
public class Servants extends Person implements Servant {
    String masterName;
    ServeQuality serveQuality;
    public enum ServeQuality {
        EXTRA_BAD,
        BAD,
        GOOD,
        EXTRA_GOOD
    }

    public Servants() {
        super("Слуги", Gender.PLURAL, LifeQuality.BAD, new Place("улица"));
        serveQuality = ServeQuality.GOOD;
    }

    @Override
    public String getName(){
        return name + " мастера с прозвищем " + masterName + ".";
    }

    ///
    /// Let servants relax after each move
    ///
    protected void relax(){
        if (Math.random() <= 0.5 && serveQuality != ServeQuality.EXTRA_BAD) {
            serveQuality = ServeQuality.values()[serveQuality.ordinal() - 1];
            System.out.println("Щеглы совсем расслабились после работы и, вероятно, их стоит проучить!");
        }
    }

    @Override
    public void lookAfterHome() throws NoServantsHomeException{
        if(home.name().equals("улица")) throw new NoServantsHomeException(this);
        System.out.println(name + switch (serveQuality) {
            case EXTRA_GOOD -> " смотрели за домом так, что и муха не могла рядом пролететь!";
            case GOOD -> " смотрели за " + home.name() + ".";
            case BAD -> " кое-как присматривали за домом хозяина.";
            case EXTRA_BAD -> " совсем не смотрели за домом!";
        });
        relax();
    }

    @Override
    public void cleanHome() throws NoServantsHomeException{
        if(home.name().equals("улица")) throw new NoServantsHomeException(this);
        System.out.println(name + switch (serveQuality) {
            case EXTRA_GOOD -> " вылизали дом дочиста!";
            case GOOD -> " убрались в комнатах мастера " + home.name();
            case BAD -> " еле шевелясь вымыли полы.";
            case EXTRA_BAD -> " отказались убираться дома!";
        });
        relax();
    }

    @Override
    public void serveMaster() throws NoServantsHomeException {
        if(home.name().equals("улица")) throw new NoServantsHomeException(this);
        System.out.println(switch (serveQuality) {
            case EXTRA_GOOD -> "Они одевали, обхаживали и радовали своего хозяина всеми мыслимыми способами!";
            case GOOD -> "Они одевали его и раздевали.";
            case BAD -> "Они неспешно, нехотя работали.";
            case EXTRA_BAD -> "Они периодически избивали его";
        });
        relax();
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), serveQuality);
    }

    @Override
    public String toString(){
        return super.toString() +
                "[serveQuality=" + serveQuality +
                "]";
    }
}
