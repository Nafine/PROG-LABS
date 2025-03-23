package se.ifmo.client.io.console;

import lombok.Setter;

import java.io.*;

/**
 * Class which defines console which user will use to communicate with program.
 */
public class StandardConsole implements Console {
    @Setter
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    @Setter
    private BufferedWriter consoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public String read(String prompt) {
        writeln(prompt);
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
    public void close() throws Exception {
        consoleReader.close();
        consoleWriter.close();
    }
}
