package se.ifmo.server.communication;

import se.ifmo.shared.command.Command;
import se.ifmo.shared.command.RegisteredCommands;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;

import java.util.Objects;

/**
 * Singleton.
 * <p>
 * Class which used for routing {@link Request} to a specific command.
 * </p>
 */
public class Router {
    private static Router instance;

    private Router() {
    }

    /**
     * Returns instance of {@link Router}
     *
     * @return {@link Router}
     */
    public static Router getInstance() {
        return Objects.isNull(instance) ? instance = new Router() : instance;
    }

    /**
     * Routes specified {@link Request} to a command.
     * <p>
     * If command was not found returns specific {@link Callback} with only message.
     * </p>
     *
     * @param req to a command
     * @return {@link Callback}
     */
    public Callback route(Request req) {
        if (req == null || req.command() == null || req.command().isBlank()) return Callback.empty();

        try {
            if (RegisteredCommands.MAP.containsKey(req.command())) {
                Command command = RegisteredCommands.MAP.get(req.command());
                try {
                    return command.execute(req);
                } catch (IndexOutOfBoundsException e) {
                    return new Callback("Wrong amount of arguments (must be at least " + command.getArgs().length + ")");
                } catch (InvalidDataException e) {
                    return new Callback("You've input an invalid data: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    return new Callback("Wrong arguments: " + e.getMessage());
                }
            }
            return new Callback("Command not found");
        } catch (Exception e) {
            return Callback.serverSideError();
        }
    }
}
