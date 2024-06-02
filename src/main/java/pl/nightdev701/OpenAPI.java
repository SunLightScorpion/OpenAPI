package pl.nightdev701;

/*

Lukas - 18:36
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.database.DatabaseConnector;
import pl.nightdev701.database.formular.DatabaseFormular;
import pl.nightdev701.database.formular.RedisFormular;
import pl.nightdev701.database.redis.JedisAdapter;
import pl.nightdev701.database.type.DatabaseType;
import pl.nightdev701.io.ConfigurationManager;
import pl.nightdev701.io.ScorpionFileReader;
import pl.nightdev701.io.ValueKey;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;
import pl.nightdev701.manager.CryptManager;
import pl.nightdev701.manager.FileCryptManager;
import pl.nightdev701.manager.KeyGeneratorManager;
import pl.nightdev701.network.HttpRequestHandler;
import pl.nightdev701.network.filter.WebFilter;
import pl.nightdev701.security.honeypot.SSHHoneypot;
import pl.nightdev701.util.CryptType;
import pl.nightdev701.util.stream.OpenPrintStream;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OpenAPI {

    /**
     * generate keys
     *
     * @param type
     */
    public static KeyGeneratorManager getKeyGeneratorManager(CryptType type) {
        return getKeyGeneratorManager(type, new DefaultLogger());
    }

    /**
     * generate keys
     *
     * @param type
     * @param logger
     */
    public static KeyGeneratorManager getKeyGeneratorManager(CryptType type, AbstractLogger logger) {
        return new KeyGeneratorManager(type, logger);
    }

    /**
     * encrypt and decrypt strings
     *
     * @param key
     * @param type
     */
    public static CryptManager getCryptManager(String key, CryptType type) {
        return getCryptManager(key, type, new DefaultLogger());
    }

    /**
     * encrypt and decrypt strings
     *
     * @param key
     * @param type
     * @param logger
     */
    public static CryptManager getCryptManager(String key, CryptType type, AbstractLogger logger) {
        return new CryptManager(key, type, logger);
    }

    /**
     * encrypt and decrypt files
     *
     * @param key
     * @param type
     * @param logger
     * @return
     */
    public static FileCryptManager getFileCryptManager(String key, CryptType type, AbstractLogger logger) {
        return new FileCryptManager(key, type, logger);
    }

    /**
     * encrypt and decrypt files
     *
     * @param key
     * @param type
     * @return
     */
    public static FileCryptManager getFileCryptManager(String key, CryptType type) {
        return new FileCryptManager(key, type, new DefaultLogger());
    }

    /**
     * ssh honeypot
     *
     * @param port
     * @param logger
     * @return
     */
    public static SSHHoneypot getSshHoneyPot(int port, AbstractLogger logger) {
        return new SSHHoneypot(port, logger);
    }

    /**
     * ssh honeypot
     *
     * @param port
     * @return
     */
    public static SSHHoneypot getSshHoneyPot(int port) {
        return getSshHoneyPot(port, new DefaultLogger());
    }

    /**
     * manage database connections and command easy
     *
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type, int port) {
        return getDatabaseManager(formular, type, port, new DefaultLogger());
    }

    /**
     * Read file
     *
     * @param path
     * @param logger
     */
    public static ScorpionFileReader getFileReader(String path, AbstractLogger logger) {
        return new ScorpionFileReader(path, logger);
    }

    /**
     * Read file
     *
     * @param path
     */
    public static ScorpionFileReader getFileReader(String path) {
        return getFileReader(path, new DefaultLogger());
    }

    /**
     * Config reader
     *
     * @param path
     * @param logger
     */
    public static ConfigurationManager getConfigurationManager(String path, AbstractLogger logger) {
        return new ConfigurationManager(path, logger);
    }

    /**
     * Config reader
     *
     * @param path
     */
    public static ConfigurationManager getConfigurationManager(String path) {
        return getConfigurationManager(path, new DefaultLogger());
    }

    /**
     * Web-Filter with custom logger
     *
     * @param port
     * @param blockList
     * @param logger
     * @return
     */
    public static WebFilter getWebUrlFilter(int port, List<String> blockList, AbstractLogger logger) {
        return new WebFilter(port, blockList, logger);
    }

    /**
     * Web-Filter
     *
     * @param port
     * @param blockList
     * @return
     */
    public static WebFilter getWebUrlFilter(int port, List<String> blockList) {
        return getWebUrlFilter(port, blockList, new DefaultLogger());
    }

    /**
     * Redis connector
     *
     * @param formular
     * @param logger
     */
    public static JedisAdapter getRedisConnector(RedisFormular formular, AbstractLogger logger) {
        return new JedisAdapter(formular.host(), formular.port(), logger);
    }

    /**
     * Redis connector
     *
     * @param formular
     */
    public static JedisAdapter getRedisConnector(RedisFormular formular) {
        return getRedisConnector(formular, new DefaultLogger());
    }

    /**
     * manage database connections and command easy with logger implementation
     *
     * @param logger
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type, int port, AbstractLogger logger) {
        return new DatabaseConnector(formular, type, port, logger);
    }

    /**
     * manage database connections and command easy
     *
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type) {
        return getDatabaseManager(formular, type, new DefaultLogger());
    }

    /**
     * manage database connections and command easy with logger implementation
     *
     * @param logger
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type, AbstractLogger logger) {
        return new DatabaseConnector(formular, type, logger);
    }

    /**
     * request http and handle it
     *
     * @param url
     */
    public static HttpRequestHandler getRequestHandler(String url) {
        return getRequestHandler(url, new DefaultLogger());
    }

    /**
     * request http and handle it with logger implementation
     *
     * @param logger
     * @param url
     */
    public static HttpRequestHandler getRequestHandler(String url, AbstractLogger logger) {
        return new HttpRequestHandler(url, logger);
    }

    /**
     * key to save values (custom logger)
     *
     * @param value
     */
    public static ValueKey<String> getValueKey(String value, AbstractLogger logger) {
        return ValueKey.getKey(value, logger);
    }

    /**
     * key to save values
     *
     * @param value
     */
    public static ValueKey<String> getValueKey(String value) {
        return getValueKey(value, new DefaultLogger());
    }

    /**
     * manipulate System.out
     */
    public static OpenPrintStream getPrintStream() throws FileNotFoundException {
        return getPrintStream(new DefaultLogger());
    }

    /**
     * manipulate System.out
     *
     * @param logger
     */
    public static OpenPrintStream getPrintStream(AbstractLogger logger) throws FileNotFoundException {
        return new OpenPrintStream(logger);
    }

    /**
     * print date and clock with data
     *
     * @param time
     */
    public static String getCurrentClockDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * print only date with data
     *
     * @param time
     */
    public static String getCurrentDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * print date and clock
     */
    public static String getCurrentClockDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    /**
     * print only date
     */
    public static String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);
    }

    /**
     * show only clock
     */
    public static String getClockTime() {
        DateFormat dff = new SimpleDateFormat("HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return dff.format(today);
    }

}
