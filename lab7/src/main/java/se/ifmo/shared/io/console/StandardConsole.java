package se.ifmo.shared.io.console;

import lombok.Setter;

import java.io.*;

/**
 * Class which defines console which user will use to communicate with program.
 */
@Setter
public class StandardConsole implements Console {
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public String read(String prompt) {
        write(prompt);
        return read();
    }

    @Override
    public String read() {
        try {
            return consoleReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void write(String text) {
        try {
            consoleWriter.append(text).flush();
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean ready() {
        try {
            return consoleReader.ready();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
