package com.openmock.seatguruscrapper;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class AirlineScrapperProducer implements Runnable {
    private BlockingQueue<AirlineJob> queue;
    private int numConsumers;


    @Setter(AccessLevel.NONE)
    private static final Logger log = LogManager.getLogger(AirlineScrapperProducer.class);

    @Override
    public void run() {

        List<String> urls = AirlinesScrapper.getAirlineURLs();

        if (urls != null) {
            for (String url : urls) {
                log.info("URL {} added.", url);
                queue.add(new AirlineJob(url));
            }
        }

        for (int i = 0; i < numConsumers; i++) {
            log.info("Adding LAST AIRLINE JOB {} message.", i);
            queue.add(new AirlineJob());
        }
    }
}
