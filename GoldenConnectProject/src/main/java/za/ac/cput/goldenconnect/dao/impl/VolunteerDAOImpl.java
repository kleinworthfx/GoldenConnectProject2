package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.VolunteerDAO;
import za.ac.cput.goldenconnect.model.Volunteer;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAOImpl implements VolunteerDAO {
    
    @Override
    public boolean save(Volunteer volunteer) {
        String sql = "INSERT INTO volunteers (full_name, email, phone_number, address, date_of_birth, " +
                    "skills, interests, availability, emergency_contact, emergency_contact_phone, " +
                    "background_check, training_completed, preferences, notes, is_active, " +
                    "registration_date, last_active_date, created_at, updated_at, " +
                    "total_sessions_completed, average_rating, total_hours, certification, experience_level) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, volunteer.getFullName());
            pstmt.setString(2, volunteer.getEmail());
            pstmt.setString(3, volunteer.getPhoneNumber());
            pstmt.setString(4, volunteer.getAddress());
            pstmt.setTimestamp(5, volunteer.getDateOfBirth() != null ? Timestamp.valueOf(volunteer.getDateOfBirth()) : null);
            pstmt.setString(6, volunteer.getSkills());
            pstmt.setString(7, volunteer.getInterests());
            pstmt.setString(8, volunteer.getAvailability());
            pstmt.setString(9, volunteer.getEmergencyContact());
            pstmt.setString(10, volunteer.getEmergencyContactPhone());
            pstmt.setString(11, volunteer.getBackgroundCheck());
            pstmt.setString(12, volunteer.getTrainingCompleted());
            pstmt.setString(13, volunteer.getPreferences());
            pstmt.setString(14, volunteer.getNotes());
            pstmt.setBoolean(15, volunteer.isActive());
            pstmt.setTimestamp(16, Timestamp.valueOf(volunteer.getRegistrationDate()));
            pstmt.setTimestamp(17, Timestamp.valueOf(volunteer.getLastActiveDate()));
            pstmt.setTimestamp(18, Timestamp.valueOf(volunteer.getCreatedAt()));
            pstmt.setTimestamp(19, Timestamp.valueOf(volunteer.getUpdatedAt()));
            pstmt.setInt(20, volunteer.getTotalSessionsCompleted());
            pstmt.setDouble(21, volunteer.getAverageRating());
            pstmt.setInt(22, volunteer.getTotalHours());
            pstmt.setString(23, volunteer.getCertification());
            pstmt.setString(24, volunteer.getExperienceLevel());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    volunteer.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving volunteer: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public Volunteer findById(int id) {
        String sql = "SELECT * FROM volunteers WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVolunteer(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteer by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Volunteer findByEmail(String email) {
        String sql = "SELECT * FROM volunteers WHERE email = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVolunteer(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteer by email: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public Volunteer findByFullName(String fullName) {
        String sql = "SELECT * FROM volunteers WHERE full_name = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fullName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVolunteer(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteer by name: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Volunteer> findAll() {
        String sql = "SELECT * FROM volunteers ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all volunteers: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findActiveVolunteers() {
        String sql = "SELECT * FROM volunteers WHERE is_active = true ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding active volunteers: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public boolean update(Volunteer volunteer) {
        String sql = "UPDATE volunteers SET full_name = ?, email = ?, phone_number = ?, address = ?, " +
                    "date_of_birth = ?, skills = ?, interests = ?, availability = ?, emergency_contact = ?, " +
                    "emergency_contact_phone = ?, background_check = ?, training_completed = ?, " +
                    "preferences = ?, notes = ?, is_active = ?, last_active_date = ?, updated_at = ?, " +
                    "total_sessions_completed = ?, average_rating = ?, total_hours = ?, certification = ?, " +
                    "experience_level = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, volunteer.getFullName());
            pstmt.setString(2, volunteer.getEmail());
            pstmt.setString(3, volunteer.getPhoneNumber());
            pstmt.setString(4, volunteer.getAddress());
            pstmt.setTimestamp(5, volunteer.getDateOfBirth() != null ? Timestamp.valueOf(volunteer.getDateOfBirth()) : null);
            pstmt.setString(6, volunteer.getSkills());
            pstmt.setString(7, volunteer.getInterests());
            pstmt.setString(8, volunteer.getAvailability());
            pstmt.setString(9, volunteer.getEmergencyContact());
            pstmt.setString(10, volunteer.getEmergencyContactPhone());
            pstmt.setString(11, volunteer.getBackgroundCheck());
            pstmt.setString(12, volunteer.getTrainingCompleted());
            pstmt.setString(13, volunteer.getPreferences());
            pstmt.setString(14, volunteer.getNotes());
            pstmt.setBoolean(15, volunteer.isActive());
            pstmt.setTimestamp(16, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(18, volunteer.getTotalSessionsCompleted());
            pstmt.setDouble(19, volunteer.getAverageRating());
            pstmt.setInt(20, volunteer.getTotalHours());
            pstmt.setString(21, volunteer.getCertification());
            pstmt.setString(22, volunteer.getExperienceLevel());
            pstmt.setInt(23, volunteer.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating volunteer: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM volunteers WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting volunteer: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Volunteer> findBySkills(String skills) {
        String sql = "SELECT * FROM volunteers WHERE skills LIKE ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + skills + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers by skills: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findByExperienceLevel(String experienceLevel) {
        String sql = "SELECT * FROM volunteers WHERE experience_level = ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, experienceLevel);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers by experience level: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findByAvailability(String availability) {
        String sql = "SELECT * FROM volunteers WHERE availability LIKE ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + availability + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers by availability: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public boolean updateSessionCount(int volunteerId, int sessionCount) {
        String sql = "UPDATE volunteers SET total_sessions_completed = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, sessionCount);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, volunteerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating session count: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean updateAverageRating(int volunteerId, double averageRating) {
        String sql = "UPDATE volunteers SET average_rating = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, averageRating);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, volunteerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating average rating: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean updateTotalHours(int volunteerId, int totalHours) {
        String sql = "UPDATE volunteers SET total_hours = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, totalHours);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, volunteerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating total hours: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean updateLastActiveDate(int volunteerId) {
        String sql = "UPDATE volunteers SET last_active_date = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, volunteerId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating last active date: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<Volunteer> searchByName(String searchTerm) {
        String sql = "SELECT * FROM volunteers WHERE full_name LIKE ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching volunteers by name: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> searchByEmail(String searchTerm) {
        String sql = "SELECT * FROM volunteers WHERE email LIKE ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching volunteers by email: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findTopVolunteersBySessions(int limit) {
        String sql = "SELECT * FROM volunteers ORDER BY total_sessions_completed DESC LIMIT ?";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top volunteers by sessions: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findTopVolunteersByRating(int limit) {
        String sql = "SELECT * FROM volunteers ORDER BY average_rating DESC LIMIT ?";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top volunteers by rating: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findTopVolunteersByHours(int limit) {
        String sql = "SELECT * FROM volunteers ORDER BY total_hours DESC LIMIT ?";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top volunteers by hours: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findByRegistrationDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM volunteers WHERE registration_date BETWEEN ? AND ? ORDER BY registration_date DESC";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers by registration date range: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findByCertification(String certification) {
        String sql = "SELECT * FROM volunteers WHERE certification = ? ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, certification);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers by certification: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findWithBackgroundCheck() {
        String sql = "SELECT * FROM volunteers WHERE background_check IS NOT NULL AND background_check != '' ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers with background check: " + e.getMessage());
        }
        return volunteers;
    }
    
    @Override
    public List<Volunteer> findWithTrainingCompleted() {
        String sql = "SELECT * FROM volunteers WHERE training_completed IS NOT NULL AND training_completed != '' ORDER BY full_name";
        List<Volunteer> volunteers = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                volunteers.add(mapResultSetToVolunteer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding volunteers with training completed: " + e.getMessage());
        }
        return volunteers;
    }
    
    private Volunteer mapResultSetToVolunteer(ResultSet rs) throws SQLException {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(rs.getInt("id"));
        volunteer.setFullName(rs.getString("full_name"));
        volunteer.setEmail(rs.getString("email"));
        volunteer.setPhoneNumber(rs.getString("phone_number"));
        volunteer.setAddress(rs.getString("address"));
        
        Timestamp dob = rs.getTimestamp("date_of_birth");
        volunteer.setDateOfBirth(dob != null ? dob.toLocalDateTime() : null);
        
        volunteer.setSkills(rs.getString("skills"));
        volunteer.setInterests(rs.getString("interests"));
        volunteer.setAvailability(rs.getString("availability"));
        volunteer.setEmergencyContact(rs.getString("emergency_contact"));
        volunteer.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        volunteer.setBackgroundCheck(rs.getString("background_check"));
        volunteer.setTrainingCompleted(rs.getString("training_completed"));
        volunteer.setPreferences(rs.getString("preferences"));
        volunteer.setNotes(rs.getString("notes"));
        volunteer.setActive(rs.getBoolean("is_active"));
        volunteer.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        volunteer.setLastActiveDate(rs.getTimestamp("last_active_date").toLocalDateTime());
        volunteer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        volunteer.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        volunteer.setTotalSessionsCompleted(rs.getInt("total_sessions_completed"));
        volunteer.setAverageRating(rs.getDouble("average_rating"));
        volunteer.setTotalHours(rs.getInt("total_hours"));
        volunteer.setCertification(rs.getString("certification"));
        volunteer.setExperienceLevel(rs.getString("experience_level"));
        
        return volunteer;
    }
}
