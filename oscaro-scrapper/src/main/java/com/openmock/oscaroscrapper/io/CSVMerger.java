package com.openmock.oscaroscrapper.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class CSVMerger {

    private static final Logger log = LogManager.getLogger(CSVMerger.class);

    public static void mergeCSVFiles(String inputPath, String outputFilename) throws IOException {
        // Get working directory
        Path currentDir = Paths.get(inputPath);

        // Create output file
        Path outputPath = currentDir.resolve(outputFilename);
        AtomicBoolean isFirstFile = new AtomicBoolean(true);

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            // Process all the .csv file
            try (Stream<Path> paths = Files.list(currentDir)) {
                paths.filter(path -> path.toString().endsWith(".csv"))
                        .filter(path -> !path.equals(outputPath))  // Exclude output file
                        .forEach(path -> isFirstFile.set(processFile(path, writer, isFirstFile.get())));
            }
        }
    }

    private static boolean processFile(Path file, BufferedWriter writer, boolean isFirstFile) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            // Read header
            String header = reader.readLine();
            if (header == null)
                return isFirstFile;  // Empty file

            // Write header only for the first file
            if (isFirstFile) {
                writer.write(header);
                writer.newLine();
                isFirstFile = false;
            }

            // Copiar resto del contenido
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error merging file {} ", file, e);
        }

        return isFirstFile;
    }


}
