package se.ifmo.client.command;

import se.ifmo.client.command.util.VehicleReader;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.exceptions.InvalidDataException;

public class Add extends Command {

    public Add() {
        super("add", Command.EMPTY_ARGS, "Adds new element to the collection");
    }

    @Override
    public Callback execute(Request req) {
        try {
            CollectionManager.getInstance().getCollection().add(VehicleReader.readElement(req.console()));
            return new Callback("Successfully added element to the collection");
        } catch (InterruptedException e) {
            return new Callback("Command got interrupted.");
        }
        catch (InvalidDataException e) {
            return new Callback("You've inputed an invalid data.");
        }
    }
}
