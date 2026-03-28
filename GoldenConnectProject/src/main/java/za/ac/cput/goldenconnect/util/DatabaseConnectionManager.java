package za.ac.cput.goldenconnect.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    // Database configuration with environment variable support
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/goldenconnect";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "password"; // Generic default
    
    // Environment variable names
    private static final String DB_URL_ENV = "GOLDENCONNECT_DB_URL";
    private static final String DB_USERNAME_ENV = "GOLDENCONNECT_DB_USERNAME";
    private static final String DB_PASSWORD_ENV = "GOLDENCONNECT_DB_PASSWORD";
    
    private static Connection connection = null;
    
    // Get database URL from environment or use default
    private static String getDatabaseUrl() {
        String envUrl = System.getenv(DB_URL_ENV);
        return envUrl != null ? envUrl : DEFAULT_URL;
    }
    
    // Get database username from environment or use default
    private static String getDatabaseUsername() {
        String envUsername = System.getenv(DB_USERNAME_ENV);
        return envUsername != null ? envUsername : DEFAULT_USERNAME;
    }
    
    // Get database password from environment or use default
    private static String getDatabasePassword() {
        String envPassword = System.getenv(DB_PASSWORD_ENV);
        return envPassword != null ? envPassword : DEFAULT_PASSWORD;
    }
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = getDatabaseUrl();
                String username = getDatabaseUsername();
                String password = getDatabasePassword();
                
                System.out.println("Connecting to database: " + url);
                System.out.println("Username: " + username);
                
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connection established successfully!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection testConn = getConnection()) {
            return testConn != null && !testConn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    // Method to print current database configuration (for debugging)
    public static void printDatabaseConfig() {
        System.out.println("=== Database Configuration ===");
        System.out.println("URL: " + getDatabaseUrl());
        System.out.println("Username: " + getDatabaseUsername());
        System.out.println("Password: [HIDDEN]");
        System.out.println("===============================");
    }
}
