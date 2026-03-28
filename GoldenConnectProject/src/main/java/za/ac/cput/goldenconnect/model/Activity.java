package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class Activity {
    private int id;
    private String name;
    private String description;
    private String category; // COMPANIONSHIP, TECH_HELP, LEARNING, HEALTH, ENTERTAINMENT
    private String type; // INDIVIDUAL, GROUP, WORKSHOP
    private int duration; // Duration in minutes
    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED
    private String location; // Virtual, Physical, Hybrid
    private int maxParticipants;
    private int currentParticipants;
    private LocalDateTime scheduledDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    private int volunteerId; // Lead volunteer
    private String materials; // Required materials or resources
    private String instructions; // Step-by-step instructions
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isRecurring;
    private String recurrencePattern; // Daily, Weekly, Monthly
    private double rating;
    private int totalSessions;

    // Default constructor
    public Activity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "SCHEDULED";
        this.currentParticipants = 0;
        this.rating = 0.0;
        this.totalSessions = 0;
        this.isRecurring = false;
    }

    // Constructor with essential fields
    public Activity(String name, String description, String category, String type, int duration) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.duration = duration;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public int getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(int currentParticipants) { this.currentParticipants = currentParticipants; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public String getMaterials() { return materials; }
    public void setMaterials(String materials) { this.materials = materials; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }

    public String getRecurrencePattern() { return recurrencePattern; }
    public void setRecurrencePattern(String recurrencePattern) { this.recurrencePattern = recurrencePattern; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalSessions() { return totalSessions; }
    public void setTotalSessions(int totalSessions) { this.totalSessions = totalSessions; }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", scheduledDate=" + scheduledDate +
                '}';
    }
}
