package pl.nightdev701.io;

/*

Lukas - 16:08
18.12.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigurationManager {

    private final Map<String, String> dataMap;
    private final String path;
    private final AbstractLogger logger;
    private final boolean allowLogging;

    public ConfigurationManager(String path, AbstractLogger logger, boolean allowLogging) {
        this.logger = logger;
        this.path = path.endsWith(".slsd") ? path : path + ".slsd";

        this.allowLogging = allowLogging;
        this.dataMap = new LinkedHashMap<>();

        readConfig(this.path);
    }

    /**
     * read config file
     */
    private void readConfig(String filePath) {

        if (!filePath.endsWith(".slsd")) {
            filePath = filePath + ".slsd";
        }

        if (allowLogging) {
            logger.log(Level.CONFIG, "Read config...");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    continue;
                }
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    dataMap.put(parts[0].trim(), parts[1].trim());
                }
            }

            if (allowLogging) {
                logger.log(Level.INFO, "Config read!");
            }
        } catch (IOException e) {
            File check = new File(filePath);

            if (check.exists()) {
                if (allowLogging) {
                    logger.log(Level.WARNING, "Failed to read config: " + e.getMessage());
                }
            } else {
                if (allowLogging) {
                    logger.log(Level.INFO, "File was not found, don't worry, the file will be created!");
                }
            }
        }
    }

    /**
     * return value
     */
    public String getValue(String key) {
        return dataMap.get(key);
    }

    /**
     * check if, key is contained
     */
    public boolean containsKey(String key) {
        return dataMap.containsKey(key);
    }

    /**
     * Check if file exist
     */
    public boolean existFile() {
        return new File(path).exists();
    }

    /**
     * check if line exist
     */
    public boolean isSet(String base) {
        return dataMap.containsKey(base);
    }

    /**
     * remove line
     */
    public void remove(String key) {
        if (!containsKey(key)) {
            if (allowLogging) {
                logger.log(Level.WARNING, "Key does not exist: " + key);
            }
            return;
        }

        try {
            File inputFile = new File(path);
            File tempFile = new File(path + ".tmp");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String lineToRemove = key + "=";

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith(lineToRemove)) {
                    continue;
                }
                writer.write(currentLine);
                writer.newLine();
            }
            writer.close();
            reader.close();

            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    if (allowLogging) {
                        logger.log(Level.WARNING, "Error while renaming temp file to original file");
                    }
                }
            } else {
                if (allowLogging) {
                    logger.log(Level.WARNING, "Error while deleting original file");
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while updating the file: " + e.getMessage());
        }

        readConfig(path);
    }

    /**
     * add line
     */
    public void add(String base, String value) {
        if (isSet(base)) {
            if (allowLogging) {
                logger.log(Level.WARNING, "Line already exist: " + base);
            }
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(path, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(base + "=" + value);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while writing to the file: " + e.getMessage());
        }

        readConfig(path);
    }

    /**
     * set value
     */
    public void set(String base, String value) {
        if (isSet(base)) {
            try {
                File inputFile = new File(path);
                File tempFile = new File(path + ".tmp");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String lineToRemove = base + "=";

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.startsWith(lineToRemove)) {
                        writer.write(base + "=" + value);
                    } else {
                        writer.write(currentLine);
                    }
                    writer.newLine();
                }
                writer.close();
                reader.close();

                if (inputFile.delete()) {
                    if (!tempFile.renameTo(inputFile)) {
                        logger.log(Level.WARNING, "Error while renaming temp file to original file");
                    }
                } else {
                    if (allowLogging) {
                        logger.log(Level.WARNING, "Error while deleting original file");
                    }
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error while updating the file: " + e.getMessage());
            }
        } else {
            add(base, value);
        }

        readConfig(path);
    }

}
