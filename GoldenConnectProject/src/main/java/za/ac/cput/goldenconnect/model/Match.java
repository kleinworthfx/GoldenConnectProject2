package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class Match {
    private int id;
    private int volunteerId;
    private int elderlyId;
    private String matchType; // COMPANIONSHIP, TECH_HELP, ACTIVITY, HEALTH
    private String status; // ACTIVE, INACTIVE, PAUSED
    private LocalDateTime matchDate;
    private LocalDateTime lastInteractionDate;
    private int totalInteractions;
    private double compatibilityScore;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPreferred; // If this is a preferred match
    private String matchReason; // Why this match was made

    // Default constructor
    public Match() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
        this.totalInteractions = 0;
        this.compatibilityScore = 0.0;
        this.isPreferred = false;
    }

    // Constructor with essential fields
    public Match(int volunteerId, int elderlyId, String matchType) {
        this();
        this.volunteerId = volunteerId;
        this.elderlyId = elderlyId;
        this.matchType = matchType;
        this.matchDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVolunteerId() { return volunteerId; }
    public void setVolunteerId(int volunteerId) { this.volunteerId = volunteerId; }

    public int getElderlyId() { return elderlyId; }
    public void setElderlyId(int elderlyId) { this.elderlyId = elderlyId; }

    public String getMatchType() { return matchType; }
    public void setMatchType(String matchType) { this.matchType = matchType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    public LocalDateTime getLastInteractionDate() { return lastInteractionDate; }
    public void setLastInteractionDate(LocalDateTime lastInteractionDate) { this.lastInteractionDate = lastInteractionDate; }

    public int getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(int totalInteractions) { this.totalInteractions = totalInteractions; }

    public double getCompatibilityScore() { return compatibilityScore; }
    public void setCompatibilityScore(double compatibilityScore) { this.compatibilityScore = compatibilityScore; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isPreferred() { return isPreferred; }
    public void setPreferred(boolean preferred) { isPreferred = preferred; }

    public String getMatchReason() { return matchReason; }
    public void setMatchReason(String matchReason) { this.matchReason = matchReason; }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", volunteerId=" + volunteerId +
                ", elderlyId=" + elderlyId +
                ", matchType='" + matchType + '\'' +
                ", status='" + status + '\'' +
                ", matchDate=" + matchDate +
                '}';
    }
}
