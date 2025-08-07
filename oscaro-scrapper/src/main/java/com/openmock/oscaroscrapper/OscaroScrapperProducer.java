package com.openmock.oscaroscrapper;

import com.openmock.oscaroscrapper.dto.Brand;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OscaroScrapperProducer implements Runnable {
    private BlockingQueue<BrandJob> queue;
    private int numConsumers;
    private String lang;
    private boolean useZenRowsAPI;


    @Override
    public void run() {
        OscaroScrapperType scrapperType = OscaroScrapperType.DEFAULT;

        if(useZenRowsAPI){
            scrapperType=OscaroScrapperType.ZEN_ROW;
        }

        AbstractOscaroScrapper scrapper = OscaroScrapperFactory.getInstance().getOscaroScrapper(scrapperType, lang);
        List<Brand> brands = scrapper.getBrands();

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
