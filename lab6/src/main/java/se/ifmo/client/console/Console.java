package se.ifmo.client.console;

import se.ifmo.system.file.handler.IOHandler;

/**
 * An interface which defines how console should work.
 */
public interface Console extends IOHandler<String> {
    /**
     * Reads from console and prompts to it.
     * @param prompt to prompt to user
     * @return {@link String}
     */
    String read(String prompt);

    /**
     * Only prompts to console.
     * @param prompt to prompt to user
     */
    void writeln(String prompt);
}
