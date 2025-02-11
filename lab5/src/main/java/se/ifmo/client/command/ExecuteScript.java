package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class ExecuteScript extends Command{
    public ExecuteScript() {
        super("execute_script", "Execute script from file");
    }

    @Override
    public Callback execute(Request req){return Callback.empty();}
}
