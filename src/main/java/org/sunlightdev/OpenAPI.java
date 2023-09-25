package org.sunlightdev;

/*

Lukas - 18:36
25.08.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import org.sunlightdev.crypto.CryptoManager;
import org.sunlightdev.database.DatabaseConnector;
import org.sunlightdev.database.form.DatabaseFormular;
import org.sunlightdev.database.type.DatabaseType;
import org.sunlightdev.http.HttpRequestHandler;

public class OpenAPI {

    public static CryptoManager getCryptoManager(String key) {
        return new CryptoManager(key);
    }

    public static DatabaseConnector getDatabaseManager(DatabaseFormular formular, DatabaseType type) {
        return new DatabaseConnector(formular, type);
    }
    
    public static HttpRequestHandler getRequestHandler(String url){
        return new HttpRequestHandler(url);
    }

}
