package com.openmock.oscaroscrapper.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilTest {
    private static final String EXAMPLE_FILE_NAME = "renault.csv";

    @Test
    public void getFileFromResources() {
        File f = FileUtil.getFileFromResources(EXAMPLE_FILE_NAME);
        assertNotNull(f);
        assertTrue(f.exists());
    }
}