package se.ifmo.client.console;

import se.ifmo.system.file.handler.IOHandler;

public interface Console extends IOHandler<String> {
    String read(String prompt);
    void writeln(String prompt);
}
