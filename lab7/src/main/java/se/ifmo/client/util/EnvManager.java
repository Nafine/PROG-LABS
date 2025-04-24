package se.ifmo.client.util;

import lombok.Getter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class which user for handling environment variables of the program.
 * <p>
 * Defines getters for both file paths: data and index files.
 * </p>
 */
public final class EnvManager {
    @Getter
    static InetAddress host;
    @Getter
    static int port;

    static {
        try {
            host = InetAddress.getByName(System.getenv("SERVER_HOST"));
            port = Integer.parseInt(System.getenv("SERVER_PORT"));
        } catch (UnknownHostException e) {
            System.out.println("Host not found, check setup files.");
        } catch (NullPointerException e) {
            System.err.println("CHECK environment variables, some of them was not defined.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("SERVER_PORT must be an integer value.");
            System.exit(1);
        }
    }
}
