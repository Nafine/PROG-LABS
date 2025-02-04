package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Clear extends Command{
    @Override
    public Callback execute(Request req){return Callback.empty();}
}
