package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.VideoTutorialDAO;
import za.ac.cput.goldenconnect.model.VideoTutorial;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoTutorialDAOImpl implements VideoTutorialDAO {
    
    @Override
    public VideoTutorial findById(int id) {
        String sql = "SELECT * FROM video_tutorials WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVideoTutorial(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorial by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<VideoTutorial> findAll() {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all video tutorials: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByCategory(String category) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE category = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by category: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByDifficulty(String difficulty) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE difficulty = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, difficulty);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by difficulty: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByStatus(String status) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by status: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByInstructorId(int instructorId) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE instructor_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by instructor ID: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByTargetAudience(String targetAudience) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE target_audience = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, targetAudience);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by target audience: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findActiveTutorials() {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE is_active = 1 ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding active video tutorials: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByLanguage(String language) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE language = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, language);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by language: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByTags(String tags) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE tags LIKE ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + tags + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by tags: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findTopRatedTutorials(int limit) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE rating > 0 ORDER BY rating DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top rated video tutorials: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findMostViewedTutorials(int limit) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials ORDER BY view_count DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding most viewed video tutorials: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public List<VideoTutorial> findByDurationRange(int minDuration, int maxDuration) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE duration BETWEEN ? AND ? ORDER BY duration ASC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, minDuration);
            stmt.setInt(2, maxDuration);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding video tutorials by duration range: " + e.getMessage());
        }
        return tutorials;
    }

    @Override
    public boolean save(VideoTutorial tutorial) {
        String sql = "INSERT INTO video_tutorials (title, description, category, difficulty, video_url, " +
                    "thumbnail_url, duration, language, instructor, instructor_id, rating, view_count, " +
                    "total_ratings, tags, transcript, is_active, created_at, updated_at, status, " +
                    "target_audience, prerequisites) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, tutorial.getTitle());
            stmt.setString(2, tutorial.getDescription());
            stmt.setString(3, tutorial.getCategory());
            stmt.setString(4, tutorial.getDifficulty());
            stmt.setString(5, tutorial.getVideoUrl());
            stmt.setString(6, tutorial.getThumbnailUrl());
            stmt.setInt(7, tutorial.getDuration());
            stmt.setString(8, tutorial.getLanguage());
            stmt.setString(9, tutorial.getInstructor());
            stmt.setInt(10, tutorial.getInstructorId());
            stmt.setDouble(11, tutorial.getRating());
            stmt.setInt(12, tutorial.getViewCount());
            stmt.setInt(13, tutorial.getTotalRatings());
            stmt.setString(14, tutorial.getTags());
            stmt.setString(15, tutorial.getTranscript());
            stmt.setBoolean(16, tutorial.isActive());
            stmt.setTimestamp(17, Timestamp.valueOf(tutorial.getCreatedAt()));
            stmt.setTimestamp(18, Timestamp.valueOf(tutorial.getUpdatedAt()));
            stmt.setString(19, tutorial.getStatus());
            stmt.setString(20, tutorial.getTargetAudience());
            stmt.setString(21, tutorial.getPrerequisites());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    tutorial.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving video tutorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(VideoTutorial tutorial) {
        String sql = "UPDATE video_tutorials SET title = ?, description = ?, category = ?, difficulty = ?, " +
                    "video_url = ?, thumbnail_url = ?, duration = ?, language = ?, instructor = ?, " +
                    "instructor_id = ?, rating = ?, view_count = ?, total_ratings = ?, tags = ?, " +
                    "transcript = ?, is_active = ?, updated_at = ?, status = ?, target_audience = ?, " +
                    "prerequisites = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tutorial.getTitle());
            stmt.setString(2, tutorial.getDescription());
            stmt.setString(3, tutorial.getCategory());
            stmt.setString(4, tutorial.getDifficulty());
            stmt.setString(5, tutorial.getVideoUrl());
            stmt.setString(6, tutorial.getThumbnailUrl());
            stmt.setInt(7, tutorial.getDuration());
            stmt.setString(8, tutorial.getLanguage());
            stmt.setString(9, tutorial.getInstructor());
            stmt.setInt(10, tutorial.getInstructorId());
            stmt.setDouble(11, tutorial.getRating());
            stmt.setInt(12, tutorial.getViewCount());
            stmt.setInt(13, tutorial.getTotalRatings());
            stmt.setString(14, tutorial.getTags());
            stmt.setString(15, tutorial.getTranscript());
            stmt.setBoolean(16, tutorial.isActive());
            stmt.setTimestamp(17, Timestamp.valueOf(tutorial.getUpdatedAt()));
            stmt.setString(18, tutorial.getStatus());
            stmt.setString(19, tutorial.getTargetAudience());
            stmt.setString(20, tutorial.getPrerequisites());
            stmt.setInt(21, tutorial.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating video tutorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM video_tutorials WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting video tutorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatus(int tutorialId, String status) {
        String sql = "UPDATE video_tutorials SET status = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(3, tutorialId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating video tutorial status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateRating(int tutorialId, double rating) {
        String sql = "UPDATE video_tutorials SET rating = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rating);
            stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(3, tutorialId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating video tutorial rating: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean incrementViewCount(int tutorialId) {
        String sql = "UPDATE video_tutorials SET view_count = view_count + 1, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(2, tutorialId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing video tutorial view count: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean incrementTotalRatings(int tutorialId) {
        String sql = "UPDATE video_tutorials SET total_ratings = total_ratings + 1, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(java.time.LocalDateTime.now()));
            stmt.setInt(2, tutorialId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing video tutorial total ratings: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<VideoTutorial> searchTutorials(String searchTerm) {
        List<VideoTutorial> tutorials = new ArrayList<>();
        String sql = "SELECT * FROM video_tutorials WHERE title LIKE ? OR description LIKE ? OR tags LIKE ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tutorials.add(mapResultSetToVideoTutorial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching video tutorials: " + e.getMessage());
        }
        return tutorials;
    }

    private VideoTutorial mapResultSetToVideoTutorial(ResultSet rs) throws SQLException {
        VideoTutorial tutorial = new VideoTutorial();
        tutorial.setId(rs.getInt("id"));
        tutorial.setTitle(rs.getString("title"));
        tutorial.setDescription(rs.getString("description"));
        tutorial.setCategory(rs.getString("category"));
        tutorial.setDifficulty(rs.getString("difficulty"));
        tutorial.setVideoUrl(rs.getString("video_url"));
        tutorial.setThumbnailUrl(rs.getString("thumbnail_url"));
        tutorial.setDuration(rs.getInt("duration"));
        tutorial.setLanguage(rs.getString("language"));
        tutorial.setInstructor(rs.getString("instructor"));
        tutorial.setInstructorId(rs.getInt("instructor_id"));
        tutorial.setRating(rs.getDouble("rating"));
        tutorial.setViewCount(rs.getInt("view_count"));
        tutorial.setTotalRatings(rs.getInt("total_ratings"));
        tutorial.setTags(rs.getString("tags"));
        tutorial.setTranscript(rs.getString("transcript"));
        tutorial.setActive(rs.getBoolean("is_active"));
        tutorial.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        tutorial.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        tutorial.setStatus(rs.getString("status"));
        tutorial.setTargetAudience(rs.getString("target_audience"));
        tutorial.setPrerequisites(rs.getString("prerequisites"));
        return tutorial;
    }
}
