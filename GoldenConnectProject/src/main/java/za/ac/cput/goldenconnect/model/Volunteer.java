package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class Volunteer {
    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime dateOfBirth;
    private String skills;
    private String interests;
    private String availability;
    private String emergencyContact;
    private String emergencyContactPhone;
    private String backgroundCheck;
    private String trainingCompleted;
    private String preferences;
    private String notes;
    private boolean isActive;
    private LocalDateTime registrationDate;
    private LocalDateTime lastActiveDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalSessionsCompleted;
    private double averageRating;
    private int totalHours;
    private String certification;
    private String experienceLevel;
    
    // Constructors
    public Volunteer() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.registrationDate = LocalDateTime.now();
        this.lastActiveDate = LocalDateTime.now();
        this.isActive = true;
        this.totalSessionsCompleted = 0;
        this.averageRating = 0.0;
        this.totalHours = 0;
    }
    
    public Volunteer(String fullName, String email, String phoneNumber) {
        this();
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getSkills() {
        return skills;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public String getAvailability() {
        return availability;
    }
    
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }
    
    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
    
    public String getBackgroundCheck() {
        return backgroundCheck;
    }
    
    public void setBackgroundCheck(String backgroundCheck) {
        this.backgroundCheck = backgroundCheck;
    }
    
    public String getTrainingCompleted() {
        return trainingCompleted;
    }
    
    public void setTrainingCompleted(String trainingCompleted) {
        this.trainingCompleted = trainingCompleted;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public LocalDateTime getLastActiveDate() {
        return lastActiveDate;
    }
    
    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public int getTotalSessionsCompleted() {
        return totalSessionsCompleted;
    }
    
    public void setTotalSessionsCompleted(int totalSessionsCompleted) {
        this.totalSessionsCompleted = totalSessionsCompleted;
    }
    
    public double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    
    public int getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }
    
    public String getCertification() {
        return certification;
    }
    
    public void setCertification(String certification) {
        this.certification = certification;
    }
    
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    
    // Helper methods
    public int getAge() {
        if (dateOfBirth != null) {
            return LocalDateTime.now().getYear() - dateOfBirth.getYear();
        }
        return 0;
    }
    
    public String getRegistrationDateFormatted() {
        if (registrationDate != null) {
            return registrationDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "N/A";
    }
    
    public String getLastActiveDateFormatted() {
        if (lastActiveDate != null) {
            return lastActiveDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "N/A";
    }
    
    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", totalSessionsCompleted=" + totalSessionsCompleted +
                ", averageRating=" + averageRating +
                ", totalHours=" + totalHours +
                '}';
    }
}
