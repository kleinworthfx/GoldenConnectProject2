package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.Request;
import java.time.LocalDateTime;
import java.util.List;

public interface RequestDAO {
    Request findById(int id);
    List<Request> findAll();
    List<Request> findByRequesterId(int requesterId);
    List<Request> findByVolunteerId(int volunteerId);
    List<Request> findByStatus(String status);
    List<Request> findByRequestType(String requestType);
    List<Request> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Request> findPendingRequests();
    List<Request> findCompletedRequests();
    boolean save(Request request);
    boolean update(Request request);
    boolean delete(int id);
    boolean updateStatus(int requestId, String status);
    boolean assignVolunteer(int requestId, int volunteerId);
    List<Request> findRequestsByDate(LocalDateTime date);
}
