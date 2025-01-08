package ru.itmo.world;

public record Place(String name, String location) {
    public Place(String name) {
        this(name, "неизвестно где");
    }

    public Place(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getLocation(){
        return location;
    }

    @Override
    public int hashCode() {
        return (name + location).hashCode();
    }

    /*@Override
    public String toString() {
        return getClass().getSimpleName() +
                "[name=" +"\"" + name + "\"" +
                ", location=" + "\"" + location + "\"" +
                "]";
    }*/
}
