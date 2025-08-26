package com.openmock.openavscrapper;

import com.openmock.Airport;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OpenNavScrapperConsumer implements Runnable {
    private BlockingQueue<AirportJob> queue;
    private String output;



    @Override
    public void run() {
        AirportJob job;
        Airport airport = null;
        String country;
        String name;
        String iata;
        String icao;

        try {
            while (true) {
                job = queue.take();

                log.info("> Job type: {} ", job.getType());

                if (job.getType() == AirportJobType.END) {
                    log.info("OpenNav consumer ending...");
                    return;
                }


                log.info("URL to process: {}", job.getUrl());
                airport = AirportScrapper.getAirport(job.getUrl());

                country = airport.getIsoCountryCode() == null? "unknown": airport.getIsoCountryCode();
                iata = airport.getIata() == null? "" : airport.getIata();
                icao = airport.getIcao() == null? "" : airport.getIcao();
                name = iata + "-" + icao + ".json";

                stringToFile(airport.toJSON(), Paths.get(output, country, name).toString());
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            log.error("Error writing json file: ", e);
        }
    }

    public static void stringToFile(String content, String filePath) throws IOException {
        Path path = Paths.get(filePath);

        log.info("Writing file: {}", path.toString());

        // Create parent directories if they don't exist
        Path parentDir = path.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        // Write content to file (creates file if it doesn't exist)
        Files.write(path, content.getBytes());
    }
}
