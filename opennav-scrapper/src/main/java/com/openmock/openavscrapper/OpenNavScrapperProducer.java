package com.openmock.openavscrapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OpenNavScrapperProducer implements Runnable {
    private BlockingQueue<AirportJob> queue;
    private int numConsumers;

    @Override
    public void run() {
        List<String> airportURLs = AirportScrapper.getAirportsURLs();

        if (airportURLs != null && !airportURLs.isEmpty()) {
            for (String url : airportURLs) {
                log.info("Airport URL: {}", url);
                queue.add(new AirportJob(url));
            }
        }

        for (int i = 0; i < numConsumers; i++) {
            log.info("Adding END JOB {} message.", i);
            queue.add(new AirportJob(AirportJobType.END));
        }
    }
}
