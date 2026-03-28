package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.ElderlyProfile;
import java.util.List;

public interface ElderlyProfileDAO {
    
    /**
     * Save a new elderly profile
     */
    boolean save(ElderlyProfile elderlyProfile);
    
    /**
     * Find elderly profile by ID
     */
    ElderlyProfile findById(int id);
    
    /**
     * Find elderly profile by full name
     */
    ElderlyProfile findByFullName(String fullName);
    
    /**
     * Get all elderly profiles
     */
    List<ElderlyProfile> findAll();
    
    /**
     * Get all active elderly profiles
     */
    List<ElderlyProfile> findActiveProfiles();
    
    /**
     * Update an existing elderly profile
     */
    boolean update(ElderlyProfile elderlyProfile);
    
    /**
     * Delete an elderly profile by ID
     */
    boolean delete(int id);
    
    /**
     * Find elderly profiles by retirement village
     */
    List<ElderlyProfile> findByRetirementVillage(String retirementVillage);
    
    /**
     * Find elderly profiles by age range
     */
    List<ElderlyProfile> findByAgeRange(int minAge, int maxAge);
    
    /**
     * Find elderly profiles by medical condition
     */
    List<ElderlyProfile> findByMedicalCondition(String medicalCondition);
    
    /**
     * Update session count for an elderly profile
     */
    boolean updateSessionCount(int elderlyId, int sessionCount);
    
    /**
     * Update average rating for an elderly profile
     */
    boolean updateAverageRating(int elderlyId, double averageRating);
    
    /**
     * Search elderly profiles by name (partial match)
     */
    List<ElderlyProfile> searchByName(String searchTerm);
    
    /**
     * Get elderly profiles with most sessions
     */
    List<ElderlyProfile> findTopProfilesBySessions(int limit);
    
    /**
     * Get elderly profiles with highest ratings
     */
    List<ElderlyProfile> findTopProfilesByRating(int limit);
}
