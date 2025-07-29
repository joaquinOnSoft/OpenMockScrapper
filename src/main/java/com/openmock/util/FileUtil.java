package com.openmock.util;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
public class FileUtil {
    /**
     * Create a file with the given text
     * @param path - File path
     * @param str - String to be written in the file
     * @return true if the file is created successfully, false in other case
     */
    public static boolean save(String path, String str) {
        return save(Paths.get(path), str);
    }

    /**
     * Create a file with the given text
     * @param path - File path
     * @param str - String to be written in the file
     * @return true if the file is created successfully, false in other case
     */
    public static boolean save(Path path, String str) {
        boolean saved = true;

        // Try block to check for exceptions
        try {
            // Now calling Files.writeString() method
            // with path , content & standard charsets
            Files.writeString(path, str, StandardCharsets.UTF_8);
        }
        // Catch block to handle the exception
        catch (IOException ex) {
            log.error("Invalid Path", ex);
            saved = false;
        }

        return saved;
    }

    public static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
