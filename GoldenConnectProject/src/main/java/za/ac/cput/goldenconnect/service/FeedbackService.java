package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Feedback;
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackService {
    Feedback createFeedback(Feedback feedback);
    Feedback updateFeedback(Feedback feedback);
    boolean deleteFeedback(int feedbackId);
    Feedback findFeedbackById(int feedbackId);
    List<Feedback> findAllFeedback();
    List<Feedback> findFeedbackByUser(int userId);
    List<Feedback> findFeedbackByTarget(int targetId, String targetType);
    List<Feedback> findFeedbackByStatus(String status);
    List<Feedback> findFeedbackByCategory(String category);
    List<Feedback> findFeedbackByRating(int rating);
    List<Feedback> findFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Feedback> findPendingFeedback();
    List<Feedback> findResolvedFeedback();
    List<Feedback> findAnonymousFeedback();
    boolean updateFeedbackStatus(int feedbackId, String status);
    boolean updateFeedbackResponse(int feedbackId, String response);
    boolean markFeedbackAsResolved(int feedbackId);
    double calculateAverageRating(int targetId, String targetType);
    List<Feedback> findFeedbackByDate(LocalDateTime date);
    boolean validateFeedback(Feedback feedback);
    List<Feedback> findFeedbackByTags(String tags);
}
