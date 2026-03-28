package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.User;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    boolean sendNotification(int userId, String message, String type);
    boolean sendReminder(int userId, String message, LocalDateTime reminderTime);
    boolean sendEmailNotification(String email, String subject, String message);
    boolean sendSMSNotification(String phoneNumber, String message);
    boolean sendPushNotification(int userId, String title, String message);
    List<String> getUserNotifications(int userId);
    List<String> getUserNotificationsByType(int userId, String type);
    boolean markNotificationAsRead(int notificationId);
    boolean markAllNotificationsAsRead(int userId);
    boolean deleteNotification(int notificationId);
    boolean deleteAllNotifications(int userId);
    boolean scheduleReminder(int userId, String message, LocalDateTime reminderTime);
    boolean cancelReminder(int reminderId);
    List<String> findOverdueReminders();
    boolean sendBulkNotification(List<Integer> userIds, String message, String type);
    boolean sendActivityReminder(int activityId, int userId);
    boolean sendHealthCheckReminder(int userId);
}
