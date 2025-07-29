package com.openmock.seatguruscrapper;

import com.openmock.util.NumberUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Log4j2
public class AirlineScrapperLauncher {
    private static final int DEFAULT_NUM_THREADS = 4;

    private static final String SHORT_PARAM_THREADS = "t";
    private static final String LONG_PARAM_THREADS = "threads";

    private static final String HELP = """
            Generates a json with the airline infomrmaton (amenities, aircraft...)
            from seatguru.com.
            
            Call example:
            
            java -jar AirlineScrapper.jar --threads 8 
            """;

    public static void main(String[] args) {
        Options options = new Options();

        Option threadOption = new Option(SHORT_PARAM_THREADS, LONG_PARAM_THREADS, true, "Number of thread to process seatguru.con site. Default value: 4");
        options.addOption(threadOption);

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

        BlockingQueue<AirlineJob> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < numConsumers; i++) {
            log.info(">> Consumer {} launched.", i);
            new Thread(new AirlineScrapperConsumer(queue)).start();
        }
        log.info("# {} consumers launched in total.", numConsumers);


        log.info(">>> Producer launched.");
        new Thread(new AirlineScrapperProducer(queue, numConsumers)).start();
    }

    private static int validateParamThreads(CommandLine cmd) throws InvalidParameterException {
        int numThreads = DEFAULT_NUM_THREADS;

        if (cmd.hasOption(LONG_PARAM_THREADS) || cmd.hasOption(SHORT_PARAM_THREADS)) {
            String strNumThreads = cmd.getOptionValue(LONG_PARAM_THREADS);
            if (strNumThreads != null && NumberUtil.isPositiveInt(strNumThreads)) {
                numThreads = Integer.parseInt(strNumThreads);

                if (numThreads == 0) {
                    throw new InvalidParameterException("--threads # Should be a positive integer bigger or equal than 1");
                }

                int cores = Runtime.getRuntime().availableProcessors();
                log.debug("# cores: {}", cores);

                if (numThreads > cores) {
                    throw new InvalidParameterException("--threads # Should be a positive integer smaller o equal than # of cores (" + cores + ")");
                }
            }
        }

        return numThreads;
    }

}
