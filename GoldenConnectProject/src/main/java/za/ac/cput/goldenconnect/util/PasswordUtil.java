package za.ac.cput.goldenconnect.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * Hash a password using SHA-256
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify a password against its hash
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hash);
    }
    
    /**
     * Generate a random salt
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash password with salt
     */
    public static String hashPasswordWithSalt(String password, String salt) {
        return hashPassword(password + salt);
    }
    
    /**
     * Verify password with salt
     */
    public static boolean verifyPasswordWithSalt(String password, String hash, String salt) {
        String hashedInput = hashPasswordWithSalt(password, salt);
        return hashedInput.equals(hash);
    }
    
    /**
     * Generate a secure random password
     */
    public static String generateSecurePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}
