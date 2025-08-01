package com.openmock.oscaroscrapper.io;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVWriterTest {
    @Test
    public void write() {
        CSVWriter writer = new CSVWriter("example.csv");

        //Header
        writer.write("name,alias");
        writer.write("Peter Parker,Spiderman");
        writer.write("James Howlett,Wolverine ");
        writer.write(new String[]{"Clark Kent", "Superman"});

        writer.close();

        File example = new File("example.csv");
        assertTrue(example.exists());

        assertTrue(example.delete());
    }
}
