package org.week.lany;

import java.sql.*;

public class LanyDBConnection {
    private static Connection instance;

    private LanyDBConnection() {}

    public static synchronized Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            instance = DriverManager.getConnection("jdbc:sqlite:lany_tickets.db");
        }
        return instance;
    }


    public static void createUsersTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ")";
        String insertUserSQL = "INSERT OR IGNORE INTO users (username, password) VALUES (?, ?)";
        try {
            Connection conn = getInstance();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
            try (PreparedStatement ps = conn.prepareStatement(insertUserSQL)) {
                String[][] defaultUsers = {
                    {"user1", "pass1"},
                    {"user2", "pass2"},
                    {"user3", "pass3"}
                };
                for (String[] user : defaultUsers) {
                    ps.setString(1, user[0]);
                    ps.setString(2, user[1]);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            Connection conn = getInstance();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try {
            Connection conn = getInstance();
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, password);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
