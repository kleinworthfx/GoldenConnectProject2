package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.AuthService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.dao.UserDAO;
import za.ac.cput.goldenconnect.dao.impl.UserDAOImpl;
import za.ac.cput.goldenconnect.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {
    
    private UserDAO userDAO;
    
    public AuthServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User login(String email, String password) {
        // Validate input parameters
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        
        try {
            // Find user by email
            User user = userDAO.findByEmail(email.trim().toLowerCase());
            if (user == null) {
                return null;
            }
            
            // Check if user is active
            if (!user.isActive()) {
                return null;
            }
            
            // Verify password hash
            if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                // Return user object (without password hash for security)
                user.setPasswordHash(null);
                return user;
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean register(User user) {
        try {
            // Validate user data
            if (!validateUserData(user)) {
                return false;
            }
            
            // Check if email already exists
            if (userDAO.existsByEmail(user.getEmail())) {
                return false;
            }
            
            // Hash password
            String hashedPassword = PasswordUtil.hashPassword(user.getPasswordHash());
            user.setPasswordHash(hashedPassword);
            
            // Normalize email
            user.setEmail(user.getEmail().trim().toLowerCase());
            
            // Set default values
            if (user.getRating() == 0.0) {
                user.setRating(0.0);
            }
            if (user.getTotalSessions() == 0) {
                user.setTotalSessions(0);
            }
            user.setActive(true);
            
            // Save user to database
            boolean saved = userDAO.save(user);
            
            if (saved) {
                // TODO: Send confirmation email (implement later)
                System.out.println("User registered successfully: " + user.getEmail());
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
        }
        
        return false;
    }

    @Override
    public boolean logout(int userId) {
        try {
            // For now, just log the logout action
            // TODO: Implement token invalidation when session management is added
            System.out.println("User logged out: " + userId);
            return true;
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            // Validate input
            if (oldPassword == null || newPassword == null || newPassword.length() < 6) {
                return false;
            }
            
            // Find user
            User user = userDAO.findById(userId);
            if (user == null || !user.isActive()) {
                return false;
            }
            
            // Verify old password
            if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
                return false;
            }
            
            // Hash new password
            String hashedNewPassword = PasswordUtil.hashPassword(newPassword);
            user.setPasswordHash(hashedNewPassword);
            
            // Update database
            return userDAO.update(user);
        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email) {
        try {
            // Find user by email
            User user = userDAO.findByEmail(email.trim().toLowerCase());
            if (user == null || !user.isActive()) {
                return false;
            }
            
            // Generate temporary password
            String tempPassword = PasswordUtil.generateSecurePassword(8);
            String hashedTempPassword = PasswordUtil.hashPassword(tempPassword);
            
            // Update user password
            user.setPasswordHash(hashedTempPassword);
            boolean updated = userDAO.update(user);
            
            if (updated) {
                // TODO: Send reset email with temporary password
                System.out.println("Password reset for user: " + email + ", temp password: " + tempPassword);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error resetting password: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean validateToken(String token) {
        // TODO: Implement token validation when session management is added
        // For now, return true if token is not null
        return token != null && !token.trim().isEmpty();
    }

    @Override
    public String generateToken(User user) {
        // TODO: Implement proper JWT or session token generation
        // For now, return a simple token based on user ID and timestamp
        if (user == null) {
            return null;
        }
        
        try {
            String tokenData = user.getId() + ":" + user.getEmail() + ":" + System.currentTimeMillis();
            return PasswordUtil.hashPassword(tokenData);
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isAuthenticated(int userId) {
        try {
            // Check if user exists and is active
            User user = userDAO.findById(userId);
            return user != null && user.isActive();
        } catch (Exception e) {
            System.err.println("Error checking authentication: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean hasRole(int userId, String role) {
        try {
            User user = userDAO.findById(userId);
            return user != null && user.isActive() && role.equals(user.getRole());
        } catch (Exception e) {
            System.err.println("Error checking user role: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateProfile(User user) {
        try {
            // Validate user data (excluding password validation for profile updates)
            if (user == null || user.getId() <= 0) {
                return false;
            }
            
            // Find existing user
            User existingUser = userDAO.findById(user.getId());
            if (existingUser == null) {
                return false;
            }
            
            // Update only profile fields (preserve password and other sensitive data)
            existingUser.setName(user.getName());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setAddress(user.getAddress());
            existingUser.setDateOfBirth(user.getDateOfBirth());
            existingUser.setProfilePicture(user.getProfilePicture());
            existingUser.setSkills(user.getSkills());
            existingUser.setPreferences(user.getPreferences());
            
            return userDAO.update(existingUser);
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deactivateAccount(int userId) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                return false;
            }
            
            user.setActive(false);
            return userDAO.update(user);
        } catch (Exception e) {
            System.err.println("Error deactivating account: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean reactivateAccount(int userId) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                return false;
            }
            
            user.setActive(true);
            return userDAO.update(user);
        } catch (Exception e) {
            System.err.println("Error reactivating account: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Validates user data for registration
     */
    private boolean validateUserData(User user) {
        if (user == null) {
            return false;
        }
        
        // Validate required fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return false;
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }
        
        // Validate email format
        if (!isValidEmail(user.getEmail())) {
            return false;
        }
        
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            return false;
        }
        
        // Validate role values
        if (!isValidRole(user.getRole())) {
            return false;
        }
        
        if (user.getPasswordHash() == null || user.getPasswordHash().length() < 6) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates email format
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * Validates role values
     */
    private boolean isValidRole(String role) {
        return "VOLUNTEER".equals(role) || "ELDERLY".equals(role) || "ADMIN".equals(role);
    }
}
