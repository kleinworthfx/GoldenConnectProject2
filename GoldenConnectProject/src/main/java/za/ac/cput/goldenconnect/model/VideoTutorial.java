package za.ac.cput.goldenconnect.model;

import java.time.LocalDateTime;

public class VideoTutorial {
    private int id;
    private String title;
    private String description;
    private String category; // TECH_BASICS, SOCIAL_MEDIA, BANKING, SHOPPING, COMMUNICATION
    private String difficulty; // BEGINNER, INTERMEDIATE, ADVANCED
    private String videoUrl;
    private String thumbnailUrl;
    private int duration; // Duration in seconds
    private String language;
    private String instructor; // Instructor name
    private int instructorId; // Instructor user ID
    private double rating;
    private int viewCount;
    private int totalRatings;
    private String tags; // Comma-separated tags
    private String transcript; // Video transcript for accessibility
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private String targetAudience; // ELDERLY, VOLUNTEER, ALL
    private String prerequisites; // What users should know before watching

    // Default constructor
    public VideoTutorial() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
        this.rating = 0.0;
        this.viewCount = 0;
        this.totalRatings = 0;
        this.status = "DRAFT";
    }

    // Constructor with essential fields
    public VideoTutorial(String title, String description, String category, String difficulty, String videoUrl) {
        this();
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.videoUrl = videoUrl;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }

    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }

    @Override
    public String toString() {
        return "VideoTutorial{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", status='" + status + '\'' +
                ", duration=" + duration +
                '}';
    }
}
