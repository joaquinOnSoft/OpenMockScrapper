package com.openmock.oscaroscrapper.util;

import java.io.File;
import java.net.URL;

public class FileUtil {
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
}