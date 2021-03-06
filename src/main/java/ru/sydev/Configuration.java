package ru.sydev;

import org.apache.commons.cli.*;

import java.util.Iterator;
import java.util.Properties;

/**
 * Created by Salavat.Yalalov on 18.12.2016.
 */
public class Configuration {
    public static final String DOWNLOAD_FOLDER = "download-folder";
    public static final String THREADS = "threads-number";
    public static final String BANDWIDTH = "bandwidth";
    public static final String LINKS = "links-folder";
    private static String[] optionNames = {DOWNLOAD_FOLDER, THREADS, BANDWIDTH, LINKS};

    private static Configuration instance = null;

    private Properties properties = null;
    private Options options = null;

    private Configuration(String[] args) {
        properties = new Properties();
        options = new Options();

        setDefaultConfigValues();
        initOptions();
        parseOptions(args);

    }

    public static synchronized Configuration getInstance(String[] args) {
        if (instance == null) {
            instance = new Configuration(args);
        }
        return instance;
    }

    public synchronized String getProperty(String key) {
        String value = null;
        if (properties.containsKey(key)) {
            value = (String)properties.get(key);
        } else {
            System.err.println("Something is wrong");
            System.exit(1);
        }
        return value;
    }

    private void initOptions() {
        Option opt = new Option("n", THREADS, true, "number of simultaneous threads");
        opt.setRequired(true);
        options.addOption(opt);

        opt = new Option("l", BANDWIDTH, true, "maximum bandwidth for all threads. you can use suffixes" +
                " k,m (k=1024, m=1024*1024)");
        opt.setRequired(true);
        options.addOption(opt);

        opt = new Option("f", LINKS, true, "file with links");
        opt.setRequired(true);
        options.addOption(opt);

        opt = new Option("o", DOWNLOAD_FOLDER, true, "folder name where to download");
        opt.setRequired(false);
        options.addOption(opt);
    }

    private void setDefaultConfigValues() {
        properties.put(THREADS, 1);
        properties.put(DOWNLOAD_FOLDER, "./");
        properties.put(BANDWIDTH, 0);
        properties.put(LINKS, "");
    }

    private void parseOptions(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        if (args.length == 0) {
            formatter.printHelp("utility", options);
            System.exit(1);
        }

        try {
            cmd = parser.parse(options, args);

        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility", options);
            System.exit(1);
        }


        Iterator iter = cmd.iterator();

        while (iter.hasNext()) {
            String option = (String)iter.next();
            for (int i = 0; i < optionNames.length; i++) {
                if (option.equals(optionNames[i])) {
                    properties.put(optionNames[i], option);
                }
            }
        }

    }
}
