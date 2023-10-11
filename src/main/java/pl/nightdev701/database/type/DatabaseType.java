package pl.nightdev701.database.type;

/* database types */
public enum DatabaseType {

    MYSQL(3306),
    POSTGRESQL(5432),
    ;

    private final int defaultPort;

    DatabaseType(int defaultPort) {
        this.defaultPort = defaultPort;
    }

    /**
     * Gets the default port for a database type
     * @return defaultPort
     */
    public int getDefaultPort() {
        return defaultPort;
    }
}
