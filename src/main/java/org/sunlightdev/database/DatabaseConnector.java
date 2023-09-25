package org.sunlightdev.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import org.sunlightdev.database.form.DatabaseFormular;
import org.sunlightdev.database.type.DatabaseType;

import java.sql.*;

public class DatabaseConnector {

    private final String ip;
    private final String user;
    private final String password;
    private final String database;
    private final String table;
    private final DatabaseType databaseType;
    private int port;
    private Connection connection;

    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType) {
        this.ip = formular.ip();
        this.user = formular.user();
        this.password = formular.password();
        this.database = formular.database();
        this.table = formular.table();
        this.databaseType = databaseType;

        if (databaseType == DatabaseType.MYSQL) {
            this.port = 3306;
        } else if (databaseType == DatabaseType.POSTGRESQL) {
            this.port = 5432;
        }
    }

    public void connect() {
        System.out.println("Connect to database, with the type \"" + databaseType.name() + "\"!");
        try {
            if (databaseType == DatabaseType.MYSQL) {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                        ip + ":" + port + "/" + database + "?autoReconnect=true" +
                        "&characterEncoding=utf8&useUnicode=true" +
                        "&sessionVariables=storage_engine%3DInnoDB" +
                        "&interactiveClient=true&dontTrackOpenResources=true", user, password);
            } else if (databaseType == DatabaseType.POSTGRESQL) {
                connection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + database +
                        "?reWriteBatchedInserts=true" +
                        "&charSet=utf-8", user, password);
            }
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table: " + table);
    }

    public void close() {
        if (isConnected()) {
            try {
                connection.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getDatabaseStatement(String command, String data) {
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getObject(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void executeDatabaseStatement(String command) {
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (connection == null) {
            return false;
        }
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
