package pl.nightdev701.logger.standard;

/*

Lukas - 15:02
30.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.OpenAPI;
import pl.nightdev701.logger.AbstractLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultLogger extends AbstractLogger {

    @Override
    public void log(Object level, String msg) {

        File logFolder = new File("logs");

        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        Logger logger = Logger.getLogger("openapi");
        logger.log(Level.parse(level.toString()), msg);

        String result = OpenAPI.getClockTime() + " | " + level + " | " + msg;

        try {
            File file = new File("logs/events-" + OpenAPI.getCurrentDay() + ".log");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8));
            writer.write(result + "\n");
            writer.flush();

            System.out.print(result+"\n");
        } catch (IOException e) {
            System.err.println("Failed to log! " + e.getMessage());
            e.printStackTrace();
            System.out.println("(" + OpenAPI.getCurrentClockDate() + ") Log data: " + level + ", " + msg);
        }

    }

}
