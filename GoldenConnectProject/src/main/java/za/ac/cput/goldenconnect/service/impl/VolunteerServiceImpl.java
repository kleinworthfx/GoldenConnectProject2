package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.VolunteerService;
import za.ac.cput.goldenconnect.dao.VolunteerDAO;
import za.ac.cput.goldenconnect.dao.impl.VolunteerDAOImpl;
import za.ac.cput.goldenconnect.model.Volunteer;

import java.time.LocalDateTime;
import java.util.List;

public class VolunteerServiceImpl implements VolunteerService {
    
    private VolunteerDAO volunteerDAO;
    
    public VolunteerServiceImpl() {
        this.volunteerDAO = new VolunteerDAOImpl();
    }
    
    @Override
    public Volunteer createVolunteer(Volunteer volunteer) {
        try {
            // Validate volunteer data
            if (!validateVolunteer(volunteer)) {
                throw new IllegalArgumentException("Invalid volunteer data");
            }
            
            // Check if volunteer with same email already exists
            Volunteer existingVolunteer = volunteerDAO.findByEmail(volunteer.getEmail());
            if (existingVolunteer != null) {
                throw new IllegalArgumentException("Volunteer with this email already exists");
            }
            
            // Set timestamps
            volunteer.setCreatedAt(LocalDateTime.now());
            volunteer.setUpdatedAt(LocalDateTime.now());
            volunteer.setRegistrationDate(LocalDateTime.now());
            volunteer.setLastActiveDate(LocalDateTime.now());
            
            // Save to database
            boolean saved = volunteerDAO.save(volunteer);
            if (saved) {
                return volunteer;
            } else {
                throw new RuntimeException("Failed to save volunteer to database");
            }
        } catch (Exception e) {
            System.err.println("Error creating volunteer: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public Volunteer getVolunteerById(int id) {
        try {
            return volunteerDAO.findById(id);
        } catch (Exception e) {
            System.err.println("Error getting volunteer by ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Volunteer getVolunteerByEmail(String email) {
        try {
            return volunteerDAO.findByEmail(email);
        } catch (Exception e) {
            System.err.println("Error getting volunteer by email: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Volunteer getVolunteerByName(String fullName) {
        try {
            return volunteerDAO.findByFullName(fullName);
        } catch (Exception e) {
            System.err.println("Error getting volunteer by name: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Volunteer> getAllVolunteers() {
        try {
            return volunteerDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all volunteers: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getActiveVolunteers() {
        try {
            return volunteerDAO.findActiveVolunteers();
        } catch (Exception e) {
            System.err.println("Error getting active volunteers: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public Volunteer updateVolunteer(Volunteer volunteer) {
        try {
            // Validate volunteer data
            if (!validateVolunteer(volunteer)) {
                throw new IllegalArgumentException("Invalid volunteer data");
            }
            
            // Check if volunteer exists
            Volunteer existingVolunteer = volunteerDAO.findById(volunteer.getId());
            if (existingVolunteer == null) {
                throw new IllegalArgumentException("Volunteer not found");
            }
            
            // Check if email is being changed and if new email already exists
            if (!existingVolunteer.getEmail().equals(volunteer.getEmail())) {
                Volunteer volunteerWithNewEmail = volunteerDAO.findByEmail(volunteer.getEmail());
                if (volunteerWithNewEmail != null && volunteerWithNewEmail.getId() != volunteer.getId()) {
                    throw new IllegalArgumentException("Volunteer with this email already exists");
                }
            }
            
            // Update timestamp
            volunteer.setUpdatedAt(LocalDateTime.now());
            
            // Update in database
            boolean updated = volunteerDAO.update(volunteer);
            if (updated) {
                return volunteer;
            } else {
                throw new RuntimeException("Failed to update volunteer in database");
            }
        } catch (Exception e) {
            System.err.println("Error updating volunteer: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public boolean deleteVolunteer(int id) {
        try {
            // Check if volunteer exists
            Volunteer existingVolunteer = volunteerDAO.findById(id);
            if (existingVolunteer == null) {
                throw new IllegalArgumentException("Volunteer not found");
            }
            
            return volunteerDAO.delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting volunteer: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Volunteer> searchVolunteersByName(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.searchByName(searchTerm.trim());
        } catch (Exception e) {
            System.err.println("Error searching volunteers by name: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> searchVolunteersByEmail(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.searchByEmail(searchTerm.trim());
        } catch (Exception e) {
            System.err.println("Error searching volunteers by email: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersBySkills(String skills) {
        try {
            if (skills == null || skills.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.findBySkills(skills.trim());
        } catch (Exception e) {
            System.err.println("Error getting volunteers by skills: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersByExperienceLevel(String experienceLevel) {
        try {
            if (experienceLevel == null || experienceLevel.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.findByExperienceLevel(experienceLevel.trim());
        } catch (Exception e) {
            System.err.println("Error getting volunteers by experience level: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersByAvailability(String availability) {
        try {
            if (availability == null || availability.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.findByAvailability(availability.trim());
        } catch (Exception e) {
            System.err.println("Error getting volunteers by availability: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean updateSessionCount(int volunteerId, int sessionCount) {
        try {
            if (sessionCount < 0) {
                throw new IllegalArgumentException("Session count cannot be negative");
            }
            return volunteerDAO.updateSessionCount(volunteerId, sessionCount);
        } catch (Exception e) {
            System.err.println("Error updating session count: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateAverageRating(int volunteerId, double averageRating) {
        try {
            if (averageRating < 0 || averageRating > 5) {
                throw new IllegalArgumentException("Average rating must be between 0 and 5");
            }
            return volunteerDAO.updateAverageRating(volunteerId, averageRating);
        } catch (Exception e) {
            System.err.println("Error updating average rating: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateTotalHours(int volunteerId, int totalHours) {
        try {
            if (totalHours < 0) {
                throw new IllegalArgumentException("Total hours cannot be negative");
            }
            return volunteerDAO.updateTotalHours(volunteerId, totalHours);
        } catch (Exception e) {
            System.err.println("Error updating total hours: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean updateLastActiveDate(int volunteerId) {
        try {
            return volunteerDAO.updateLastActiveDate(volunteerId);
        } catch (Exception e) {
            System.err.println("Error updating last active date: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Volunteer> getTopVolunteersBySessions(int limit) {
        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be positive");
            }
            return volunteerDAO.findTopVolunteersBySessions(limit);
        } catch (Exception e) {
            System.err.println("Error getting top volunteers by sessions: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getTopVolunteersByRating(int limit) {
        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be positive");
            }
            return volunteerDAO.findTopVolunteersByRating(limit);
        } catch (Exception e) {
            System.err.println("Error getting top volunteers by rating: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getTopVolunteersByHours(int limit) {
        try {
            if (limit <= 0) {
                throw new IllegalArgumentException("Limit must be positive");
            }
            return volunteerDAO.findTopVolunteersByHours(limit);
        } catch (Exception e) {
            System.err.println("Error getting top volunteers by hours: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersByRegistrationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Invalid date range");
            }
            return volunteerDAO.findByRegistrationDateRange(startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error getting volunteers by registration date range: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersByCertification(String certification) {
        try {
            if (certification == null || certification.trim().isEmpty()) {
                return getAllVolunteers();
            }
            return volunteerDAO.findByCertification(certification.trim());
        } catch (Exception e) {
            System.err.println("Error getting volunteers by certification: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersWithBackgroundCheck() {
        try {
            return volunteerDAO.findWithBackgroundCheck();
        } catch (Exception e) {
            System.err.println("Error getting volunteers with background check: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Volunteer> getVolunteersWithTrainingCompleted() {
        try {
            return volunteerDAO.findWithTrainingCompleted();
        } catch (Exception e) {
            System.err.println("Error getting volunteers with training completed: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean validateVolunteer(Volunteer volunteer) {
        if (volunteer == null) {
            return false;
        }
        
        // Validate required fields
        if (volunteer.getFullName() == null || volunteer.getFullName().trim().isEmpty()) {
            return false;
        }
        
        if (volunteer.getEmail() == null || volunteer.getEmail().trim().isEmpty()) {
            return false;
        }
        
        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!volunteer.getEmail().matches(emailRegex)) {
            return false;
        }
        
        if (volunteer.getPhoneNumber() == null || volunteer.getPhoneNumber().trim().isEmpty()) {
            return false;
        }
        
        // Validate phone number format
        String phoneRegex = "^[+]?[0-9\\s\\-\\(\\)]{10,}$";
        if (!volunteer.getPhoneNumber().matches(phoneRegex)) {
            return false;
        }
        
        // Validate age (if date of birth is provided)
        if (volunteer.getDateOfBirth() != null) {
            int age = volunteer.getAge();
            if (age < 16 || age > 100) {
                return false;
            }
        }
        
        // Validate emergency contact phone (optional field)
        if (volunteer.getEmergencyContactPhone() != null && !volunteer.getEmergencyContactPhone().trim().isEmpty()) {
            if (!volunteer.getEmergencyContactPhone().matches(phoneRegex)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public VolunteerStatistics getVolunteerStatistics() {
        try {
            List<Volunteer> allVolunteers = getAllVolunteers();
            VolunteerStatistics stats = new VolunteerStatistics();
            
            if (allVolunteers.isEmpty()) {
                return stats;
            }
            
            stats.setTotalVolunteers(allVolunteers.size());
            stats.setActiveVolunteers((int) allVolunteers.stream().filter(Volunteer::isActive).count());
            stats.setInactiveVolunteers(stats.getTotalVolunteers() - stats.getActiveVolunteers());
            
            double averageAge = allVolunteers.stream()
                .mapToInt(Volunteer::getAge)
                .filter(age -> age > 0)
                .average()
                .orElse(0.0);
            stats.setAverageAge(averageAge);
            
            int totalSessions = allVolunteers.stream()
                .mapToInt(Volunteer::getTotalSessionsCompleted)
                .sum();
            stats.setTotalSessionsCompleted(totalSessions);
            
            double averageRating = allVolunteers.stream()
                .mapToDouble(Volunteer::getAverageRating)
                .average()
                .orElse(0.0);
            stats.setAverageRating(averageRating);
            
            int totalHours = allVolunteers.stream()
                .mapToInt(Volunteer::getTotalHours)
                .sum();
            stats.setTotalHours(totalHours);
            
            int volunteersWithBackgroundCheck = (int) allVolunteers.stream()
                .filter(v -> v.getBackgroundCheck() != null && !v.getBackgroundCheck().trim().isEmpty())
                .count();
            stats.setVolunteersWithBackgroundCheck(volunteersWithBackgroundCheck);
            
            int volunteersWithTraining = (int) allVolunteers.stream()
                .filter(v -> v.getTrainingCompleted() != null && !v.getTrainingCompleted().trim().isEmpty())
                .count();
            stats.setVolunteersWithTraining(volunteersWithTraining);
            
            return stats;
        } catch (Exception e) {
            System.err.println("Error getting volunteer statistics: " + e.getMessage());
            return new VolunteerStatistics();
        }
    }
}
