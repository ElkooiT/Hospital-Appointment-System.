package com.hospital.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * CHAPTER 7 (JDBC — Database Programming)
 *
 * Singleton connection manager.
 * Demonstrates:
 *   - Class.forName() driver loading (Ch7)
 *   - DriverManager.getConnection() (Ch7)
 *   - JDBC URL format: jdbc:mysql://host:port/dbname (Ch7)
 *
 * NOTE: Chapter 7 uses the legacy com.mysql.jdbc.Driver.
 * We use com.mysql.cj.jdbc.Driver — the modern, non-deprecated equivalent.
 * The behaviour is identical; the old class was deprecated in Connector/J 6.x
 * and removed in 8.x.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/hospital_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";   // Change to your MySQL root password

    private static Connection connection = null;

    // Private constructor — nobody instantiates this class
    private DBConnection() {}

    /**
     * CHAPTER 7: Returns the shared Connection object.
     * Creates it on first call (lazy initialization).
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // CHAPTER 7: Load the JDBC driver class
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("🔌 Database connected successfully.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
