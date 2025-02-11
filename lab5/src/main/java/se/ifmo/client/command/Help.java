package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

public class Help extends Command{
    public Help() {
        super("help", "List all available commands");
    }

    @Override
    public Callback execute(Request req){
        StringBuilder stringBuilder = new StringBuilder();

        for (Command command : RegisteredCommands.LIST) stringBuilder.append(command.getName()).append(' ').append(command.getDescription()).append(",\n");

        return new Callback(stringBuilder.toString());
    }
}
