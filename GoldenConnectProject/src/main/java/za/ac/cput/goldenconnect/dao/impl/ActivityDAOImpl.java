package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.ActivityDAO;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements ActivityDAO {
    
    @Override
    public Activity findById(int id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToActivity(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding activity by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Activity> findAll() {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all activities: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByCategory(String category) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE category = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by category: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByType(String type) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE type = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by type: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByStatus(String status) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE status = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by status: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByVolunteerId(int volunteerId) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE volunteer_id = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by volunteer ID: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByDifficulty(String difficulty) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE difficulty = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, difficulty);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by difficulty: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE scheduled_date BETWEEN ? AND ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by date range: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findUpcomingActivities() {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE scheduled_date >= NOW() AND status IN ('SCHEDULED', 'IN_PROGRESS') ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding upcoming activities: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findRecurringActivities() {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE is_recurring = true ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding recurring activities: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public List<Activity> findActivitiesByLocation(String location) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE location = ? ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by location: " + e.getMessage());
        }
        return activities;
    }

    @Override
    public boolean save(Activity activity) {
        String sql = "INSERT INTO activities (name, description, category, type, duration, difficulty, location, " +
                    "max_participants, current_participants, scheduled_date, start_time, end_time, status, " +
                    "volunteer_id, materials, instructions, is_recurring, recurrence_pattern, rating, total_sessions) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, activity.getName());
            stmt.setString(2, activity.getDescription());
            stmt.setString(3, activity.getCategory());
            stmt.setString(4, activity.getType());
            stmt.setInt(5, activity.getDuration());
            stmt.setString(6, activity.getDifficulty());
            stmt.setString(7, activity.getLocation());
            stmt.setInt(8, activity.getMaxParticipants());
            stmt.setInt(9, activity.getCurrentParticipants());
            stmt.setTimestamp(10, activity.getScheduledDate() != null ? Timestamp.valueOf(activity.getScheduledDate()) : null);
            stmt.setTimestamp(11, activity.getStartTime() != null ? Timestamp.valueOf(activity.getStartTime()) : null);
            stmt.setTimestamp(12, activity.getEndTime() != null ? Timestamp.valueOf(activity.getEndTime()) : null);
            stmt.setString(13, activity.getStatus());
            if (activity.getVolunteerId() > 0) {
                stmt.setInt(14, activity.getVolunteerId());
            } else {
                stmt.setNull(14, Types.INTEGER);
            }
            stmt.setString(15, activity.getMaterials());
            stmt.setString(16, activity.getInstructions());
            stmt.setBoolean(17, activity.isRecurring());
            stmt.setString(18, activity.getRecurrencePattern());
            stmt.setDouble(19, activity.getRating());
            stmt.setInt(20, activity.getTotalSessions());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    activity.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving activity: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Activity activity) {
        String sql = "UPDATE activities SET name = ?, description = ?, category = ?, type = ?, duration = ?, " +
                    "difficulty = ?, location = ?, max_participants = ?, current_participants = ?, scheduled_date = ?, " +
                    "start_time = ?, end_time = ?, status = ?, volunteer_id = ?, materials = ?, instructions = ?, " +
                    "is_recurring = ?, recurrence_pattern = ?, rating = ?, total_sessions = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, activity.getName());
            stmt.setString(2, activity.getDescription());
            stmt.setString(3, activity.getCategory());
            stmt.setString(4, activity.getType());
            stmt.setInt(5, activity.getDuration());
            stmt.setString(6, activity.getDifficulty());
            stmt.setString(7, activity.getLocation());
            stmt.setInt(8, activity.getMaxParticipants());
            stmt.setInt(9, activity.getCurrentParticipants());
            stmt.setTimestamp(10, activity.getScheduledDate() != null ? Timestamp.valueOf(activity.getScheduledDate()) : null);
            stmt.setTimestamp(11, activity.getStartTime() != null ? Timestamp.valueOf(activity.getStartTime()) : null);
            stmt.setTimestamp(12, activity.getEndTime() != null ? Timestamp.valueOf(activity.getEndTime()) : null);
            stmt.setString(13, activity.getStatus());
            if (activity.getVolunteerId() > 0) {
                stmt.setInt(14, activity.getVolunteerId());
            } else {
                stmt.setNull(14, Types.INTEGER);
            }
            stmt.setString(15, activity.getMaterials());
            stmt.setString(16, activity.getInstructions());
            stmt.setBoolean(17, activity.isRecurring());
            stmt.setString(18, activity.getRecurrencePattern());
            stmt.setDouble(19, activity.getRating());
            stmt.setInt(20, activity.getTotalSessions());
            stmt.setInt(21, activity.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM activities WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting activity: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatus(int activityId, String status) {
        String sql = "UPDATE activities SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, activityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateParticipants(int activityId, int currentParticipants) {
        String sql = "UPDATE activities SET current_participants = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, currentParticipants);
            stmt.setInt(2, activityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity participants: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateRating(int activityId, double rating) {
        String sql = "UPDATE activities SET rating = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, rating);
            stmt.setInt(2, activityId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating activity rating: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Activity> findActivitiesByDate(LocalDateTime date) {
        List<Activity> activities = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE DATE(scheduled_date) = DATE(?) ORDER BY scheduled_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding activities by date: " + e.getMessage());
        }
        return activities;
    }
    
    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setName(rs.getString("name"));
        activity.setDescription(rs.getString("description"));
        activity.setCategory(rs.getString("category"));
        activity.setType(rs.getString("type"));
        activity.setDuration(rs.getInt("duration"));
        activity.setDifficulty(rs.getString("difficulty"));
        activity.setLocation(rs.getString("location"));
        activity.setMaxParticipants(rs.getInt("max_participants"));
        activity.setCurrentParticipants(rs.getInt("current_participants"));
        
        Timestamp scheduledDate = rs.getTimestamp("scheduled_date");
        if (scheduledDate != null) {
            activity.setScheduledDate(scheduledDate.toLocalDateTime());
        }
        
        Timestamp startTime = rs.getTimestamp("start_time");
        if (startTime != null) {
            activity.setStartTime(startTime.toLocalDateTime());
        }
        
        Timestamp endTime = rs.getTimestamp("end_time");
        if (endTime != null) {
            activity.setEndTime(endTime.toLocalDateTime());
        }
        
        activity.setStatus(rs.getString("status"));
        activity.setVolunteerId(rs.getInt("volunteer_id"));
        activity.setMaterials(rs.getString("materials"));
        activity.setInstructions(rs.getString("instructions"));
        activity.setRecurring(rs.getBoolean("is_recurring"));
        activity.setRecurrencePattern(rs.getString("recurrence_pattern"));
        activity.setRating(rs.getDouble("rating"));
        activity.setTotalSessions(rs.getInt("total_sessions"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            activity.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            activity.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return activity;
    }
}
