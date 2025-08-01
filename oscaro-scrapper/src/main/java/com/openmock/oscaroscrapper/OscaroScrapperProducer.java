package com.openmock.oscaroscrapper;

import com.openmock.oscaroscrapper.dto.Brand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class OscaroScrapperProducer implements Runnable {
    private BlockingQueue<BrandJob> queue;
    private int numConsumers;
    private String lang;

    @Setter(AccessLevel.NONE)
    private static final Logger log = LogManager.getLogger(OscaroScrapperProducer.class);

    @Override
    public void run() {
        OscaroScrapper wrapper = new OscaroScrapper(lang);
        List<Brand> brands = wrapper.getBrands();

        if (brands != null) {
            for (Brand brand : brands) {
                log.info("Brand {} added.", brand.getName());
                queue.add(new BrandJob(JobType.BRAND, brand));
            }
        }

        for (int i = 0; i < numConsumers; i++) {
            log.info("Adding KILL JOB {} message.", i);
            queue.add(new BrandJob(JobType.KILL_JOB));
        }
    }
}
