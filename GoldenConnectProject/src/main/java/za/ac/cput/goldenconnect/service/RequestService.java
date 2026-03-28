package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Request;
import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    Request createRequest(Request request);
    Request updateRequest(Request request);
    boolean deleteRequest(int requestId);
    Request findRequestById(int requestId);
    List<Request> findRequestsByUser(int userId);
    List<Request> findRequestsByVolunteer(int volunteerId);
    List<Request> findRequestsByStatus(String status);
    List<Request> findRequestsByType(String requestType);
    List<Request> findRequestsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    boolean assignVolunteer(int requestId, int volunteerId);
    boolean updateRequestStatus(int requestId, String status);
    boolean completeRequest(int requestId, String feedback, double rating);
    List<Request> findPendingRequests();
    List<Request> findCompletedRequests();
    List<Request> findRequestsByDate(LocalDateTime date);
}
