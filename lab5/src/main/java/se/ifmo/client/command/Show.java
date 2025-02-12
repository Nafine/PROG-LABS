package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

public class Show extends Command{
    public Show() {
        super("show", "Shows whole collection");
    }

    @Override
    public Callback execute(Request req){return new Callback("Collection:", CollectionManager.getInstance().getCollection().stream().toList());}
}
