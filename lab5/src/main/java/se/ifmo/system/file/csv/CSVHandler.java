package se.ifmo.system.file.csv;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import se.ifmo.system.collection.model.Vehicle;
import se.ifmo.system.file.handler.IOHandler;

import java.io.*;
import java.nio.file.Path;
import java.util.LinkedHashSet;

public class CSVHandler implements IOHandler<LinkedHashSet<Vehicle>> {
    private final Path csvFilePath;
    private final BufferedInputStream fileInputStream;
    private final BufferedWriter bufferedWriter;


    public CSVHandler(Path csvFilePath) throws IOException {
        this.csvFilePath = csvFilePath;
        fileInputStream = new BufferedInputStream(new FileInputStream(csvFilePath.toFile()));
        bufferedWriter = new BufferedWriter(new FileWriter(csvFilePath.toFile()));
    }

    @Override
    public LinkedHashSet<Vehicle> read() {
        try {
            CsvMapper mapper = new CsvMapper();
            MappingIterator<Vehicle> it = mapper
                    .readerFor(Vehicle.class)
                    .with(mapper.schemaFor(Vehicle.class))
                    .readValues(fileInputStream);
            return new LinkedHashSet<>(it.readAll());
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.out.println(e.getMessage());
        }

        return new LinkedHashSet<>();
    }

    @Override
    public void write(LinkedHashSet<Vehicle> vehicles) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            SequenceWriter seqW = csvMapper.writerWithSchemaFor(Vehicle.class).writeValues(bufferedWriter);
            for (var vehicle : vehicles) {
                seqW.write(vehicle);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + csvFilePath.getFileName());
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void close() {

    }
}
