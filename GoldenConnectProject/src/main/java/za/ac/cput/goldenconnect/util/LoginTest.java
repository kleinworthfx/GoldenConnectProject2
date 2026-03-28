package za.ac.cput.goldenconnect.util;

import za.ac.cput.goldenconnect.service.AuthService;
import za.ac.cput.goldenconnect.service.impl.AuthServiceImpl;
import za.ac.cput.goldenconnect.model.User;

public class LoginTest {
    
    public static void main(String[] args) {
        System.out.println("=== GoldenConnect Login Test ===");
        
        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            if (DatabaseConnectionManager.testConnection()) {
                System.out.println("✓ Database connection successful!");
            } else {
                System.out.println("✗ Database connection failed!");
                return;
            }
            
            // Test AuthService
            System.out.println("\n2. Testing AuthService...");
            AuthService authService = new AuthServiceImpl();
            
            // Test login with sample user from database
            System.out.println("3. Testing login with sample user...");
            User user = authService.login("john.smith@email.com", "password123");
            
            if (user != null) {
                System.out.println("✓ Login successful!");
                System.out.println("   User: " + user.getName());
                System.out.println("   Email: " + user.getEmail());
                System.out.println("   Role: " + user.getRole());
            } else {
                System.out.println("✗ Login failed!");
            }
            
            // Test registration
            System.out.println("\n4. Testing registration...");
            User newUser = new User();
            newUser.setName("Test User");
            newUser.setEmail("test.user@example.com");
            newUser.setRole("VOLUNTEER");
            newUser.setPasswordHash("testpassword123");
            
            boolean registrationSuccess = authService.register(newUser);
            if (registrationSuccess) {
                System.out.println("✓ Registration successful!");
                
                // Test login with new user
                User loginUser = authService.login("test.user@example.com", "testpassword123");
                if (loginUser != null) {
                    System.out.println("✓ New user login successful!");
                } else {
                    System.out.println("✗ New user login failed!");
                }
            } else {
                System.out.println("✗ Registration failed!");
            }
            
            System.out.println("\n=== Test Complete ===");
            
        } catch (Exception e) {
            System.err.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
