package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.RequestService;
import za.ac.cput.goldenconnect.model.Request;
import za.ac.cput.goldenconnect.dao.RequestDAO;
import za.ac.cput.goldenconnect.dao.impl.RequestDAOImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class RequestServiceImpl implements RequestService {
    
    private RequestDAO requestDAO;
    
    public RequestServiceImpl() {
        this.requestDAO = new RequestDAOImpl();
    }
    
    @Override
    public Request createRequest(Request request) {
        try {
            // Validate request data
            if (!validateRequestData(request)) {
                return null;
            }
            
            // Set default values
            if (request.getStatus() == null) {
                request.setStatus("PENDING");
            }
            if (request.getRating() == 0.0) {
                request.setRating(0.0);
            }
            
            // Save to database
            boolean saved = requestDAO.save(request);
            
            if (saved) {
                System.out.println("Request created successfully: " + request.getId());
                return request;
            }
        } catch (Exception e) {
            System.err.println("Error creating request: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Request updateRequest(Request request) {
        try {
            // Validate request data
            if (request == null || request.getId() <= 0) {
                return null;
            }
            
            // Check if request exists
            Request existingRequest = requestDAO.findById(request.getId());
            if (existingRequest == null) {
                return null;
            }
            
            // Update request
            boolean updated = requestDAO.update(request);
            
            if (updated) {
                System.out.println("Request updated successfully: " + request.getId());
                return request;
            }
        } catch (Exception e) {
            System.err.println("Error updating request: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteRequest(int requestId) {
        try {
            // Check if request exists
            Request request = requestDAO.findById(requestId);
            if (request == null) {
                return false;
            }
            
            // Only allow deletion of pending requests
            if (!"PENDING".equals(request.getStatus())) {
                System.err.println("Cannot delete request that is not in PENDING status");
                return false;
            }
            
            return requestDAO.delete(requestId);
        } catch (Exception e) {
            System.err.println("Error deleting request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Request findRequestById(int requestId) {
        try {
            return requestDAO.findById(requestId);
        } catch (Exception e) {
            System.err.println("Error finding request by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Request> findRequestsByUser(int userId) {
        try {
            return requestDAO.findByRequesterId(userId);
        } catch (Exception e) {
            System.err.println("Error finding requests by user: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findRequestsByVolunteer(int volunteerId) {
        try {
            return requestDAO.findByVolunteerId(volunteerId);
        } catch (Exception e) {
            System.err.println("Error finding requests by volunteer: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findRequestsByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return requestDAO.findByStatus(status);
        } catch (Exception e) {
            System.err.println("Error finding requests by status: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findRequestsByType(String requestType) {
        try {
            if (requestType == null || requestType.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return requestDAO.findByRequestType(requestType);
        } catch (Exception e) {
            System.err.println("Error finding requests by type: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findRequestsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            if (startDate == null || endDate == null) {
                return new ArrayList<>();
            }
            return requestDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error finding requests by date range: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean assignVolunteer(int requestId, int volunteerId) {
        try {
            // Check if request exists and is in pending status
            Request request = requestDAO.findById(requestId);
            if (request == null || !"PENDING".equals(request.getStatus())) {
                return false;
            }
            
            // Assign volunteer and update status
            boolean assigned = requestDAO.assignVolunteer(requestId, volunteerId);
            
            if (assigned) {
                System.out.println("Volunteer " + volunteerId + " assigned to request " + requestId);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error assigning volunteer: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateRequestStatus(int requestId, String status) {
        try {
            // Validate status
            if (!isValidStatus(status)) {
                return false;
            }
            
            // Check if request exists
            Request request = requestDAO.findById(requestId);
            if (request == null) {
                return false;
            }
            
            // Update status
            boolean updated = requestDAO.updateStatus(requestId, status);
            
            if (updated) {
                System.out.println("Request " + requestId + " status updated to " + status);
                
                // If status is completed, update completion date
                if ("COMPLETED".equals(status)) {
                    request.setCompletedDate(LocalDateTime.now());
                    requestDAO.update(request);
                }
                
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating request status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean completeRequest(int requestId, String feedback, double rating) {
        try {
            // Find request
            Request request = requestDAO.findById(requestId);
            if (request == null) {
                return false;
            }
            
            // Update request with completion details
            request.setStatus("COMPLETED");
            request.setCompletedDate(LocalDateTime.now());
            request.setFeedback(feedback);
            request.setRating(rating);
            
            boolean updated = requestDAO.update(request);
            
            if (updated) {
                System.out.println("Request " + requestId + " completed with rating: " + rating);
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error completing request: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Request> findPendingRequests() {
        try {
            return requestDAO.findPendingRequests();
        } catch (Exception e) {
            System.err.println("Error finding pending requests: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findCompletedRequests() {
        try {
            return requestDAO.findCompletedRequests();
        } catch (Exception e) {
            System.err.println("Error finding completed requests: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Request> findRequestsByDate(LocalDateTime date) {
        try {
            if (date == null) {
                return new ArrayList<>();
            }
            return requestDAO.findRequestsByDate(date);
        } catch (Exception e) {
            System.err.println("Error finding requests by date: " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    /**
     * Validates request data
     */
    private boolean validateRequestData(Request request) {
        if (request == null) {
            return false;
        }
        
        // Validate required fields
        if (request.getRequesterId() <= 0) {
            return false;
        }
        
        if (request.getRequestType() == null || request.getRequestType().trim().isEmpty()) {
            return false;
        }
        
        if (!isValidRequestType(request.getRequestType())) {
            return false;
        }
        
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            return false;
        }
        
        if (request.getRequestedDate() == null) {
            return false;
        }
        
        // Validate duration
        if (request.getDuration() <= 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates request type
     */
    private boolean isValidRequestType(String requestType) {
        return "COMPANIONSHIP".equals(requestType) || 
               "TECH_HELP".equals(requestType) || 
               "ACTIVITY".equals(requestType) || 
               "HEALTH_CHECK".equals(requestType);
    }
    
    /**
     * Validates request status
     */
    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) || 
               "ACCEPTED".equals(status) || 
               "IN_PROGRESS".equals(status) || 
               "COMPLETED".equals(status) || 
               "CANCELLED".equals(status);
    }
}
