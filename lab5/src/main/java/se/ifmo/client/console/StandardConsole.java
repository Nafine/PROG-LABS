package se.ifmo.client.console;

import java.io.*;

public class StandardConsole implements Console {
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private final BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

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
        } catch (IOException e) {
        }
    }

    @Override
    public void writeln(String text) {
        try {
            consoleWriter.append(text).append(System.lineSeparator()).flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
