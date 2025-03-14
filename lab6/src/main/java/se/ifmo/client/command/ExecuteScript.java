package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.client.communication.exceptions.AlreadyRunningScriptException;

import java.io.IOException;

public class ExecuteScript extends Command {
    /**
     * Constructs a new {@link ExecuteScript} command.
     */
    public ExecuteScript() {
        super("execute_script", new String[]{"file_path"}, "Execute script from file");
    }

    /**
     * Runs and reads script file using it.
     * <p>
     * Handles script recursion, stopping script reading on recursive call.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        return new Callback("script" + req.args().get(0));
    }
}
