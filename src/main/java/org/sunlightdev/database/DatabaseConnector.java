package org.sunlightdev.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

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

    public DatabaseConnector(String ip, String user, String password, String database, String table, DatabaseType databaseType) {
        this.ip = ip;
        this.user = user;
        this.password = password;
        this.database = database;
        this.table = table;
        this.databaseType = databaseType;

        if (databaseType == DatabaseType.MYSQL) {
            this.port = 3306;
        } else if (databaseType == DatabaseType.POSTGRESQL) {
            this.port = 5432;
        }
    }

    public void connect() {
        try {
            if (databaseType == DatabaseType.MYSQL) {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                        ip + ":" + 3306 + "/" + database + "?autoReconnect=true" +
                        "&characterEncoding=utf8&useUnicode=true" +
                        "&sessionVariables=storage_engine%3DInnoDB" +
                        "&interactiveClient=true&dontTrackOpenResources=true", user, password);
            } else if (databaseType == DatabaseType.POSTGRESQL) {
                connection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + 5432 + "/" + database +
                        "?reWriteBatchedInserts=true" +
                        "&charSet=utf-8", user, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Table: " + table);
    }

    public void close() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
            throw new RuntimeException(ex);
        }
    }

    public void executeDatabaseStatement(String command) {
        try {
            PreparedStatement statement = connection.prepareStatement(command);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
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
