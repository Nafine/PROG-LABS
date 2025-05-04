package se.ifmo.shared.command;

import se.ifmo.server.db.UserService;
import se.ifmo.shared.communication.Callback;
import se.ifmo.shared.communication.Request;
import se.ifmo.shared.exceptions.InvalidDataException;

/**
 * Client-side command used to log in user.
 */
public class Register extends Command {
    /**
     * Constructs a new {@link Add} command.
     */
    public Register() {
        super("register", "Register which creates pair [username, password] in database");
    }

    /**
     * Executes the command to create another user.
     * <p>
     * Creates pair (login, password) if no such pair exist, and returns corresponding callback.
     * </p>
     *
     * @param req {@link Request}
     * @return {@link Callback}
     */
    @Override
    public Callback execute(Request req) throws InvalidDataException {
        if (UserService.getInstance().getUserID(req.credentials().username()) != -1) return Callback.existingUser();
        return UserService.getInstance().addUser(req.credentials()) ? Callback.successfulLogin() : Callback.failedRegister();
    }
}
