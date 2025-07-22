package com.openmock.seatguruscrapper;

import com.openmock.Airline;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class AirlineScrapperConsumer implements Runnable {
    private BlockingQueue<AirlineJob> queue;
    private String lang;

    @Setter(AccessLevel.NONE)
    private static final Logger log = LogManager.getLogger(AirlineScrapperConsumer.class);

    @Override
    public void run() {
        AirlineJob job;

        try {

            while (true) {
                job = queue.take();

                log.info("> URL: {} ", job.getUrl());

                if (job.isLast()) {
                    log.info("Consumer ending...");
                    return;
                }

                Airline airline = AirlinesScrapper.getAirline(job.getUrl());
                //TODO write json file
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        }
        //catch (IOException e) {
        //    log.error("I/O error: ", e);
        //}
    }
}
