package pl.nightdev701.logger.standard;

/*

Lukas - 15:02
30.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.logger.AbstractLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultLogger extends AbstractLogger {

    @Override
    public void log(Level level, String msg) {
        Logger logger = Logger.getLogger("openapi");
        logger.log(level, msg);
    }

}
