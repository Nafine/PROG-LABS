package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.server.collection.CollectionManager;
import se.ifmo.shared.model.Vehicle;

import java.util.LinkedHashSet;

/**
 * Shows some specific information about collection.
 */
public class Info extends Command{
    /**
     * Constructs a new {@link Info} command.
     */
    public Info(){
        super("info", "Shows information about collection (size, type and e.t.c)");
    }

    /**
     * Currently outputs type of collection and its size.
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req){
        LinkedHashSet<Vehicle> collection = CollectionManager.getInstance().getCollection();
        return new Callback(String.join("\n", new String[]{"type: " + collection.getClass(), "size: " + collection.size()}));
    }
}
