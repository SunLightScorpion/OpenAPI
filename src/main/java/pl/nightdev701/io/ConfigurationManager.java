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

    public ConfigurationManager(String path, AbstractLogger logger) {
        this.logger = logger;
        if(path.endsWith(".slsd")){
            this.path = path;
        } else {
            this.path = path + ".slsd";
        }
        this.dataMap = new LinkedHashMap<>();

        readConfig(path);
    }

    /**
     * read config file
     */
    private void readConfig(String filePath) {

        logger.log(Level.CONFIG, "Read config...");

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

            logger.log(Level.INFO, "Config read!");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read config: " + e.getMessage());
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
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(base + "=")) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            logger.log(Level.WARNING, "The file was not found: " + e.getMessage());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while reading the file: " + e.getMessage());
        }
        return false;
    }

    /**
     * remove line
     */
    public void remove(String key) {
        if (!containsKey(key)) {
            logger.log(Level.WARNING, "Key does not exist: " + key);
            return;
        }

        try {
            File inputFile = new File(path);
            File tempFile = new File("tempFile.txt");

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
                    logger.log(Level.WARNING, "Error while renaming temp file to original file");
                }
            } else {
                logger.log(Level.WARNING, "Error while deleting original file");
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
            logger.log(Level.WARNING, "Line already exist: " + base);
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
                File tempFile = new File("tempFile.txt");

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
                    logger.log(Level.WARNING, "Error while deleting original file");
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
