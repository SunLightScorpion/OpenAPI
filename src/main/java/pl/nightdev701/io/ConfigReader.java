package pl.nightdev701.io;

/*

Lukas - 16:08
18.12.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.logger.AbstractLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigReader {

    private final Map<String, String> dataMap;
    private final AbstractLogger logger;

    public ConfigReader(String path, AbstractLogger logger) {
        this.logger = logger;
        this.dataMap = new LinkedHashMap<>();

        readConfig(path);
    }

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

    public String getData(String key) {
        return dataMap.get(key);
    }

}
