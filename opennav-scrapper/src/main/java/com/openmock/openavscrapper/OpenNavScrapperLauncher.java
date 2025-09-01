package com.openmock.openavscrapper;

import com.openmock.util.NumberUtil;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OpenNavScrapperLauncher {
    private static final int DEFAULT_NUM_THREADS = 4;
    private static final String DEFAULT_OUTPUT = ".";
    private static final boolean DEFAULT_SKIP_EXISTING = false;

    private static final String SHORT_PARAM_THREADS = "t";
    private static final String LONG_PARAM_THREADS = "threads";

    private static final String SHORT_PARAM_OUTPUT = "o";
    private static final String LONG_PARAM_OUTPUT = "output";

    private static final String SHORT_PARAM_SKIP_EXISTING = "s";
    private static final String LONG_PARAM_SKIP_EXISTING = "skip";

    private static final String HELP = """
            Generates multiple JSON files with airports information from opennav.com
            
            Valid parameters:
            
              --threads or -t: (Optional) Number of threads used simultaneously to parse the page. Default value: 4
              --output or -o: (Optional) Output path (directory). Default value '.'
              --skip or -s: (Optional) Skip existing downloaded airports. Default value 'false' if not specified
            
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
        String output = DEFAULT_OUTPUT;
        boolean skipExisting = DEFAULT_SKIP_EXISTING;

        try {
            cmd = parser.parse(options, args);
            numConsumers = validateParamThreads(cmd);
            output = validateParamOutput(cmd);
            skipExisting = validateParamSkipExisting(cmd);
        } catch (ParseException | InvalidParameterException e) {
            formatter.printHelp(HELP, options);
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        // javax.net.ssl.SSLHandshakeException: Remote host closed connection during handshake
        // during web service communication
        //
        // https://stackoverflow.com/questions/21245796/javax-net-ssl-sslhandshakeexception-remote-host-closed-connection-during-handsh
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        BlockingQueue<AirportJob> queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < numConsumers; i++) {
            log.info(">> Consumer {} launched.", i);
            new Thread(new OpenNavScrapperConsumer(queue, output)).start();
        }
        log.info("# {} consumers launched in total.", numConsumers);


        log.info(">>> Producer launched.");
        new Thread(new OpenNavScrapperProducer(queue, numConsumers, output, skipExisting)).start();
    }

    private static Options getOptions() {
        Options options = new Options();

        Option threadOption = new Option(SHORT_PARAM_THREADS, LONG_PARAM_THREADS, true, "(optinal) Number of thread to process simultaneously the contracts .csv file. Default value: 4");
        options.addOption(threadOption);

        Option outputOption = new Option(SHORT_PARAM_OUTPUT, LONG_PARAM_OUTPUT, true, "(Optional) Output path (directory). Default value '.'");
        options.addOption(outputOption);

        Option outputSkipExisting = new Option(SHORT_PARAM_SKIP_EXISTING, LONG_PARAM_SKIP_EXISTING, false, "(Optional) Skip existing downloaded airports. Default value 'false' if not specified");
        options.addOption(outputSkipExisting);

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

    private static String validateParamOutput(CommandLine cmd) throws InvalidParameterException {
        String outputStr = DEFAULT_OUTPUT;

        if (cmd.hasOption(LONG_PARAM_OUTPUT) || cmd.hasOption(SHORT_PARAM_OUTPUT)) {
            outputStr = cmd.getOptionValue(LONG_PARAM_OUTPUT);
            File outputPath = new File(outputStr);

            if(outputPath.isFile()){
                throw new InvalidParameterException("--output # should be a folder");
            }

            if(!outputPath.exists()){
                try {
                    Files.createDirectories(outputPath.toPath());
                } catch (IOException e) {
                    log.error(e);
                    log.error("Not possible to create directory: {}", outputStr);
                    throw new InvalidParameterException();
                }
            }
        }
        return outputStr;
    }

    private static boolean validateParamSkipExisting(CommandLine cmd) throws InvalidParameterException {
        boolean skip = DEFAULT_SKIP_EXISTING;

        if (cmd.hasOption(LONG_PARAM_SKIP_EXISTING) || cmd.hasOption(SHORT_PARAM_SKIP_EXISTING)) {
            skip = true;
        }

        return skip;
    }
}