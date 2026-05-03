package org.week.keshi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KeshiDBConnection {
    private static Connection instance;

    private KeshiDBConnection() {}

    public static synchronized Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection("jdbc:sqlite:keshi_tickets.db");
        }
        return instance;
    }
}

