package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.Activity;
import java.time.LocalDateTime;
import java.util.List;

public interface ActivityDAO {
    Activity findById(int id);
    List<Activity> findAll();
    List<Activity> findByCategory(String category);
    List<Activity> findByType(String type);
    List<Activity> findByStatus(String status);
    List<Activity> findByVolunteerId(int volunteerId);
    List<Activity> findByDifficulty(String difficulty);
    List<Activity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Activity> findUpcomingActivities();
    List<Activity> findRecurringActivities();
    List<Activity> findActivitiesByLocation(String location);
    boolean save(Activity activity);
    boolean update(Activity activity);
    boolean delete(int id);
    boolean updateStatus(int activityId, String status);
    boolean updateParticipants(int activityId, int currentParticipants);
    boolean updateRating(int activityId, double rating);
    List<Activity> findActivitiesByDate(LocalDateTime date);
}
