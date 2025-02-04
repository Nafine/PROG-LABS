package se.ifmo.client.communication;

import se.ifmo.system.collection.model.Vehicle;

import java.util.List;


public record Request(String command, List<String> args, List<Vehicle> vehicles) {
}
