package se.ifmo.client.communication;

import se.ifmo.client.command.RegisteredCommands;
import se.ifmo.client.command.util.HistoryManager;
import se.ifmo.system.exceptions.InvalidDataException;

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
            return RegisteredCommands.LIST.stream()
                    .filter(temp -> temp.getName().equalsIgnoreCase(req.command()))
                    .findFirst()
                    .map(temp -> {
                        HistoryManager.getInstance().addCommand(temp.getName());
                        try {
                            return temp.execute(req);
                        } catch (IndexOutOfBoundsException e) {
                            return new Callback("Wrong amount of arguments (must be at least " + temp.getArgs().length + ")");
                        } catch (InvalidDataException e) {
                            return new Callback("You've input an invalid data: " + e.getMessage());
                        } catch (IllegalArgumentException e) {
                            return new Callback("Wrong arguments: " + e.getMessage());
                        }
                    })
                    .orElse(new Callback("command not found, type 'help' for help"));
        } catch (Exception e) {
            return new Callback("Something went wrong");
        }
    }
}
