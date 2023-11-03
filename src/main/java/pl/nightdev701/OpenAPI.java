package pl.nightdev701;

/*

Lukas - 18:36
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.base.BaseKey;
import pl.nightdev701.crypto.CryptoManager;
import pl.nightdev701.database.DatabaseConnector;
import pl.nightdev701.database.formular.DatabaseFormular;
import pl.nightdev701.database.type.DatabaseType;
import pl.nightdev701.http.HttpRequestHandler;
import pl.nightdev701.key.UniqueValueKey;
import pl.nightdev701.key.ValueKey;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.util.stream.OpenPrintStream;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class OpenAPI {

    /**
     * encrypt and decrypt strings
     * @param value
     */
    public static CryptoManager getCryptoManager(String value) {
        return new CryptoManager(value);
    }

    /**
     * encrypt and decrypt strings with logger implementation
     * @param logger
     * @param value
     */
    public static CryptoManager getCryptoManager(String value, AbstractLogger logger) {
        return new CryptoManager(value, logger);
    }

    /**
     * manage database connections and command easy
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type) {
        return new DatabaseConnector(formular, type);
    }

    /**
     * manage database connections and command easy with logger implementation
     * @param logger
     * @param formular
     * @param type
     */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type, AbstractLogger logger) {
        return new DatabaseConnector(formular, type, logger);
    }

    /**
     * request http and handle it
     * @param url
     */
    public static HttpRequestHandler getRequestHandler(String url) {
        return new HttpRequestHandler(url);
    }

    /**
     * request http and handle it with logger implementation
     * @param logger
     * @param url
     */
    public static HttpRequestHandler getRequestHandler(String url, AbstractLogger logger) {
        return new HttpRequestHandler(url, logger);
    }

    /**
     * key to save values
     * @param value
     */
    public static BaseKey getValueKey(String value) {
        return ValueKey.getKey(value);
    }

    /**
     * key to save uuid
     * @param value
     */
    public static BaseKey getValueKey(UUID value) {
        return UniqueValueKey.getKey(value);
    }

    /**
     * manipulate System.out
     */
    public static OpenPrintStream getPrintStream() throws FileNotFoundException {
        return new OpenPrintStream();
    }

    /**
     * manipulate System.out
     * @param logger
     */
    public static OpenPrintStream getPrintStream(AbstractLogger logger) throws FileNotFoundException {
        return new OpenPrintStream(logger);
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
