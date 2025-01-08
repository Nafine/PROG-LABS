package ru.itmo.persons;

import ru.itmo.enums.Gender;
import ru.itmo.enums.LifeQuality;
import ru.itmo.enums.Mood;
import ru.itmo.world.Place;

import java.util.Objects;

public abstract class Person {
    protected final String name;
    protected final Gender gender;
    protected final LifeQuality lifeQuality;
    protected Place home;
    protected Mood mood;

    public Person(String name, Gender gender, LifeQuality lifeQuality, Place home) throws IllegalArgumentException {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("Вы должны задать имя персонажу!");
        }
        this.name = name;
        this.gender = gender;
        this.lifeQuality = lifeQuality;
        this.home = home;
        this.mood = Mood.values()[lifeQuality.ordinal()];
    }

    abstract public String getName();

    final public Gender getGender() {
        return gender;
    }

    final public LifeQuality getLifeQuality() {
        return lifeQuality;
    }

    final public Place getHome() {
        return home;
    }

    final public Mood getMood() {
        return mood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, lifeQuality, home, mood);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person other = (Person) o;

        return Objects.equals(name, other.name)
                && Objects.equals(gender, other.gender)
                && Objects.equals(lifeQuality, other.lifeQuality)
                && Objects.equals(home, other.home)
                && Objects.equals(mood, other.mood);
    }

    @Override
    public String toString() {
        return getClass().getName() +
                "[name=" + name +
                ", gender=" + gender +
                ", lifeQuality=" + lifeQuality +
                ", home=" + home +
                ", mood=" + mood +
                "]";
    }
}
