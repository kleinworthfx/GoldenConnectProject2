package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class Feedback {
    private int id;
    private int userId; // User giving feedback
    private int targetId; // User/Activity/Service being rated
    private String targetType; // USER, ACTIVITY, SERVICE, SYSTEM
    private int rating; // 1-5 stars
    private String comment;
    private String category; // COMPANIONSHIP, TECH_HELP, ACTIVITY, OVERALL
    private LocalDateTime feedbackDate;
    private String status; // PENDING, APPROVED, REJECTED
    private boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String response; // Admin response to feedback
    private boolean isResolved;
    private String tags; // Comma-separated tags for categorization

    // Default constructor
    public Feedback() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.feedbackDate = LocalDateTime.now();
        this.status = "PENDING";
        this.isAnonymous = false;
        this.isResolved = false;
        this.rating = 0;
    }

    // Constructor with essential fields
    public Feedback(int userId, int targetId, String targetType, int rating, String comment) {
        this();
        this.userId = userId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getTargetId() { return targetId; }
    public void setTargetId(int targetId) { this.targetId = targetId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDateTime feedbackDate) { this.feedbackDate = feedbackDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAnonymous() { return isAnonymous; }
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public boolean isResolved() { return isResolved; }
    public void setResolved(boolean resolved) { isResolved = resolved; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", userId=" + userId +
                ", targetId=" + targetId +
                ", targetType='" + targetType + '\'' +
                ", rating=" + rating +
                ", status='" + status + '\'' +
                ", feedbackDate=" + feedbackDate +
                '}';
    }
}
