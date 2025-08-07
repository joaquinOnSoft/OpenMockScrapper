package com.openmock.util;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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

    /**
     * Get file from classpath, resources folder
     * SEE:
     * <a href="https://www.mkyong.com/java/java-read-a-file-from-resources-folder/">
     * Java – Read a file from resources folder
     * </a>
     *
     * @param fileName - file name to be read from resources folder
     * @return file from resource
     */
    public static File getFileFromResources(String fileName) {
        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    public static InputStream getStreamFromResources(String fileName) throws IllegalArgumentException {
        InputStream resource = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File '" + fileName + "' is not found!");
        } else {
            return resource;
        }
    }

    public static  Properties loadProperties(String propFileName) {
        Properties prop = null;

        log.debug("Properties file name: " + propFileName);
        InputStream propFile = FileUtil.getStreamFromResources(propFileName);

        try {
            prop = new Properties();
            log.debug("Loading");
            prop.load(propFile);
            log.debug("Loaded");
        }
        catch (FileNotFoundException e) {
           log.error("Properties file not found", e);
        }
        catch (IOException e) {
            log.error("Properties file: ", e);
        }

        return prop;
    }
}