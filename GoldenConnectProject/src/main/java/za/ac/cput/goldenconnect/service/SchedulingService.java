package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Request;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulingService {
    Activity scheduleActivity(Activity activity);
    Activity updateScheduledActivity(Activity activity);
    boolean cancelScheduledActivity(int activityId);
    Activity findScheduledActivityById(int activityId);
    List<Activity> findScheduledActivitiesByUser(int userId);
    List<Activity> findScheduledActivitiesByVolunteer(int volunteerId);
    List<Activity> findScheduledActivitiesByDate(LocalDateTime date);
    List<Activity> findScheduledActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Activity> findUpcomingActivities();
    List<Activity> findRecurringActivities();
    boolean updateActivityStatus(int activityId, String status);
    boolean updateActivityParticipants(int activityId, int currentParticipants);
    boolean checkAvailability(int volunteerId, LocalDateTime startTime, LocalDateTime endTime);
    List<LocalDateTime> findAvailableSlots(int volunteerId, LocalDateTime date);
    boolean rescheduleActivity(int activityId, LocalDateTime newStartTime, LocalDateTime newEndTime);
    List<Activity> findConflictingActivities(int volunteerId, LocalDateTime startTime, LocalDateTime endTime);
}
