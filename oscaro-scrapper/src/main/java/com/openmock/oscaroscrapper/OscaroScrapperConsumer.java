package com.openmock.oscaroscrapper;

import com.opencsv.CSVWriter;
import com.openmock.oscaroscrapper.dto.Brand;
import com.openmock.oscaroscrapper.dto.Family;
import com.openmock.oscaroscrapper.dto.Model;
import com.openmock.oscaroscrapper.dto.Type;
import com.openmock.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@Log4j2
@AllArgsConstructor
public class OscaroScrapperConsumer implements Runnable {
    private BlockingQueue<BrandJob> queue;
    private String lang;
    private boolean useZenRowsAPI;

    @Override
    public void run() {
        try {
            BrandJob job;
            Brand brand;
            OscaroScrapperType scrapperType = OscaroScrapperType.DEFAULT;

            if(useZenRowsAPI){
                scrapperType=OscaroScrapperType.ZEN_ROW;
            }
            AbstractOscaroScrapper scrapper = OscaroScrapperFactory.getInstance().getOscaroScrapper(scrapperType, lang);

            while (true) {
                job = queue.take();

                log.info("> Job type: {} ", job.getType());

                if (job.getType() == JobType.KILL_JOB) {
                    log.info("Consumer ending...");
                    return;
                }

                brand = job.getBrand();
                log.info("{} > {}", brand.getId(), brand.getName());
                brand = scrapper.getBrandTypes(brand);

                if(useZenRowsAPI){
                    long milliseconds = millisecondsToSleep();
                    log.info("Sleeping {} milliseconds", milliseconds);
                    Thread.sleep(milliseconds);
                }

                writeCSV(brand);
            }
        } catch (InterruptedException e) {
            log.error("Thread interrupted: ", e);
            Thread.currentThread().interrupt();
        }
        catch (IOException e) {
            log.error("I/O error: ", e);
        }
    }

    private long millisecondsToSleep(){
        return (new Random()).nextInt(5000 - 1000) + 1000;
    }

    private void writeCSV(Brand brand) throws IOException {
        if (brand != null) {
            String filename = brand.getName()
                    .replace("Ë", "E") //CITROËN
                    .replace(" ", "_") + "-" + lang + ".csv";

            log.info("Writing vehicles CSV  file: {}", filename);

            //Writer writer, char separator, char quotechar, char escapechar, String lineEnd
            CSVWriter csvWriter  = new CSVWriter( new OutputStreamWriter(new FileOutputStream(filename), StandardCharsets.UTF_8));

            List<String[]> data = new LinkedList<>();

            String[] header = new String[]{
                    "brandId", "brandName",
                    "familyId", "familyName",
                    "modelId", "modelName", "manufacturedFrom", "manufacturedTo",
                    "typeId", "typeName", "typeFullName", "energy"
            };
            data.add(header);

            String[] line;
            for (Family family : brand.getFamilies()) {
                for (Model model : family.getModels()) {
                    for (Type type : model.getTypes()) {
                        line = new String[]{
                                brand.getId(),
                                brand.getName(),
                                family.getId(),
                                family.getName(),
                                model.getId(),
                                model.getName(),
                                DateUtil.dateToStr(model.getManufacturedFrom(), "MM/yyyy"),
                                DateUtil.dateToStr(model.getManufacturedTo(), "MM/yyyy"),
                                type.getId(),
                                type.getName(),
                                type.getFullName(),
                                type.getEnergy()
                        };

                        data.add(line);
                    }
                }
            }

            csvWriter.writeAll(data);
            csvWriter.close();
        }
    }
}
