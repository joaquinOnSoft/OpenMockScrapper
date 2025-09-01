package com.openmock.openavscrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set; // Importación necesaria para Set
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to retrieve ICAO codes from JSON files in a directory tree.
 */
public class IcaoCodeFinder {

    // Regex pattern to match files like "LEMD-MAD.json" or "MAD-LEMD.json" (with 3 or 4 letter codes)
    private static final Pattern ICAO_PATTERN = Pattern.compile("^([A-Z]{3,4})-([A-Z]{3,4})\\.json$");

    /**
     * Scans a directory and its subdirectories for JSON files and extracts unique ICAO codes
     * based on a specific filename pattern.
     *
     * @param directoryPath The path to the directory to scan.
     * @return A list of unique ICAO codes found.
     * @throws IOException if an I/O error occurs while accessing the directory.
     */
    public static List<String> getICAOCodes(String directoryPath) throws IOException {
        Path startPath = Paths.get(directoryPath);

        // Ensure the provided path is a directory
        if (!Files.isDirectory(startPath)) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }

        try (Stream<Path> stream = Files.walk(startPath)) {

            // Recolectar los códigos en un Set para asegurar unicidad
            Set<String> uniqueCodes = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        Matcher matcher = ICAO_PATTERN.matcher(fileName);
                        if (matcher.matches()) {
                            return matcher.group(2); // Return the first captured group (ICAO code)
                        }
                        return null;
                    })
                    .filter(icaoCode -> icaoCode != null)
                    .collect(Collectors.toSet()); // Usamos toSet() para recolectar valores únicos

            // Convertir el Set a una List para el retorno del método
            return new java.util.ArrayList<>(uniqueCodes);
        }
    }
}