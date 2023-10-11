package pl.nightdev701.database.type;

/* database types */
public enum DatabaseType {

    MYSQL,
    POSTGRESQL,
    ;

    /**
     * Gets the default port for the database type.
     *
     * @return the default port for the database type (3306 for MySQL, 5432 for PostgresSQL).
     */
    public int getDefaultPort() {
        int port = -1;

        if (this == MYSQL) {
            port = 3306;
        } else if (this == POSTGRESQL) {
            port = 5432;
        }

        return port;
    }
}
