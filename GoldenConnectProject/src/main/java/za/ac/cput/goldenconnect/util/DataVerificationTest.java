package za.ac.cput.goldenconnect.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataVerificationTest {
    
    public static void main(String[] args) {
        System.out.println("=== GoldenConnect Data Verification Test ===");
        
        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            Connection conn = DatabaseConnectionManager.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful!");
            } else {
                System.out.println("✗ Database connection failed!");
                return;
            }
            
            // Check if users table has data
            System.out.println("\n2. Checking users table...");
            String userQuery = "SELECT COUNT(*) as count FROM users";
            try (PreparedStatement stmt = conn.prepareStatement(userQuery);
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    int userCount = rs.getInt("count");
                    System.out.println("✓ Users table has " + userCount + " records");
                    
                    if (userCount > 0) {
                        // Show sample users
                        System.out.println("\n3. Sample users in database:");
                        String sampleQuery = "SELECT id, name, email, role FROM users LIMIT 5";
                        try (PreparedStatement sampleStmt = conn.prepareStatement(sampleQuery);
                             ResultSet sampleRs = sampleStmt.executeQuery()) {
                            
                            while (sampleRs.next()) {
                                System.out.println("   ID: " + sampleRs.getInt("id") + 
                                                 " | Name: " + sampleRs.getString("name") + 
                                                 " | Email: " + sampleRs.getString("email") + 
                                                 " | Role: " + sampleRs.getString("role"));
                            }
                        }
                    }
                }
            }
            
            // Check if requests table has data
            System.out.println("\n4. Checking requests table...");
            String requestQuery = "SELECT COUNT(*) as count FROM requests";
            try (PreparedStatement stmt = conn.prepareStatement(requestQuery);
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    int requestCount = rs.getInt("count");
                    System.out.println("✓ Requests table has " + requestCount + " records");
                }
            }
            
            // Check if activities table has data
            System.out.println("\n5. Checking activities table...");
            String activityQuery = "SELECT COUNT(*) as count FROM activities";
            try (PreparedStatement stmt = conn.prepareStatement(activityQuery);
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    int activityCount = rs.getInt("count");
                    System.out.println("✓ Activities table has " + activityCount + " records");
                }
            }
            
            // Test login credentials
            System.out.println("\n6. Testing sample login credentials...");
            String loginQuery = "SELECT id, name, email, role, password_hash FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(loginQuery)) {
                stmt.setString(1, "john.smith@email.com");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("✓ Sample user found:");
                        System.out.println("   Name: " + rs.getString("name"));
                        System.out.println("   Email: " + rs.getString("email"));
                        System.out.println("   Role: " + rs.getString("role"));
                        System.out.println("   Password Hash: " + rs.getString("password_hash"));
                    } else {
                        System.out.println("✗ Sample user not found!");
                    }
                }
            }
            
            System.out.println("\n=== Data Verification Complete ===");
            System.out.println("✓ Database is ready for login/registration testing!");
            
        } catch (SQLException e) {
            System.err.println("Data verification failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
