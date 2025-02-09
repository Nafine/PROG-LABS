package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Add extends Command {

    public Add() {
        super("add", new String[]{""}, "", 0);
    }

    @Override
    public Callback execute(Request req){return Callback.empty();}
}
