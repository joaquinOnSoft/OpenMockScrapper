package com.openmock.openavscrapper;

import com.openmock.util.NumberUtil;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OpenNavScrapperLauncher {
    private static final int DEFAULT_NUM_THREADS = 4;

    private static final String SHORT_PARAM_THREADS = "t";
    private static final String LONG_PARAM_THREADS = "threads";


    private static final String HELP = """
            Generates multiple JSON files with airports information from opennav.com
            
            Valid parameters:
            
              --threads or -t: (Optional) Number of threads used simultaneously to parse the page
            
            Call example:
            
            java -jar OpenNavScrapper.jar --threads 8
            """;

    private static final Logger log = LogManager.getLogger(OpenNavScrapperLauncher.class);

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        int numConsumers = DEFAULT_NUM_THREADS;

        try {
            cmd = parser.parse(options, args);
            numConsumers = validateParamThreads(cmd);
        } catch (ParseException | InvalidParameterException e) {
            formatter.printHelp(HELP, options);
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        BlockingQueue<AirportJob> queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < numConsumers; i++) {
            log.info(">> Consumer {} launched.", i);
            new Thread(new OpenNavScrapperConsumer(queue)).start();
        }
        log.info("# {} consumers launched in total.", numConsumers);


        log.info(">>> Producer launched.");
        new Thread(new OpenNavScrapperProducer(queue, numConsumers)).start();
    }

    private static Options getOptions() {
        Options options = new Options();

        Option threadOption = new Option(SHORT_PARAM_THREADS, LONG_PARAM_THREADS, true, "(optinal) Number of thread to process simultaneously the contracts .csv file. Default value: 4");
        options.addOption(threadOption);
        return options;
    }

    private static int validateParamThreads(CommandLine cmd) throws InvalidParameterException {
        int numThreads = DEFAULT_NUM_THREADS;

        if (cmd.hasOption(LONG_PARAM_THREADS) || cmd.hasOption(SHORT_PARAM_THREADS)) {
            String strNumThreads = cmd.getOptionValue(LONG_PARAM_THREADS);
            if (strNumThreads != null && NumberUtil.isPositiveInt(strNumThreads)) {
                numThreads = Integer.parseInt(strNumThreads);

                if (numThreads == 0) {
                    throw new InvalidParameterException("--thread # Should be a positive integer bigger or equal than 1");
                }

                int cores = Runtime.getRuntime().availableProcessors();
                log.debug("# cores: {}", cores);

                if (numThreads > cores) {
                    throw new InvalidParameterException("--thread # Should be a positive integer smaller o equal than # of cores (" + cores + ")");
                }
            }
        }

        return numThreads;
    }
}