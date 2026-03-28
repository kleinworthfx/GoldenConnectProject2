package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.FeedbackDAO;
import za.ac.cput.goldenconnect.model.Feedback;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAOImpl implements FeedbackDAO {
    
    @Override
    public Feedback findById(int id) {
        String sql = "SELECT * FROM feedback WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToFeedback(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding feedback by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Feedback> findAll() {
        // TODO: Implement database query to find all feedback
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByUserId(int userId) {
        List<Feedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                feedbacks.add(mapResultSetToFeedback(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding feedback by user ID: " + e.getMessage());
        }
        return feedbacks;
    }

    @Override
    public List<Feedback> findByTargetId(int targetId) {
        // TODO: Implement database query to find feedback by target ID
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByTargetType(String targetType) {
        // TODO: Implement database query to find feedback by target type
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByStatus(String status) {
        // TODO: Implement database query to find feedback by status
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByCategory(String category) {
        // TODO: Implement database query to find feedback by category
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByRating(int rating) {
        // TODO: Implement database query to find feedback by rating
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement database query to find feedback by date range
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findPendingFeedback() {
        // TODO: Implement database query to find pending feedback
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findResolvedFeedback() {
        // TODO: Implement database query to find resolved feedback
        return new ArrayList<>();
    }

    @Override
    public List<Feedback> findAnonymousFeedback() {
        // TODO: Implement database query to find anonymous feedback
        return new ArrayList<>();
    }

    @Override
    public boolean save(Feedback feedback) {
        String sql = "INSERT INTO feedback (user_id, target_id, target_type, rating, comment, " +
                    "category, is_anonymous, response, status, is_resolved, tags, feedback_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, feedback.getUserId());
            stmt.setInt(2, feedback.getTargetId());
            stmt.setString(3, feedback.getTargetType());
            stmt.setInt(4, feedback.getRating());
            stmt.setString(5, feedback.getComment());
            stmt.setString(6, feedback.getCategory());
            stmt.setBoolean(7, feedback.isAnonymous());
            stmt.setString(8, feedback.getResponse());
            stmt.setString(9, feedback.getStatus());
            stmt.setBoolean(10, feedback.isResolved());
            stmt.setString(11, feedback.getTags());
            stmt.setTimestamp(12, feedback.getFeedbackDate() != null ? Timestamp.valueOf(feedback.getFeedbackDate()) : null);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    feedback.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving feedback: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Feedback feedback) {
        // TODO: Implement database update for existing feedback
        return false;
    }

    @Override
    public boolean delete(int id) {
        // TODO: Implement database delete for feedback
        return false;
    }

    @Override
    public boolean updateStatus(int feedbackId, String status) {
        // TODO: Implement database update for feedback status
        return false;
    }

    @Override
    public boolean updateResponse(int feedbackId, String response) {
        // TODO: Implement database update for feedback response
        return false;
    }

    @Override
    public boolean markAsResolved(int feedbackId) {
        // TODO: Implement database update to mark feedback as resolved
        return false;
    }

    @Override
    public List<Feedback> findFeedbackByDate(LocalDateTime date) {
        // TODO: Implement database query to find feedback by specific date
        return new ArrayList<>();
    }
    
    private Feedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setId(rs.getInt("id"));
        feedback.setUserId(rs.getInt("user_id"));
        feedback.setTargetId(rs.getInt("target_id"));
        feedback.setTargetType(rs.getString("target_type"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setComment(rs.getString("comment"));
        feedback.setCategory(rs.getString("category"));
        feedback.setAnonymous(rs.getBoolean("is_anonymous"));
        feedback.setResponse(rs.getString("response"));
        feedback.setStatus(rs.getString("status"));
        feedback.setResolved(rs.getBoolean("is_resolved"));
        feedback.setTags(rs.getString("tags"));
        
        Timestamp feedbackDate = rs.getTimestamp("feedback_date");
        if (feedbackDate != null) {
            feedback.setFeedbackDate(feedbackDate.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            feedback.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            feedback.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return feedback;
    }
}
