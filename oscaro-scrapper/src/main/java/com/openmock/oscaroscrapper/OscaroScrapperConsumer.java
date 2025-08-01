package com.openmock.oscaroscrapper;

import com.openmock.oscaroscrapper.dto.Brand;
import com.openmock.oscaroscrapper.dto.Family;
import com.openmock.oscaroscrapper.dto.Model;
import com.openmock.oscaroscrapper.dto.Type;
import com.openmock.oscaroscrapper.util.DateUtil;
import com.opencsv.CSVWriter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@AllArgsConstructor
public class OscaroScrapperConsumer implements Runnable {
    private BlockingQueue<BrandJob> queue;
    private String lang;

    @Setter(AccessLevel.NONE)
    private static final Logger log = LogManager.getLogger(OscaroScrapperConsumer.class);

    @Override
    public void run() {
        try {
            BrandJob job;
            Brand brand;
            OscaroScrapper wrapper = new OscaroScrapper(lang);

            while (true) {
                job = queue.take();

                log.info("> Job type: {} ", job.getType());

                if (job.getType() == JobType.KILL_JOB) {
                    log.info("Consumer ending...");
                    return;
                }

                brand = job.getBrand();
                log.info("{} > {}", brand.getId(), brand.getName());
                brand = wrapper.getBrandTypes(brand);

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
