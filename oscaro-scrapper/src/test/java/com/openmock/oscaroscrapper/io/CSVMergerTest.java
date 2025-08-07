package com.openmock.oscaroscrapper.io;

import com.openmock.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVMergerTest {

    public static final String VEHICLES_CSV = "vehicles.csv";

    @Test
    public void mergeCSVFiles() {
        File resources = FileUtil.getFileFromResources("renault.csv");

        assertNotNull(resources);

        try {
            CSVMerger.mergeCSVFiles(resources.getParent(), VEHICLES_CSV);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        File vehicles = FileUtil.getFileFromResources(VEHICLES_CSV);
        assertNotNull(vehicles);
        assertTrue(vehicles.exists());
        assertTrue(vehicles.isFile());

        //TODO count output file lines. It must be 6

        //Remove merged file
        assertTrue(vehicles.delete());
    }
}
