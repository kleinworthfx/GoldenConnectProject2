package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.User;
import java.util.List;

public interface UserDAO {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findByRole(String role);
    List<User> findActiveUsers();
    boolean save(User user);
    boolean update(User user);
    boolean delete(int id);
    boolean existsByEmail(String email);
    List<User> findVolunteersBySkills(String skills);
    List<User> findElderlyByPreferences(String preferences);
    boolean updateRating(int userId, double rating);
    boolean incrementTotalSessions(int userId);
}
