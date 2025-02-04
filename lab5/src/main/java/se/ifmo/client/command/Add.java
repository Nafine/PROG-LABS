package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Add extends Command {

    @Override
    public Callback execute(Request req){return Callback.empty();}
}
