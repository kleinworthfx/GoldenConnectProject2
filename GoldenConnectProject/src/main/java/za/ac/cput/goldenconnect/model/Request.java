package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class Request {
    private int id;
    private int requesterId; // Elderly user ID
    private int volunteerId; // Volunteer user ID (can be null initially)
    private String requestType; // COMPANIONSHIP, TECH_HELP, ACTIVITY, HEALTH_CHECK
    private String description;
    private String status; // PENDING, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED
    private LocalDateTime requestedDate;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String location; // Virtual or physical location
    private int duration; // Duration in minutes
    private String specialRequirements;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double rating;
    private String feedback;

    // Default constructor
    public Request() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING";
        this.rating = 0.0;
    }

    // Constructor with essential fields
    public Request(int requesterId, String requestType, String description, LocalDateTime requestedDate) {
        this();
        this.requesterId = requesterId;
        this.requestType = requestType;
        this.description = description;
        this.requestedDate = requestedDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRequesterId() { return requesterId; }
    public void setRequesterId(int requesterId) { this.requesterId = requesterId; }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDateTime requestedDate) { this.requestedDate = requestedDate; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public LocalDateTime getCompletedDate() { return completedDate; }
    public void setCompletedDate(LocalDateTime completedDate) { this.completedDate = completedDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getSpecialRequirements() { return specialRequirements; }
    public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", requesterId=" + requesterId +
                ", volunteerId=" + volunteerId +
                ", requestType='" + requestType + '\'' +
                ", status='" + status + '\'' +
                ", requestedDate=" + requestedDate +
                '}';
    }
}
