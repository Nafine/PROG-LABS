package se.ifmo.client.console;

import se.ifmo.system.file.handler.IOHandler;

/**
 * An interface which defines how io should work.
 */
public interface Console extends IOHandler<String> {
    /**
     * Reads from io and prompts to it.
     * @param prompt to prompt to user
     * @return {@link String}
     */
    String read(String prompt);

    /**
     * Only prompts to io.
     * @param prompt to prompt to user
     */
}
