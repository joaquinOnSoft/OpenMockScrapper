package com.openmock.openavscrapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OpenNavScrapperProducer implements Runnable {
    private BlockingQueue<AirportJob> queue;
    private int numConsumers;
    private String output;
    private boolean skipExistings;

    @Override
    public void run() {
        List <String> existing = null;
        List<String> airportURLs = AirportScrapper.getAirportsURLs();

        if(skipExistings){
            try {
                existing = IcaoCodeFinder.getICAOCodes(output);
            } catch (IOException e) {
                log.error("Error retrieving existing airport .json files from output folder: ", e);
            }
        }

        if (!airportURLs.isEmpty()) {
            for (String url : airportURLs) {
                if(skipExistings){
                    String icao = getICAOFromURL(url);
                    if(icao != null && existing != null && existing.contains(icao) ) {
                        log.info("Airport URL SKIPPED: {}", url);
                        continue;
                    }
                }
                log.info("Airport URL: {}", url);
                queue.add(new AirportJob(url));
            }
        }

        for (int i = 0; i < numConsumers; i++) {
            log.info("Adding END JOB {} message.", i);
            queue.add(new AirportJob(AirportJobType.END));
        }
    }

    private String getICAOFromURL(String url){
        String icao = null;

        if(url != null){
            int index = url.lastIndexOf("/");
            if(index != -1)
                icao = url.substring(index +1);
        }
        return icao;
    }
}
