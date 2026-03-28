package za.ac.cput.goldenconnect.dao.impl;

import za.ac.cput.goldenconnect.dao.RequestDAO;
import za.ac.cput.goldenconnect.model.Request;
import za.ac.cput.goldenconnect.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDAOImpl implements RequestDAO {
    
    @Override
    public Request findById(int id) {
        String sql = "SELECT * FROM requests WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRequest(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding request by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Request> findAll() {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all requests: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findByRequesterId(int requesterId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE requester_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requesterId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by requester ID: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findByVolunteerId(int volunteerId) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE volunteer_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by volunteer ID: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findByStatus(String status) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by status: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findByRequestType(String requestType) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE request_type = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, requestType);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by type: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE requested_date BETWEEN ? AND ? ORDER BY requested_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by date range: " + e.getMessage());
        }
        return requests;
    }

    @Override
    public List<Request> findPendingRequests() {
        return findByStatus("PENDING");
    }

    @Override
    public List<Request> findCompletedRequests() {
        return findByStatus("COMPLETED");
    }

    @Override
    public boolean save(Request request) {
        String sql = "INSERT INTO requests (requester_id, volunteer_id, request_type, description, status, " +
                    "requested_date, scheduled_date, completed_date, location, duration, special_requirements, " +
                    "rating, feedback) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, request.getRequesterId());
            if (request.getVolunteerId() > 0) {
                stmt.setInt(2, request.getVolunteerId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, request.getRequestType());
            stmt.setString(4, request.getDescription());
            stmt.setString(5, request.getStatus());
            stmt.setTimestamp(6, request.getRequestedDate() != null ? Timestamp.valueOf(request.getRequestedDate()) : null);
            stmt.setTimestamp(7, request.getScheduledDate() != null ? Timestamp.valueOf(request.getScheduledDate()) : null);
            stmt.setTimestamp(8, request.getCompletedDate() != null ? Timestamp.valueOf(request.getCompletedDate()) : null);
            stmt.setString(9, request.getLocation());
            stmt.setInt(10, request.getDuration());
            stmt.setString(11, request.getSpecialRequirements());
            stmt.setDouble(12, request.getRating());
            stmt.setString(13, request.getFeedback());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    request.setId(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Request request) {
        String sql = "UPDATE requests SET requester_id = ?, volunteer_id = ?, request_type = ?, description = ?, " +
                    "status = ?, requested_date = ?, scheduled_date = ?, completed_date = ?, location = ?, " +
                    "duration = ?, special_requirements = ?, rating = ?, feedback = ?, updated_at = CURRENT_TIMESTAMP " +
                    "WHERE id = ?";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, request.getRequesterId());
            if (request.getVolunteerId() > 0) {
                stmt.setInt(2, request.getVolunteerId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, request.getRequestType());
            stmt.setString(4, request.getDescription());
            stmt.setString(5, request.getStatus());
            stmt.setTimestamp(6, request.getRequestedDate() != null ? Timestamp.valueOf(request.getRequestedDate()) : null);
            stmt.setTimestamp(7, request.getScheduledDate() != null ? Timestamp.valueOf(request.getScheduledDate()) : null);
            stmt.setTimestamp(8, request.getCompletedDate() != null ? Timestamp.valueOf(request.getCompletedDate()) : null);
            stmt.setString(9, request.getLocation());
            stmt.setInt(10, request.getDuration());
            stmt.setString(11, request.getSpecialRequirements());
            stmt.setDouble(12, request.getRating());
            stmt.setString(13, request.getFeedback());
            stmt.setInt(14, request.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM requests WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE requests SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean assignVolunteer(int requestId, int volunteerId) {
        String sql = "UPDATE requests SET volunteer_id = ?, status = 'ACCEPTED', updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning volunteer to request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Request> findRequestsByDate(LocalDateTime date) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests WHERE DATE(requested_date) = DATE(?) ORDER BY requested_date";
        
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(mapResultSetToRequest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding requests by date: " + e.getMessage());
        }
        return requests;
    }
    
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt("id"));
        request.setRequesterId(rs.getInt("requester_id"));
        request.setVolunteerId(rs.getInt("volunteer_id"));
        request.setRequestType(rs.getString("request_type"));
        request.setDescription(rs.getString("description"));
        request.setStatus(rs.getString("status"));
        
        Timestamp requestedDate = rs.getTimestamp("requested_date");
        if (requestedDate != null) {
            request.setRequestedDate(requestedDate.toLocalDateTime());
        }
        
        Timestamp scheduledDate = rs.getTimestamp("scheduled_date");
        if (scheduledDate != null) {
            request.setScheduledDate(scheduledDate.toLocalDateTime());
        }
        
        Timestamp completedDate = rs.getTimestamp("completed_date");
        if (completedDate != null) {
            request.setCompletedDate(completedDate.toLocalDateTime());
        }
        
        request.setLocation(rs.getString("location"));
        request.setDuration(rs.getInt("duration"));
        request.setSpecialRequirements(rs.getString("special_requirements"));
        request.setRating(rs.getDouble("rating"));
        request.setFeedback(rs.getString("feedback"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            request.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            request.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return request;
    }
}
