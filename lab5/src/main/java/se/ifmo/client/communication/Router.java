package se.ifmo.client.communication;

import se.ifmo.client.command.RegisteredCommands;
import se.ifmo.client.command.util.HistoryManager;

import java.util.Objects;

public class Router {
    private static Router instance;

    private Router() {
    }

    public static Router getInstance() {
        return Objects.isNull(instance) ? instance = new Router() : instance;
    }

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
        }
        catch (Exception e) {
            return new Callback("Something went wrong");
        }
    }
}
