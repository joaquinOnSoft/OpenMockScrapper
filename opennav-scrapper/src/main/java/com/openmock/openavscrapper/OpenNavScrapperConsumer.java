package com.openmock.openavscrapper;

import com.openmock.Airport;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OpenNavScrapperConsumer implements Runnable {
    private BlockingQueue<AirportJob> queue;


    @Override
    public void run() {
        try {
            AirportJob job;

            while (true) {
                job = queue.take();

                log.info("> Job type: {} ", job.getType());

                if (job.getType() == AirportJobType.END) {
                    log.info("OpenNav consumer ending...");
                    return;
                }


                log.info("URL to process: {}", job.getUrl());
                Airport airport = AirportScrapper.getAirport(job.getUrl());

                System.out.println(airport.toJSON());
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        }

    }
}
