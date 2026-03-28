package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.ElderlyProfileDAO;
import za.ac.cput.goldenconnect.model.ElderlyProfile;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ElderlyProfileDAOImpl implements ElderlyProfileDAO {
    
    @Override
    public boolean save(ElderlyProfile elderlyProfile) {
        String sql = "INSERT INTO elderly_profiles (full_name, age, medical_condition, retirement_village, " +
                    "address, phone_number, emergency_contact, emergency_contact_phone, preferences, notes, " +
                    "is_active, created_at, updated_at, total_sessions, average_rating) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, elderlyProfile.getFullName());
            pstmt.setInt(2, elderlyProfile.getAge());
            pstmt.setString(3, elderlyProfile.getMedicalCondition());
            pstmt.setString(4, elderlyProfile.getRetirementVillage());
            pstmt.setString(5, elderlyProfile.getAddress());
            pstmt.setString(6, elderlyProfile.getPhoneNumber());
            pstmt.setString(7, elderlyProfile.getEmergencyContact());
            pstmt.setString(8, elderlyProfile.getEmergencyContactPhone());
            pstmt.setString(9, elderlyProfile.getPreferences());
            pstmt.setString(10, elderlyProfile.getNotes());
            pstmt.setBoolean(11, elderlyProfile.isActive());
            pstmt.setTimestamp(12, Timestamp.valueOf(elderlyProfile.getCreatedAt()));
            pstmt.setTimestamp(13, Timestamp.valueOf(elderlyProfile.getUpdatedAt()));
            pstmt.setInt(14, elderlyProfile.getTotalSessions());
            pstmt.setDouble(15, elderlyProfile.getAverageRating());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    elderlyProfile.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving elderly profile: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public ElderlyProfile findById(int id) {
        String sql = "SELECT * FROM elderly_profiles WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToElderlyProfile(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding elderly profile by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public ElderlyProfile findByFullName(String fullName) {
        String sql = "SELECT * FROM elderly_profiles WHERE full_name = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fullName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToElderlyProfile(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding elderly profile by name: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<ElderlyProfile> findAll() {
        String sql = "SELECT * FROM elderly_profiles ORDER BY full_name";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all elderly profiles: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public List<ElderlyProfile> findActiveProfiles() {
        String sql = "SELECT * FROM elderly_profiles WHERE is_active = true ORDER BY full_name";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding active elderly profiles: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public boolean update(ElderlyProfile elderlyProfile) {
        String sql = "UPDATE elderly_profiles SET full_name = ?, age = ?, medical_condition = ?, " +
                    "retirement_village = ?, address = ?, phone_number = ?, emergency_contact = ?, " +
                    "emergency_contact_phone = ?, preferences = ?, notes = ?, is_active = ?, " +
                    "updated_at = ?, total_sessions = ?, average_rating = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, elderlyProfile.getFullName());
            pstmt.setInt(2, elderlyProfile.getAge());
            pstmt.setString(3, elderlyProfile.getMedicalCondition());
            pstmt.setString(4, elderlyProfile.getRetirementVillage());
            pstmt.setString(5, elderlyProfile.getAddress());
            pstmt.setString(6, elderlyProfile.getPhoneNumber());
            pstmt.setString(7, elderlyProfile.getEmergencyContact());
            pstmt.setString(8, elderlyProfile.getEmergencyContactPhone());
            pstmt.setString(9, elderlyProfile.getPreferences());
            pstmt.setString(10, elderlyProfile.getNotes());
            pstmt.setBoolean(11, elderlyProfile.isActive());
            pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(13, elderlyProfile.getTotalSessions());
            pstmt.setDouble(14, elderlyProfile.getAverageRating());
            pstmt.setInt(15, elderlyProfile.getId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating elderly profile: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM elderly_profiles WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting elderly profile: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<ElderlyProfile> findByRetirementVillage(String retirementVillage) {
        String sql = "SELECT * FROM elderly_profiles WHERE retirement_village = ? ORDER BY full_name";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, retirementVillage);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding elderly profiles by retirement village: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public List<ElderlyProfile> findByAgeRange(int minAge, int maxAge) {
        String sql = "SELECT * FROM elderly_profiles WHERE age BETWEEN ? AND ? ORDER BY age";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, minAge);
            pstmt.setInt(2, maxAge);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding elderly profiles by age range: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public List<ElderlyProfile> findByMedicalCondition(String medicalCondition) {
        String sql = "SELECT * FROM elderly_profiles WHERE medical_condition LIKE ? ORDER BY full_name";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + medicalCondition + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding elderly profiles by medical condition: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public boolean updateSessionCount(int elderlyId, int sessionCount) {
        String sql = "UPDATE elderly_profiles SET total_sessions = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, sessionCount);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, elderlyId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating session count: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public boolean updateAverageRating(int elderlyId, double averageRating) {
        String sql = "UPDATE elderly_profiles SET average_rating = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, averageRating);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, elderlyId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating average rating: " + e.getMessage());
        }
        return false;
    }
    
    @Override
    public List<ElderlyProfile> searchByName(String searchTerm) {
        String sql = "SELECT * FROM elderly_profiles WHERE full_name LIKE ? ORDER BY full_name";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching elderly profiles by name: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public List<ElderlyProfile> findTopProfilesBySessions(int limit) {
        String sql = "SELECT * FROM elderly_profiles ORDER BY total_sessions DESC LIMIT ?";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top profiles by sessions: " + e.getMessage());
        }
        return profiles;
    }
    
    @Override
    public List<ElderlyProfile> findTopProfilesByRating(int limit) {
        String sql = "SELECT * FROM elderly_profiles ORDER BY average_rating DESC LIMIT ?";
        List<ElderlyProfile> profiles = new ArrayList<>();
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                profiles.add(mapResultSetToElderlyProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding top profiles by rating: " + e.getMessage());
        }
        return profiles;
    }
    
    private ElderlyProfile mapResultSetToElderlyProfile(ResultSet rs) throws SQLException {
        ElderlyProfile profile = new ElderlyProfile();
        profile.setId(rs.getInt("id"));
        profile.setFullName(rs.getString("full_name"));
        profile.setAge(rs.getInt("age"));
        profile.setMedicalCondition(rs.getString("medical_condition"));
        profile.setRetirementVillage(rs.getString("retirement_village"));
        profile.setAddress(rs.getString("address"));
        profile.setPhoneNumber(rs.getString("phone_number"));
        profile.setEmergencyContact(rs.getString("emergency_contact"));
        profile.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        profile.setPreferences(rs.getString("preferences"));
        profile.setNotes(rs.getString("notes"));
        profile.setActive(rs.getBoolean("is_active"));
        profile.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        profile.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        profile.setTotalSessions(rs.getInt("total_sessions"));
        profile.setAverageRating(rs.getDouble("average_rating"));
        return profile;
    }
}
