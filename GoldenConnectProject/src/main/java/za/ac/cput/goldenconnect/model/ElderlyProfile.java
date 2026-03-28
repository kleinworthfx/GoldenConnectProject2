package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class ElderlyProfile {
    private int id;
    private String fullName;
    private int age;
    private String medicalCondition;
    private String retirementVillage;
    private String address;
    private String phoneNumber;
    private String emergencyContact;
    private String emergencyContactPhone;
    private String preferences;
    private String notes;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalSessions;
    private double averageRating;
    
    // Constructors
    public ElderlyProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
        this.totalSessions = 0;
        this.averageRating = 0.0;
    }
    
    public ElderlyProfile(String fullName, int age, String medicalCondition, String retirementVillage) {
        this();
        this.fullName = fullName;
        this.age = age;
        this.medicalCondition = medicalCondition;
        this.retirementVillage = retirementVillage;
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
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getMedicalCondition() {
        return medicalCondition;
    }
    
    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }
    
    public String getRetirementVillage() {
        return retirementVillage;
    }
    
    public void setRetirementVillage(String retirementVillage) {
        this.retirementVillage = retirementVillage;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
    
    public int getTotalSessions() {
        return totalSessions;
    }
    
    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }
    
    public double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    
    @Override
    public String toString() {
        return "ElderlyProfile{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                ", medicalCondition='" + medicalCondition + '\'' +
                ", retirementVillage='" + retirementVillage + '\'' +
                ", isActive=" + isActive +
                ", totalSessions=" + totalSessions +
                ", averageRating=" + averageRating +
                '}';
    }
}
