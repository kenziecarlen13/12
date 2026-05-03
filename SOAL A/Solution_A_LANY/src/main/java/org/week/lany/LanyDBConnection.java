package org.week.lany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LanyDBConnection {
    private static Connection instance;

    private LanyDBConnection() {}

    public static synchronized Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection("jdbc:sqlite:lany_tickets.db");
        }
        return instance;
    }
}

