package se.ifmo.client.communication;

import se.ifmo.system.collection.model.Vehicle;

import java.util.Collections;
import java.util.List;

public record Callback(String message, List<Vehicle> vehicles) {

    public Callback(String message, Vehicle... vehicles)
    {
        this(message, List.of(vehicles));
    }

    public Callback(String message){
        this(message, Collections.emptyList());
    }

    public static Callback empty(){
        return new Callback(null);
    }
}
