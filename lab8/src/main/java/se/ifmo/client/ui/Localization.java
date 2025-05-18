package se.ifmo.client.ui;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Localization {
    private static final Map<Locale, Map<String, String>> RESOURCES = new HashMap<>();

    static {
        Map<String, String> enResources = new HashMap<>();
        //collection
        enResources.put("id", "ID");
        enResources.put("ownerId", "Owner ID");
        enResources.put("name", "Name");
        enResources.put("coordinateX", "Coordinate X");
        enResources.put("coordinateY", "Coordinate Y");
        enResources.put("creationDate", "Creation date");
        enResources.put("enginePower", "Engine power");
        enResources.put("capacity", "Capacity");
        enResources.put("distanceTravelled", "Distance travelled");
        enResources.put("fuelType", "Fuel type");


        //buttons
        enResources.put("login", "Login");
        enResources.put("register", "Register");
        enResources.put("username", "Username");
        enResources.put("password", "Password");
        enResources.put("profile", "Profile");
        enResources.put("filter", "Filter");
        enResources.put("applyFilter", "Apply filter");
        enResources.put("resetFilter", "Reset");
        enResources.put("add", "Add");
        enResources.put("clear", "Clear collection");
        enResources.put("visualize", "Visualize");
        enResources.put("collection", "Collection");
        enResources.put("type", "Type");
        enResources.put("size", "Size");
        enResources.put("firstElement", "First element");


        RESOURCES.put(Locale.ENGLISH, enResources);
    }

    private static Locale currentLocale = Locale.ENGLISH;

    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    public static String getString(String key) {
        return RESOURCES.get(currentLocale).get(key);
    }
}
