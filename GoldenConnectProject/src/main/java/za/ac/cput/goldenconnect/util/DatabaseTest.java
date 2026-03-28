package za.ac.cput.goldenconnect.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("Testing GoldenConnect Database Connection...");
        
        try {
            // Test basic connection
            Connection conn = DatabaseConnectionManager.getConnection();
            System.out.println("✅ Database connection successful!");
            
            // Test query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            
            if (rs.next()) {
                int userCount = rs.getInt("count");
                System.out.println("✅ Database query successful! Found " + userCount + " users.");
            }
            
            rs.close();
            stmt.close();
            DatabaseConnectionManager.closeConnection();
            
            System.out.println("✅ All database tests passed!");
            
        } catch (Exception e) {
            System.err.println("❌ Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
