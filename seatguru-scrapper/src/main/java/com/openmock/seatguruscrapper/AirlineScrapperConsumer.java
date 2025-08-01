package com.openmock.seatguruscrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmock.Airline;
import com.openmock.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
@Log4j2
public class AirlineScrapperConsumer implements Runnable {
    private BlockingQueue<AirlineJob> queue;

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

                //Convert object to pretty print string
                ObjectMapper mapper = new ObjectMapper();
                String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(airline);
                FileUtil.save(
                        Paths.get(FileUtil.getWorkingDirectory(), airline.getName().replace(" ", "-") + ".json"),
                        indented);
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        } catch (JsonProcessingException e) {
            log.error("JSON string generation failed: ", e);
        }
        //catch (IOException e) {
        //    log.error("I/O error: ", e);
        //}
    }
}
