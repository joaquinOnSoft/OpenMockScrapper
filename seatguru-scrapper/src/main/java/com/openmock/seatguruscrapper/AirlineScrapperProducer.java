package com.openmock.seatguruscrapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
@Log4j2
public class AirlineScrapperProducer implements Runnable {
    private BlockingQueue<AirlineJob> queue;
    private int numConsumers;

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
