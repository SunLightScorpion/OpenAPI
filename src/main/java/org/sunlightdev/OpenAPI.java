package org.sunlightdev;

/*

Lukas - 18:36
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import org.sunlightdev.crypto.CryptoManager;
import org.sunlightdev.database.DatabaseConnector;
import org.sunlightdev.database.formular.DatabaseFormular;
import org.sunlightdev.database.type.DatabaseType;
import org.sunlightdev.http.HttpRequestHandler;
import org.sunlightdev.key.UniqueValueKey;
import org.sunlightdev.key.ValueKey;

import java.util.UUID;

public class OpenAPI {

    /* encrypt and decrypt strings */
    public static CryptoManager getCryptoManager(String key) {
        return new CryptoManager(key);
    }

    /* manage database connections and command easy */
    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type) {
        return new DatabaseConnector(formular, type);
    }

    /* request http and handle it */
    public static HttpRequestHandler getRequestHandler(String url) {
        return new HttpRequestHandler(url);
    }

    /* key to save values */
    public static ValueKey<String> getValueKey(String value){
        return ValueKey.getKey(value);
    }

    /* key to save uuid */
    public static UniqueValueKey<UUID> getValueKey(UUID value){
        return UniqueValueKey.getKey(value);
    }

}
