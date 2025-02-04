package se.ifmo.client.communication;

import se.ifmo.client.command.Active;
import se.ifmo.client.command.Command;

import java.util.Objects;

public class Router {
    private static Router instance;

    private Router() {
    }

    public static Router getInstance() {
        return Objects.isNull(instance) ? instance = new Router() : instance;
    }

    public int getElementsRequiredFor(String command) {
        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(command)).findFirst()
                .map(Command::getElementsRequired).orElse(0);
    }

    public Callback route(Request request) {
        if (request == null || request.command() == null | request.command().isBlank()) return Callback.empty();
        if (request.command().equals("help")) return getHelp();

        return Active.LIST.stream()
                .filter(temp -> temp.getName().equalsIgnoreCase(request.command()))
                .findFirst()
                .map(temp -> temp.execute(request))
                .orElse(new Callback("command not found, type 'help' for help"));
    }

    public Callback getHelp() {
        return Callback.empty();
    }
}
