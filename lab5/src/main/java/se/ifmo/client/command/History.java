package se.ifmo.client.command;

import se.ifmo.client.command.util.HistoryManager;
import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;

/**
 * Show last 9 executed commands.
 */
public class History extends Command {
    public History() {
        super("history", "Show last 9 used commands");
    }

    /**
     * Calls getter of circular buffer from {@link HistoryManager}.
     * @param req {@link Request}
     * @return {@link Callback} last 9 executed commands
     */
    @Override
    public Callback execute(Request req) {
        return new Callback(String.join("\n",HistoryManager.getInstance().getHistory()));
    }
}
