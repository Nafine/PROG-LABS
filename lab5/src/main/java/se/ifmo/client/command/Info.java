package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;
import se.ifmo.system.collection.model.Vehicle;

import java.util.LinkedHashSet;

public class Info extends Command{
    public Info(){
        super("info", "Shows information about collection (size, type and e.t.c)");
    }

    @Override
    public Callback execute(Request req){
        LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
        return new Callback(String.join("\n", new String[]{"type: Vehicle", "size: " + collection.size()}));
    }
}
