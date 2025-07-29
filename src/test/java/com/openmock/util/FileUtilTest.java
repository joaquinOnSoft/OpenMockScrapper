package com.openmock.util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtilTest {
    private static final String TXT = """
			The irony of it all, Pinky. Years of trying to take over 
			the world, and all I had to do was say ‘moo’.
			""";
    @Test
    void save() {
        Path path = Paths.get(FileUtil.getWorkingDirectory(), "example.txt");
        assertTrue(FileUtil.save(path, TXT));
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            fail(e);
        }
    }
}
