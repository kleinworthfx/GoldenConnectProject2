package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.User;

public interface AuthService {
    User login(String email, String password);
    boolean register(User user);
    boolean logout(int userId);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    boolean resetPassword(String email);
    boolean validateToken(String token);
    String generateToken(User user);
    boolean isAuthenticated(int userId);
    boolean hasRole(int userId, String role);
    boolean updateProfile(User user);
    boolean deactivateAccount(int userId);
    boolean reactivateAccount(int userId);
}
