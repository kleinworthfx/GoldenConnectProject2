package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.Feedback;
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackDAO {
    Feedback findById(int id);
    List<Feedback> findAll();
    List<Feedback> findByUserId(int userId);
    List<Feedback> findByTargetId(int targetId);
    List<Feedback> findByTargetType(String targetType);
    List<Feedback> findByStatus(String status);
    List<Feedback> findByCategory(String category);
    List<Feedback> findByRating(int rating);
    List<Feedback> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Feedback> findPendingFeedback();
    List<Feedback> findResolvedFeedback();
    List<Feedback> findAnonymousFeedback();
    boolean save(Feedback feedback);
    boolean update(Feedback feedback);
    boolean delete(int id);
    boolean updateStatus(int feedbackId, String status);
    boolean updateResponse(int feedbackId, String response);
    boolean markAsResolved(int feedbackId);
    List<Feedback> findFeedbackByDate(LocalDateTime date);
}
