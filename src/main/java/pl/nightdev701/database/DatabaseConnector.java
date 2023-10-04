package pl.nightdev701.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import pl.nightdev701.database.formular.DatabaseFormular;
import pl.nightdev701.database.type.DatabaseType;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;

import java.sql.*;
import java.util.logging.Level;

public class DatabaseConnector {

    private static int port;
    private final String ip;
    private final String user;
    private final String password;
    private final String database;
    private final DatabaseType databaseType;
    private final AbstractLogger logger;
    private Connection connection;

    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType) {
        this.ip = formular.ip();
        this.user = formular.user();
        this.password = formular.password();
        this.database = formular.database();
        this.databaseType = databaseType;

        this.logger = new DefaultLogger();

        if (databaseType == DatabaseType.MYSQL) {
            setPort(3306);
        } else if (databaseType == DatabaseType.POSTGRESQL) {
            setPort(5432);
        }
    }

    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType, AbstractLogger logger) {
        this.ip = formular.ip();
        this.user = formular.user();
        this.password = formular.password();
        this.database = formular.database();
        this.databaseType = databaseType;

        this.logger = logger;

        if (databaseType == DatabaseType.MYSQL) {
            setPort(3306);
        } else if (databaseType == DatabaseType.POSTGRESQL) {
            setPort(5432);
        }
    }

    /**
     * connect to database
     */
    public void connect() throws SQLException {
        logger.log(Level.INFO, "Connect to database, with the type \"" + databaseType.name() + "\"!");
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
        logger.log(Level.INFO, "Connected to database!");
    }

    /**
     * close connection
     */
    public void close() throws SQLException {
        if (isConnected()) {
            connection.close();
            logger.log(Level.INFO, "Connection closed");
        }
    }

    /**
     * read value from database
     */
    public Object getDatabaseStatement(String command, String data) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(command);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getObject(data);
    }

    /**
     * execute action to database
     */
    public void executeDatabaseStatement(String command) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(command);
        statement.executeUpdate();
    }

    /**
     * check if connected
     */
    public boolean isConnected() throws SQLException {
        if (connection == null) {
            return false;
        }
        return !connection.isClosed();
    }

    /**
     * get port
     */
    public int getPort() {
        return port;
    }

    /**
     * set port
     */
    public void setPort(int port) {
        this.port = port;
    }

}
