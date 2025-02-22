package se.ifmo.client.communication;

import se.ifmo.client.command.RegisteredCommands;
import se.ifmo.client.command.util.HistoryManager;

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
     * @param request to a command
     * @return {@link Callback}
     */
    public Callback route(Request request) {
        if (request == null || request.command() == null || request.command().isBlank()) return Callback.empty();

        try {
            return RegisteredCommands.LIST.stream()
                    .filter(temp -> temp.getName().equalsIgnoreCase(request.command()))
                    .findFirst()
                    .map(temp -> {
                        HistoryManager.getInstance().addCommand(temp.getName());
                        return temp.execute(request);
                    })
                    .orElse(new Callback("command not found, type 'help' for help"));
        } catch (Exception e) {
            return new Callback("Something went wrong");
        }
    }
}
