package com.openmock.oscaroscrapper.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    private BufferedWriter writer = null;

    private static final Logger log = LogManager.getLogger(CSVWriter.class);

    public CSVWriter(String filename) {
        try {
            writer = new BufferedWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            log.error("Error opening file: ", e);
        }
    }


    public void write(String[] text) {
        StringBuilder sBuilder = new StringBuilder();

        if (text != null) {
            int size = text.length;
            for (int i = 0; i < size; i++) {
                sBuilder.append(text[i]);

                if (i != size - 1) {
                    sBuilder.append(",");
                }
            }
        }

        write(sBuilder.toString());
    }

    public void write(String text) {
        try {
            writer.append(text).append("\n");
        } catch (IOException e) {
            log.error("Error writing file: ", e);
        }
    }

    /**
     * Close the CSV file
     */
    public void close() {
        if (writer != null) {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                log.error("Error closing CSV file: ", e);
            }
        }
    }
}
