package ru.itmo.persons;

import ru.itmo.enums.Gender;
import ru.itmo.enums.LifeQuality;
import ru.itmo.enums.Mood;
import ru.itmo.exceptions.NoServantsException;
import ru.itmo.interfaces.CanEnjoy;
import ru.itmo.interfaces.CanOwnServants;
import ru.itmo.interfaces.CanWasteTime;
import ru.itmo.interfaces.Character;
import ru.itmo.world.Place;

import java.util.Objects;

public final class Ponchik extends Person implements Character, CanOwnServants, CanWasteTime, CanEnjoy {
    private final String nickname;
    private Servants servants;

    public Ponchik(String name, Gender gender, LifeQuality lifeQuality, Place home) {
        super(name, gender, lifeQuality, home);
        this.nickname = switch (lifeQuality) {
            case LifeQuality.EXTRA_GOOD -> "Господин Понч";
            case LifeQuality.GOOD -> "Понч";
            case LifeQuality.BAD -> "водолаз";
            case LifeQuality.EXTRA_BAD -> "\"эу, нефор!\"";
        };
    }

    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public void haveFun(Place place, Place... otherPlaces) {
        String sentence = name + " повеселился в таких местах, как: " + place.name();
        boolean hadFun = false;

        for (Place p : otherPlaces) {
            sentence += ", " + p.name();
            if (mood != Mood.EXTRA_GOOD) {
                mood = Mood.values()[mood.ordinal() + 1];
                hadFun = true;
            }
        }

        if (hadFun) sentence += ", и поднял себе настроение!";
        else sentence += ".";
        System.out.println(sentence);
    }

    @Override
    public void eat(Place place, Place... otherPlaces) {
        String sentence = name + " вкусно покушал в: " + place.name();
        boolean hadFun = false;


        for (Place p : otherPlaces) {
            sentence += ", " + p.name();
            if (mood != Mood.EXTRA_GOOD) {
                mood = Mood.values()[mood.ordinal() + 1];
                hadFun = true;
            }
        }

        if (hadFun) sentence += ", и поднял себе настроение!";
        else sentence += ".";
        System.out.println(sentence);
    }

    @Override
    public void wasteTime(Place place, Place... otherPlaces) {
        String sentence = name + " от нечего делать целыми днями просиживал в " + place.name();
        boolean hadFun = false;
        for (Place p : otherPlaces)
            sentence += ", " + p.name();

        sentence += ".";
        System.out.println(sentence);
    }

    @Override
    public void named() {
        System.out.println("И назывался он теперь не " + name + ", а " + nickname + ".");
    }

    @Override
    public void live() {
        System.out.println("Жил " + name + switch (lifeQuality) {
            case EXTRA_GOOD -> " в полное свое удовольствие.";
            case GOOD -> " вообщем то, неплохо.";
            case BAD -> " почти без друзей, многие дети мигрантов его избивали после школы.";
            case EXTRA_BAD -> " ужасно и совсем без друзей, дети мигрантов его постоянно избивали.";
        });
    }

    @Override
    public void relocate(Place to) {
        String prevHome = home.name();
        home = to;
        System.out.println(name + " из " + prevHome + " переселился в " + home.name() + ".");
    }

    @Override
    public void ownServants(Servants servants) {
        this.servants = servants;
        servants.home = home;
        servants.masterName = nickname;
        System.out.println("Завел себе слуг.");
    }

    @Override
    public void punishServants() throws NoServantsException {
        if (servants == null) throw new NoServantsException(this);
        if (servants.serveQuality != Servants.ServeQuality.EXTRA_GOOD) {
            servants.serveQuality = Servants.ServeQuality.values()[servants.serveQuality.ordinal() + 1];
            System.out.println("Щеглы были наказаны! Теперь они будут работать лучше!");
        } else {
            System.out.println("Вы просто так наказали щеглов! Но ничего, будут знать!");
        }
    }

    @Override
    public void fireServants() throws NoServantsException {
        if (Objects.isNull(servants)) throw new NoServantsException(this);
        servants.home = new Place("улица");
        servants.masterName = null;
        servants = null;
    }

    @Override
    public Servants.ServeQuality checkServeQuality() throws NoServantsException {
        if (Objects.isNull(servants)) throw new NoServantsException(this);
        return servants.serveQuality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), servants, nickname);
    }

    @Override
    public String toString() {
        return super.toString() +
                "[servants=" + servants +
                ", titulus=" + "\"" + nickname + "\"" +
                "]";
    }
}
