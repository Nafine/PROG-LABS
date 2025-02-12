package se.ifmo.client.command;

import se.ifmo.client.communication.Callback;
import se.ifmo.client.communication.Request;
import se.ifmo.client.communication.ScriptHandler;
import se.ifmo.client.communication.exceptions.AlreadyRunningScriptException;

import java.io.IOException;
import java.nio.file.Path;

public class ExecuteScript extends Command {
    public ExecuteScript() {
        super("execute_script", new String[]{"file_path"}, "Execute script from file");
    }

    @Override
    public Callback execute(Request req) {
        try (ScriptHandler scriptHandler = new ScriptHandler(Path.of(req.args().get(0)), req.console())) {
            scriptHandler.run();
            return new Callback("Script executed successfully");
        } catch (IOException e) {
            return new Callback("IO error: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            return new Callback("Wrong arguments (must be at least" + this.getArgs().length + ")");
        }
        catch (AlreadyRunningScriptException e){
            return new Callback("Script already running: " + e.getMessage());
        }
    }
}
