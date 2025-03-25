package se.ifmo.server;

import se.ifmo.client.util.EnvManager;
import se.ifmo.server.collection.CollectionManager;

import java.io.IOException;


/**
 * Class which used only as an entry point for server side of the program.
 */
public class ServerMain {
    public static void main(String[] args) {
        try (Server server = new Server()) {
            System.out.printf("Server is running on port %d%n", EnvManager.getPort());
            //Load collection to avoid errors
            CollectionManager.getInstance();
            server.run();
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
