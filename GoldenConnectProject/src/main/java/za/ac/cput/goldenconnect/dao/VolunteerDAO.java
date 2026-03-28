package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.Volunteer;
import java.util.List;

public interface VolunteerDAO {
    
    /**
     * Save a new volunteer
     */
    boolean save(Volunteer volunteer);
    
    /**
     * Find volunteer by ID
     */
    Volunteer findById(int id);
    
    /**
     * Find volunteer by email
     */
    Volunteer findByEmail(String email);
    
    /**
     * Find volunteer by full name
     */
    Volunteer findByFullName(String fullName);
    
    /**
     * Get all volunteers
     */
    List<Volunteer> findAll();
    
    /**
     * Get all active volunteers
     */
    List<Volunteer> findActiveVolunteers();
    
    /**
     * Update an existing volunteer
     */
    boolean update(Volunteer volunteer);
    
    /**
     * Delete a volunteer by ID
     */
    boolean delete(int id);
    
    /**
     * Find volunteers by skills
     */
    List<Volunteer> findBySkills(String skills);
    
    /**
     * Find volunteers by experience level
     */
    List<Volunteer> findByExperienceLevel(String experienceLevel);
    
    /**
     * Find volunteers by availability
     */
    List<Volunteer> findByAvailability(String availability);
    
    /**
     * Update session count for a volunteer
     */
    boolean updateSessionCount(int volunteerId, int sessionCount);
    
    /**
     * Update average rating for a volunteer
     */
    boolean updateAverageRating(int volunteerId, double averageRating);
    
    /**
     * Update total hours for a volunteer
     */
    boolean updateTotalHours(int volunteerId, int totalHours);
    
    /**
     * Update last active date for a volunteer
     */
    boolean updateLastActiveDate(int volunteerId);
    
    /**
     * Search volunteers by name (partial match)
     */
    List<Volunteer> searchByName(String searchTerm);
    
    /**
     * Search volunteers by email (partial match)
     */
    List<Volunteer> searchByEmail(String searchTerm);
    
    /**
     * Get volunteers with most sessions
     */
    List<Volunteer> findTopVolunteersBySessions(int limit);
    
    /**
     * Get volunteers with highest ratings
     */
    List<Volunteer> findTopVolunteersByRating(int limit);
    
    /**
     * Get volunteers with most hours
     */
    List<Volunteer> findTopVolunteersByHours(int limit);
    
    /**
     * Get volunteers registered in date range
     */
    List<Volunteer> findByRegistrationDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    /**
     * Get volunteers by certification
     */
    List<Volunteer> findByCertification(String certification);
    
    /**
     * Get volunteers who completed background check
     */
    List<Volunteer> findWithBackgroundCheck();
    
    /**
     * Get volunteers who completed training
     */
    List<Volunteer> findWithTrainingCompleted();
}
