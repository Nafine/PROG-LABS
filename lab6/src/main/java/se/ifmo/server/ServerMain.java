package se.ifmo.server;

import se.ifmo.server.collection.CollectionManager;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try (Server server = new Server()) {
            System.out.println("Server is running...");
            //Load collection to avoid errors
            CollectionManager.getInstance();
            server.run();
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
