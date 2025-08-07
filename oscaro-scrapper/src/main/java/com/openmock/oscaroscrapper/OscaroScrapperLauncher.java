package com.openmock.oscaroscrapper;

import com.openmock.util.NumberUtil;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OscaroScrapperLauncher {
    private static final int DEFAULT_NUM_THREADS = 4;
    private static final String DEFAULT_LANG = "es";

    private static final String SHORT_PARAM_THREADS = "t";
    private static final String LONG_PARAM_THREADS = "threads";

    private static final String SHORT_PARAM_LANG = "l";
    private static final String LONG_PARAM_LANG = "lang";

    private static final String SHORT_PARAM_ZEN_ROWS = "z";
    private static final String LONG_PARAM_ZEN_ROWS = "zen";

    private static final String HELP = """
            Generates a CSV with the brands, families, models and types
            of vehicles available in Oscaro.
            
            Valid parameters:
            
              --threads or -t: (Optional) Number of threads used simultaneously to parse the page
              --lang or -l: (Optional) ISO-2 language code. Supported languages: "es" (Spanish),
                            "fr" (French) and "pt" (Portuguese). Default value: "es"
              --zen or -z: (optinal) Flat to indicate that Zen Rows API must be used to bypass
                           Cloudfare block to parse the page. It requires an API key to work.
            
            Call example:
            
            java -jar OscaroScrapper.jar --threads 8 --lang en
            """;

    private static final Logger log = LogManager.getLogger(OscaroScrapperLauncher.class);

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        int numConsumers = DEFAULT_NUM_THREADS;
        String lang = DEFAULT_LANG;
        boolean useZenRowsAPI = false;

        try {
            cmd = parser.parse(options, args);
            numConsumers = validateParamThreads(cmd);
            lang = validateParamLang(cmd);
            useZenRowsAPI = cmd.hasOption(LONG_PARAM_ZEN_ROWS);
        } catch (ParseException | InvalidParameterException e) {
            formatter.printHelp(HELP, options);
            log.error(e.getMessage());
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        BlockingQueue<BrandJob> queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < numConsumers; i++) {
            log.info(">> Consumer {} launched.", i);
            new Thread(new OscaroScrapperConsumer(queue, lang, useZenRowsAPI)).start();
        }
        log.info("# {} consumers launched in total.", numConsumers);


        log.info(">>> Producer launched.");
        new Thread(new OscaroScrapperProducer(queue, numConsumers, lang, useZenRowsAPI)).start();
    }

    private static Options getOptions() {
        Options options = new Options();

        Option threadOption = new Option(SHORT_PARAM_THREADS, LONG_PARAM_THREADS, true, "(optinal) Number of thread to process simultaneously the contracts .csv file. Default value: 4");
        options.addOption(threadOption);

        Option langOption = new Option(SHORT_PARAM_LANG, LONG_PARAM_LANG, true, "(optinal) Language code ISO-2 to retrieve the information. Default value: es");
        options.addOption(langOption);

        Option zenRowsOption = new Option(SHORT_PARAM_ZEN_ROWS, LONG_PARAM_ZEN_ROWS, false, "(optinal) Flat to indicate that Zen Rows API must be used to bypass Cloudfare block to parse the page.");
        options.addOption(zenRowsOption);
        return options;
    }


    private static String validateParamLang(CommandLine cmd) throws InvalidParameterException {
        String lang = DEFAULT_LANG;

        if (cmd.hasOption(LONG_PARAM_LANG) || cmd.hasOption(SHORT_PARAM_LANG)) {
            List<String> supportedLands = Arrays.asList("es", "fr", "pt");
            lang = cmd.getOptionValue(LONG_PARAM_LANG);

            if (!supportedLands.contains(lang.toLowerCase())) {
                throw new InvalidParameterException("--lang # Should be a supported language (es, fr, pt");
            }
        }

        return lang.toLowerCase();
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