package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.system.collection.CollectionManager;

public class Save extends Command{
    public Save(){
        super("save", "Saves the collection to file");
    }

    @Override
    public Callback execute(Request req){
        CollectionManager.getInstance().save();

        return new Callback("Successfully saved the collection to file");
    }
}
