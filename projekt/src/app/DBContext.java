package app;

import java.sql.Connection;

public class DBContext {

    private static Connection connection;

    public static void setConnection(Connection connection) {
        if (connection == null) {
            throw new NullPointerException("connection cannot be null");
        }

        DBContext.connection = connection;
    }

    public static Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("connection must be set before calling this method");
        }

        return connection;
    }

    public static void clear() {
        connection = null;
    }

}