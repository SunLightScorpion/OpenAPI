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

import java.util.UUID;

public class OpenAPI {

    /** encrypt and decrypt strings */
    public static CryptoManager getCryptoManager(String value) {
        return new CryptoManager(value);
    }

    /** manage database connections and command easy */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type) {
        return new DatabaseConnector(formular, type);
    }

    /** request http and handle it */
    public static HttpRequestHandler getRequestHandler(String url) {
        return new HttpRequestHandler(url);
    }

    /** key to save values */
    public static BaseKey getValueKey(String value){
        return ValueKey.getKey(value);
    }

    /** key to save uuid */
    public static BaseKey getValueKey(UUID value){
        return UniqueValueKey.getKey(value);
    }

}
