package pl.nightdev701.database;

/*

Lukas - 18:15
03.09.2023
https://github.com/NightDev701

© SunLightScorpion 2020 - 2023

*/

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import pl.nightdev701.database.formular.DatabaseFormular;
import pl.nightdev701.database.type.DatabaseType;
import pl.nightdev701.logger.AbstractLogger;
import pl.nightdev701.logger.standard.DefaultLogger;

import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseConnector {

    private final String ip;
    private final String user;
    private final String password;
    private final String database;
    private final DatabaseType databaseType;
    private final AbstractLogger logger;
    private final String connectionURL;
    private JdbcPooledConnectionSource connectionSource;

    /**
     * Creates a new instance of the DatabaseConnector
     *
     * @param formular
     * @param databaseType
     */
    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType) {
        this(formular, databaseType, new DefaultLogger());
    }

    /**
     * Creates a new instance of the DatabaseConnector with a custom port
     *
     * @param formular
     * @param databaseType
     * @param port
     */
    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType, int port) {
        this(formular, databaseType, port, new DefaultLogger());
    }


    /**
     * Creates a new instance of the DatabaseConnector with a custom logger
     *
     * @param formular
     * @param databaseType
     * @param logger
     */
    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType, AbstractLogger logger) {
        this(formular, databaseType, databaseType.getDefaultPort(), logger);
    }

    /**
     * Creates a new instance of the DatabaseConnector with  a custom port and logger
     *
     * @param formular
     * @param databaseType
     * @param port
     * @param logger
     */
    public DatabaseConnector(DatabaseFormular formular, DatabaseType databaseType, int port, AbstractLogger logger) {
        this.ip = formular.ip();
        this.user = formular.user();
        this.password = formular.password();
        this.database = formular.database();
        this.databaseType = databaseType;

        this.logger = logger;

        if (databaseType == DatabaseType.MYSQL) {
            connectionURL = "jdbc:mysql://" +
                    ip + ":" + port + "/" + database + "?autoReconnect=true" +
                    "&characterEncoding=utf8&useUnicode=true" +
                    "&sessionVariables=storage_engine%3DInnoDB" +
                    "&interactiveClient=true&dontTrackOpenResources=true";
        } else if (databaseType == DatabaseType.POSTGRESQL) {
            connectionURL = "jdbc:postgresql://" + ip + ":" + port + "/" + database +
                    "?reWriteBatchedInserts=true" +
                    "&charSet=utf-8";
        } else {
            connectionURL = null;
        }
    }

    /**
     * initialize connection
     */
    public void initialize() throws SQLException {
        logger.log(Level.INFO, "Initializing database connection, with the type \"" + databaseType.name() + "\"!");

        connectionSource = new JdbcPooledConnectionSource(connectionURL);
        connectionSource.setUsername(user);
        connectionSource.setPassword(password);
    }

    /**
     * close connection
     */
    public void close() throws Exception {
        connectionSource.close();
        logger.log(Level.INFO, "Connection closed");
    }

    /**
     * Creates a table for a DatabaseTable class if no table already exists with the same name
     *
     * @param dataClass your DatabaseTable class
     */
    public <T> int createTableIfNotExists(Class<T> dataClass) throws SQLException {
        return TableUtils.createTableIfNotExists(connectionSource, dataClass);
    }

    /**
     * Creates a new Dao with which you can work
     *
     * @param clazz your DatabaseTable class
     * @return new Dao
     * @throws SQLException
     */
    public <D extends Dao<T, ?>, T> D createDao(Class<T> clazz) throws SQLException {
        return DaoManager.createDao(connectionSource, clazz);
    }

}
