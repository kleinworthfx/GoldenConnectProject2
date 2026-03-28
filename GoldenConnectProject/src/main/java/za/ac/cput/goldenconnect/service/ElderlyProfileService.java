package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.ElderlyProfile;
import java.util.List;

public interface ElderlyProfileService {
    
    /**
     * Create a new elderly profile
     */
    ElderlyProfile createProfile(ElderlyProfile profile);
    
    /**
     * Get elderly profile by ID
     */
    ElderlyProfile getProfileById(int id);
    
    /**
     * Get elderly profile by full name
     */
    ElderlyProfile getProfileByName(String fullName);
    
    /**
     * Get all elderly profiles
     */
    List<ElderlyProfile> getAllProfiles();
    
    /**
     * Get all active elderly profiles
     */
    List<ElderlyProfile> getActiveProfiles();
    
    /**
     * Update an existing elderly profile
     */
    ElderlyProfile updateProfile(ElderlyProfile profile);
    
    /**
     * Delete an elderly profile
     */
    boolean deleteProfile(int id);
    
    /**
     * Search profiles by name
     */
    List<ElderlyProfile> searchProfilesByName(String searchTerm);
    
    /**
     * Get profiles by retirement village
     */
    List<ElderlyProfile> getProfilesByRetirementVillage(String retirementVillage);
    
    /**
     * Get profiles by age range
     */
    List<ElderlyProfile> getProfilesByAgeRange(int minAge, int maxAge);
    
    /**
     * Get profiles by medical condition
     */
    List<ElderlyProfile> getProfilesByMedicalCondition(String medicalCondition);
    
    /**
     * Update session count for a profile
     */
    boolean updateSessionCount(int elderlyId, int sessionCount);
    
    /**
     * Update average rating for a profile
     */
    boolean updateAverageRating(int elderlyId, double averageRating);
    
    /**
     * Get top profiles by session count
     */
    List<ElderlyProfile> getTopProfilesBySessions(int limit);
    
    /**
     * Get top profiles by rating
     */
    List<ElderlyProfile> getTopProfilesByRating(int limit);
    
    /**
     * Validate elderly profile data
     */
    boolean validateProfile(ElderlyProfile profile);
    
    /**
     * Get profile statistics
     */
    ProfileStatistics getProfileStatistics();
    
    /**
     * Inner class for profile statistics
     */
    class ProfileStatistics {
        private int totalProfiles;
        private int activeProfiles;
        private int inactiveProfiles;
        private double averageAge;
        private int totalSessions;
        private double averageRating;
        
        // Constructor
        public ProfileStatistics() {}
        
        // Getters and Setters
        public int getTotalProfiles() { return totalProfiles; }
        public void setTotalProfiles(int totalProfiles) { this.totalProfiles = totalProfiles; }
        
        public int getActiveProfiles() { return activeProfiles; }
        public void setActiveProfiles(int activeProfiles) { this.activeProfiles = activeProfiles; }
        
        public int getInactiveProfiles() { return inactiveProfiles; }
        public void setInactiveProfiles(int inactiveProfiles) { this.inactiveProfiles = inactiveProfiles; }
        
        public double getAverageAge() { return averageAge; }
        public void setAverageAge(double averageAge) { this.averageAge = averageAge; }
        
        public int getTotalSessions() { return totalSessions; }
        public void setTotalSessions(int totalSessions) { this.totalSessions = totalSessions; }
        
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    }
}
