package se.ifmo.shared.command;

import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;

/**
 * Shows description of all commands.
 */
public class Help extends Command {
    /**
     * Constructs a new {@link Help} command.
     */
    public Help() {
        super("help", "List all available commands");
    }

    /**
     * Shows description all {@link Command} listed in class {@link RegisteredCommands}.
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Command command : RegisteredCommands.MAP.values()) {
            stringBuilder.append(command.getName()).append(" - ").append(command.getDescription()).append("\n");
            if (command.getArgs() != Command.EMPTY_ARGS) {
                stringBuilder.append("args: (").append(String.join(", ", command.getArgs())).append(")\n");
            }
            stringBuilder.append("\n");
        }

        return new Callback(stringBuilder.toString());
    }
}
