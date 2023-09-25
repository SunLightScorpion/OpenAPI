package org.sunlightdev.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import org.sunlightdev.database.type.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private final String ip;
    private final String user;
    private final String password;
    private final String database;
    private final String table;
    private final DatabaseType databaseType;
    private Connection connection;

    public DatabaseConnector(String ip, String user, String password, String database, String table, DatabaseType databaseType){
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.database = database;
        this.table = table;
        this.databaseType = databaseType;
    }

    public void connect(){

        try {
            if(databaseType == DatabaseType.MYSQL){
                connection = DriverManager.getConnection("jdbc:mysql://" +
                        ip + ":" + 3306 + "/" + database + "?autoReconnect=true" +
                        "&characterEncoding=utf8&useUnicode=true" +
                        "&sessionVariables=storage_engine%3DInnoDB" +
                        "&interactiveClient=true&dontTrackOpenResources=true", user, password);
            } else if(databaseType == DatabaseType.POSTGRESQL){
                connection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + 5432 + "/" + database +
                        "?reWriteBatchedInserts=true" +
                        "&charSet=utf-8", user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected(){
        return connection != null;
    }

}
