package com.openmock.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class FileUtilTest {
    private static final String EXAMPLE_FILE_NAME = "renault.csv";

    @Test
    public void getFileFromResources() {
        File f = FileUtil.getFileFromResources(EXAMPLE_FILE_NAME);
        assertNotNull(f);
        assertTrue(f.exists());
    }

    @Test
    public void loadProperties() {
        Properties p = FileUtil.loadProperties("zenrows.properties");
        assertNotNull(p);
        assertEquals("<API_KEY>", p.getProperty("apikey"));
    }
}