package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Activity;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityService {
    Activity createActivity(Activity activity);
    Activity updateActivity(Activity activity);
    boolean deleteActivity(int activityId);
    Activity findActivityById(int activityId);
    List<Activity> findAllActivities();
    List<Activity> findActivitiesByCategory(String category);
    List<Activity> findActivitiesByType(String type);
    List<Activity> findActivitiesByStatus(String status);
    List<Activity> findActivitiesByVolunteer(int volunteerId);
    List<Activity> findActivitiesByDifficulty(String difficulty);
    List<Activity> findActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Activity> findUpcomingActivities();
    List<Activity> findRecurringActivities();
    List<Activity> findActivitiesByLocation(String location);
    boolean updateActivityStatus(int activityId, String status);
    boolean updateActivityParticipants(int activityId, int currentParticipants);
    boolean updateActivityRating(int activityId, double rating);
    boolean joinActivity(int activityId, int userId);
    boolean leaveActivity(int activityId, int userId);
    List<Activity> findRecommendedActivities(int userId);
    List<Activity> searchActivities(String searchTerm);
}
