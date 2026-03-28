package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.ElderlyProfileService;
import za.ac.cput.goldenconnect.dao.ElderlyProfileDAO;
import za.ac.cput.goldenconnect.dao.impl.ElderlyProfileDAOImpl;
import za.ac.cput.goldenconnect.model.ElderlyProfile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ElderlyProfileServiceImpl implements ElderlyProfileService {
    
    private ElderlyProfileDAO elderlyProfileDAO;
    
    public ElderlyProfileServiceImpl() {
        this.elderlyProfileDAO = new ElderlyProfileDAOImpl();
    }
    
    @Override
    public ElderlyProfile createProfile(ElderlyProfile profile) {
        try {
            // Validate profile data
            if (!validateProfile(profile)) {
                throw new IllegalArgumentException("Invalid profile data");
            }
            
            // Check if profile with same name already exists
            ElderlyProfile existingProfile = elderlyProfileDAO.findByFullName(profile.getFullName());
            if (existingProfile != null) {
                throw new IllegalArgumentException("Profile with this name already exists");
            }
            
            // Set timestamps
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            
            // Save to database
            boolean saved = elderlyProfileDAO.save(profile);
            if (saved) {
                return profile;
            } else {
                throw new RuntimeException("Failed to save profile to database");
            }
        } catch (Exception e) {
            System.err.println("Error creating elderly profile: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public ElderlyProfile getProfileById(int id) {
        try {
            return elderlyProfileDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Error getting profile by ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public ElderlyProfile getProfileByName(String fullName) {
        try {
            return elderlyProfileDAO.findByFullName(fullName);
        } catch (Exception e) {
            System.err.println("Error getting profile by name: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<ElderlyProfile> getAllProfiles() {
        try {
            return elderlyProfileDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all profiles: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<ElderlyProfile> getActiveProfiles() {
        try {
            return elderlyProfileDAO.findActiveProfiles();
        } catch (Exception e) {
            System.err.println("Error getting active profiles: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public ElderlyProfile updateProfile(ElderlyProfile profile) {
        try {
            // Validate profile data
            if (!validateProfile(profile)) {
                throw new IllegalArgumentException("Invalid profile data");
            }
            
            // Check if profile exists
            ElderlyProfile existingProfile = elderlyProfileDAO.findById(profile.getId());
            if (existingProfile == null) {
                throw new IllegalArgumentException("Profile not found");
            }
            
            // Check if name is being changed and if new name already exists
            if (!existingProfile.getFullName().equals(profile.getFullName())) {
                ElderlyProfile profileWithNewName = elderlyProfileDAO.findByFullName(profile.getFullName());
                if (profileWithNewName != null && profileWithNewName.getId() != profile.getId()) {
                    throw new IllegalArgumentException("Profile with this name already exists");
                }
            }
            
            // Update timestamp
            profile.setUpdatedAt(LocalDateTime.now());
            
            // Update in database
            boolean updated = elderlyProfileDAO.update(profile);
            if (updated) {
                return profile;
            } else {
                throw new RuntimeException("Failed to update profile in database");
            }
        } catch (Exception e) {
            System.err.println("Error updating elderly profile: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteProfile(int id) {
        try {
            // Check if profile exists
            ElderlyProfile existingProfile = elderlyProfileDAO.findById(id);
            if (existingProfile == null) {
                throw new IllegalArgumentException("Profile not found");
            }
            
            return elderlyProfileDAO.delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting elderly profile: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<ElderlyProfile> searchProfilesByName(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllProfiles();
            }
            return elderlyProfileDAO.searchByName(searchTerm.trim());
        } catch (Exception e) {
            System.err.println("Error searching profiles by name: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<ElderlyProfile> getProfilesByRetirementVillage(String retirementVillage) {
        try {
            if (retirementVillage == null || retirementVillage.trim().isEmpty()) {
                return getAllProfiles();
            }
            return elderlyProfileDAO.findByRetirementVillage(retirementVillage.trim());
        } catch (Exception e) {
            System.err.println("Error getting profiles by retirement village: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<ElderlyProfile> getProfilesByAgeRange(int minAge, int maxAge) {
        try {
            if (minAge < 0 || maxAge < 0 || minAge > maxAge) {
                throw new IllegalArgumentException("Invalid age range");
            }
            return elderlyProfileDAO.findByAgeRange(minAge, maxAge);
        } catch (Exception e) {
            System.err.println("Error getting profiles by age range: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<ElderlyProfile> getProfilesByMedicalCondition(String medicalCondition) {
        try {
            if (medicalCondition == null || medicalCondition.trim().isEmpty()) {
                return getAllProfiles();
            }
            return elderlyProfileDAO.findByMedicalCondition(medicalCondition.trim());
        } catch (Exception e) {
            System.err.println("Error getting profiles by medical condition: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean updateSessionCount(int elderlyId, int sessionCount) {
        try {
            if (sessionCount < 0) {
                throw new IllegalArgumentException("Session count cannot be negative");
            }
            return elderlyProfileDAO.updateSessionCount(elderlyId, sessionCount);
        } catch (Exception e) {
            System.err.println("Error updating session count: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateAverageRating(int elderlyId, double averageRating) {
        try {
            if (averageRating < 0 || averageRating > 5) {
                throw new IllegalArgumentException("Average rating must be between 0 and 5");
            }
            return elderlyProfileDAO.updateAverageRating(elderlyId, averageRating);
        } catch (Exception e) {
            System.err.println("Error updating average rating: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<ElderlyProfile> getTopProfilesBySessions(int limit) {
        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be positive");
            }
            return elderlyProfileDAO.findTopProfilesBySessions(limit);
        } catch (Exception e) {
            System.err.println("Error getting top profiles by sessions: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<ElderlyProfile> getTopProfilesByRating(int limit) {
        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be positive");
            }
            return elderlyProfileDAO.findTopProfilesByRating(limit);
        } catch (Exception e) {
            System.err.println("Error getting top profiles by rating: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean validateProfile(ElderlyProfile profile) {
        if (profile == null) {
            return false;
        }
        
        // Validate required fields
        if (profile.getFullName() == null || profile.getFullName().trim().isEmpty()) {
            return false;
        }
        
        if (profile.getAge() < 65 || profile.getAge() > 120) {
            return false;
        }
        
        if (profile.getMedicalCondition() == null || profile.getMedicalCondition().trim().isEmpty()) {
            return false;
        }
        
        if (profile.getRetirementVillage() == null || profile.getRetirementVillage().trim().isEmpty()) {
            return false;
        }
        
        // Validate phone number format (optional field)
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().trim().isEmpty()) {
            String phoneRegex = "^[+]?[0-9\\s\\-\\(\\)]{10,}$";
            if (!profile.getPhoneNumber().matches(phoneRegex)) {
                return false;
            }
        }
        
        // Validate emergency contact phone (optional field)
        if (profile.getEmergencyContactPhone() != null && !profile.getEmergencyContactPhone().trim().isEmpty()) {
            String phoneRegex = "^[+]?[0-9\\s\\-\\(\\)]{10,}$";
            if (!profile.getEmergencyContactPhone().matches(phoneRegex)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public ProfileStatistics getProfileStatistics() {
        try {
            List<ElderlyProfile> allProfiles = getAllProfiles();
            ProfileStatistics stats = new ProfileStatistics();
            
            if (allProfiles.isEmpty()) {
                return stats;
            }
            
            stats.setTotalProfiles(allProfiles.size());
            stats.setActiveProfiles((int) allProfiles.stream().filter(ElderlyProfile::isActive).count());
            stats.setInactiveProfiles(stats.getTotalProfiles() - stats.getActiveProfiles());
            
            double averageAge = allProfiles.stream()
                .mapToInt(ElderlyProfile::getAge)
                .average()
                .orElse(0.0);
            stats.setAverageAge(averageAge);
            
            int totalSessions = allProfiles.stream()
                .mapToInt(ElderlyProfile::getTotalSessions)
                .sum();
            stats.setTotalSessions(totalSessions);
            
            double averageRating = allProfiles.stream()
                .mapToDouble(ElderlyProfile::getAverageRating)
                .average()
                .orElse(0.0);
            stats.setAverageRating(averageRating);
            
            return stats;
        } catch (Exception e) {
            System.err.println("Error getting profile statistics: " + e.getMessage());
            return new ProfileStatistics();
        }
    }
}
