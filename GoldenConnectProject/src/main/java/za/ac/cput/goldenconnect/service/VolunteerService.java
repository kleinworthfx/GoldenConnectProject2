package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Volunteer;
import java.util.List;

public interface VolunteerService {
    
    /**
     * Create a new volunteer
     */
    Volunteer createVolunteer(Volunteer volunteer);
    
    /**
     * Get volunteer by ID
     */
    Volunteer getVolunteerById(int id);
    
    /**
     * Get volunteer by email
     */
    Volunteer getVolunteerByEmail(String email);
    
    /**
     * Get volunteer by full name
     */
    Volunteer getVolunteerByName(String fullName);
    
    /**
     * Get all volunteers
     */
    List<Volunteer> getAllVolunteers();
    
    /**
     * Get all active volunteers
     */
    List<Volunteer> getActiveVolunteers();
    
    /**
     * Update an existing volunteer
     */
    Volunteer updateVolunteer(Volunteer volunteer);
    
    /**
     * Delete a volunteer
     */
    boolean deleteVolunteer(int id);
    
    /**
     * Search volunteers by name
     */
    List<Volunteer> searchVolunteersByName(String searchTerm);
    
    /**
     * Search volunteers by email
     */
    List<Volunteer> searchVolunteersByEmail(String searchTerm);
    
    /**
     * Get volunteers by skills
     */
    List<Volunteer> getVolunteersBySkills(String skills);
    
    /**
     * Get volunteers by experience level
     */
    List<Volunteer> getVolunteersByExperienceLevel(String experienceLevel);
    
    /**
     * Get volunteers by availability
     */
    List<Volunteer> getVolunteersByAvailability(String availability);
    
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
     * Get top volunteers by session count
     */
    List<Volunteer> getTopVolunteersBySessions(int limit);
    
    /**
     * Get top volunteers by rating
     */
    List<Volunteer> getTopVolunteersByRating(int limit);
    
    /**
     * Get top volunteers by hours
     */
    List<Volunteer> getTopVolunteersByHours(int limit);
    
    /**
     * Get volunteers registered in date range
     */
    List<Volunteer> getVolunteersByRegistrationDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    
    /**
     * Get volunteers by certification
     */
    List<Volunteer> getVolunteersByCertification(String certification);
    
    /**
     * Get volunteers who completed background check
     */
    List<Volunteer> getVolunteersWithBackgroundCheck();
    
    /**
     * Get volunteers who completed training
     */
    List<Volunteer> getVolunteersWithTrainingCompleted();
    
    /**
     * Validate volunteer data
     */
    boolean validateVolunteer(Volunteer volunteer);
    
    /**
     * Get volunteer statistics
     */
    VolunteerStatistics getVolunteerStatistics();
    
    /**
     * Inner class for volunteer statistics
     */
    class VolunteerStatistics {
        private int totalVolunteers;
        private int activeVolunteers;
        private int inactiveVolunteers;
        private double averageAge;
        private int totalSessionsCompleted;
        private double averageRating;
        private int totalHours;
        private int volunteersWithBackgroundCheck;
        private int volunteersWithTraining;
        
        // Constructor
        public VolunteerStatistics() {}
        
        // Getters and Setters
        public int getTotalVolunteers() { return totalVolunteers; }
        public void setTotalVolunteers(int totalVolunteers) { this.totalVolunteers = totalVolunteers; }
        
        public int getActiveVolunteers() { return activeVolunteers; }
        public void setActiveVolunteers(int activeVolunteers) { this.activeVolunteers = activeVolunteers; }
        
        public int getInactiveVolunteers() { return inactiveVolunteers; }
        public void setInactiveVolunteers(int inactiveVolunteers) { this.inactiveVolunteers = inactiveVolunteers; }
        
        public double getAverageAge() { return averageAge; }
        public void setAverageAge(double averageAge) { this.averageAge = averageAge; }
        
        public int getTotalSessionsCompleted() { return totalSessionsCompleted; }
        public void setTotalSessionsCompleted(int totalSessionsCompleted) { this.totalSessionsCompleted = totalSessionsCompleted; }
        
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
        
        public int getTotalHours() { return totalHours; }
        public void setTotalHours(int totalHours) { this.totalHours = totalHours; }
        
        public int getVolunteersWithBackgroundCheck() { return volunteersWithBackgroundCheck; }
        public void setVolunteersWithBackgroundCheck(int volunteersWithBackgroundCheck) { this.volunteersWithBackgroundCheck = volunteersWithBackgroundCheck; }
        
        public int getVolunteersWithTraining() { return volunteersWithTraining; }
        public void setVolunteersWithTraining(int volunteersWithTraining) { this.volunteersWithTraining = volunteersWithTraining; }
    }
}
