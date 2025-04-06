package se.ifmo.client.util;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;

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
            host = InetAddress.getByName(System.getenv("HOST"));
            port = Integer.parseInt(System.getenv("SERVER_PORT"));
            Path errorFile = Path.of(System.getenv("ERROR_LOG"));
            System.setErr(new PrintStream(new FileOutputStream(errorFile.toFile())));
        } catch (UnknownHostException e) {
            System.out.println("Host not found, check setup files.");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR_LOG file was not found, error logging will be performed in the console.");
        } catch (NullPointerException e) {
            System.err.println("CHECK environment variables, some of them was not defined.");
            System.exit(1);
        } catch (NumberFormatException e) {
            System.out.println("SERVER_PORT must be an integer value.");
            System.exit(1);
        }
    }
}
