package pl.nightdev701.key;

/*

Lukas - 15:34
26.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.io.ConfigurationManager;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;

import java.io.File;
import java.util.logging.Level;

/**
 * string key
 */
public class ValueKey<T extends String> {

    T key;
    AbstractLogger logger;

    private ValueKey(T key, AbstractLogger logger) {
        this.key = key;
        this.logger = logger;

        boolean created = new File("key_values").mkdir();

        if(!created){
            this.logger.log(Level.WARNING, "Value folder cannot created!");
        }
    }

    /**
     * create key
     *
     * @return key value
     */
    public static ValueKey<String> getKey(Object key) {
        return new ValueKey<>(key.toString(), new DefaultLogger());
    }

    /**
     * create key (custom logger)
     *
     * @return key value
     */
    public static ValueKey<String> getKey(Object key, AbstractLogger logger) {
        return new ValueKey<>(key.toString(), logger);
    }

    /**
     * write value
     *
     */
    public void setKeyValue(String target){
        ConfigurationManager data = new ConfigurationManager("key_values", logger);
        data.set(key, target);
    }

    /**
     * read value
     *
     * @return key value
     */
    public Object getKeyValue() {
        ConfigurationManager data = new ConfigurationManager("key_values", logger);
        return data.getValue(key);
    }

}
