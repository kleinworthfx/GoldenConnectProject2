package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.SchedulingService;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.dao.ActivityDAO;
import za.ac.cput.goldenconnect.dao.impl.ActivityDAOImpl;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class SchedulingServiceImpl implements SchedulingService {
    
    private ActivityDAO activityDAO;
    
    public SchedulingServiceImpl() {
        this.activityDAO = new ActivityDAOImpl();
    }
    
    @Override
    public Activity scheduleActivity(Activity activity) {
        try {
            // Validate activity data
            if (activity.getName() == null || activity.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Activity name is required");
            }
            
            if (activity.getScheduledDate() == null) {
                throw new IllegalArgumentException("Scheduled date is required");
            }
            
            if (activity.getStartTime() == null || activity.getEndTime() == null) {
                throw new IllegalArgumentException("Start and end times are required");
            }
            
            if (activity.getStartTime().isAfter(activity.getEndTime())) {
                throw new IllegalArgumentException("Start time cannot be after end time");
            }
            
            // Check for conflicts
            if (!checkAvailability(activity.getVolunteerId(), activity.getStartTime(), activity.getEndTime())) {
                throw new IllegalArgumentException("Time slot conflicts with existing activities");
            }
            
            // Set default values
            activity.setStatus("SCHEDULED");
            activity.setCurrentParticipants(0);
            activity.setCreatedAt(LocalDateTime.now());
            activity.setUpdatedAt(LocalDateTime.now());
            
            // Save to database
            boolean saved = activityDAO.save(activity);
            if (saved) {
                return activity;
            } else {
                throw new RuntimeException("Failed to save activity to database");
            }
        } catch (Exception e) {
            System.err.println("Error scheduling activity: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Activity updateScheduledActivity(Activity activity) {
        try {
            // Validate activity exists
            Activity existingActivity = activityDAO.findById(activity.getId());
            if (existingActivity == null) {
                throw new IllegalArgumentException("Activity not found");
            }
            
            // Check for conflicts if time changed
            if (!existingActivity.getStartTime().equals(activity.getStartTime()) || 
                !existingActivity.getEndTime().equals(activity.getEndTime())) {
                if (!checkAvailability(activity.getVolunteerId(), activity.getStartTime(), activity.getEndTime())) {
                    throw new IllegalArgumentException("Time slot conflicts with existing activities");
                }
            }
            
            // Update timestamp
            activity.setUpdatedAt(LocalDateTime.now());
            
            // Update in database
            boolean updated = activityDAO.update(activity);
            if (updated) {
                return activity;
            } else {
                throw new RuntimeException("Failed to update activity in database");
            }
        } catch (Exception e) {
            System.err.println("Error updating activity: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean cancelScheduledActivity(int activityId) {
        try {
            Activity activity = activityDAO.findById(activityId);
            if (activity == null) {
                throw new IllegalArgumentException("Activity not found");
            }
            
            // Update status to cancelled
            activity.setStatus("CANCELLED");
            activity.setUpdatedAt(LocalDateTime.now());
            
            return activityDAO.update(activity);
        } catch (Exception e) {
            System.err.println("Error cancelling activity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Activity findScheduledActivityById(int activityId) {
        try {
            return activityDAO.findById(activityId);
        } catch (Exception e) {
            System.err.println("Error finding activity by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Activity> findScheduledActivitiesByUser(int userId) {
        try {
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getVolunteerId() == userId)
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getScheduledDate().compareTo(a2.getScheduledDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding activities by user: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Activity> findScheduledActivitiesByVolunteer(int volunteerId) {
        try {
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getVolunteerId() == volunteerId)
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getScheduledDate().compareTo(a2.getScheduledDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding activities by volunteer: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Activity> findScheduledActivitiesByDate(LocalDateTime date) {
        try {
            LocalDate targetDate = date.toLocalDate();
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getScheduledDate() != null && 
                                  activity.getScheduledDate().toLocalDate().equals(targetDate))
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding activities by date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Activity> findScheduledActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getScheduledDate() != null && 
                                  !activity.getScheduledDate().isBefore(startDate) && 
                                  !activity.getScheduledDate().isAfter(endDate))
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getScheduledDate().compareTo(a2.getScheduledDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding activities by date range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Activity> findUpcomingActivities() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getScheduledDate() != null && 
                                  activity.getScheduledDate().isAfter(now))
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getScheduledDate().compareTo(a2.getScheduledDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding upcoming activities: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Activity> findRecurringActivities() {
        try {
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(Activity::isRecurring)
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .sorted((a1, a2) -> a1.getScheduledDate().compareTo(a2.getScheduledDate()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding recurring activities: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateActivityStatus(int activityId, String status) {
        try {
            Activity activity = activityDAO.findById(activityId);
            if (activity == null) {
                return false;
            }
            
            activity.setStatus(status);
            activity.setUpdatedAt(LocalDateTime.now());
            
            return activityDAO.update(activity);
        } catch (Exception e) {
            System.err.println("Error updating activity status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateActivityParticipants(int activityId, int currentParticipants) {
        try {
            Activity activity = activityDAO.findById(activityId);
            if (activity == null) {
                return false;
            }
            
            if (currentParticipants > activity.getMaxParticipants()) {
                throw new IllegalArgumentException("Current participants cannot exceed maximum participants");
            }
            
            activity.setCurrentParticipants(currentParticipants);
            activity.setUpdatedAt(LocalDateTime.now());
            
            return activityDAO.update(activity);
        } catch (Exception e) {
            System.err.println("Error updating activity participants: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkAvailability(int volunteerId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            List<Activity> conflictingActivities = findConflictingActivities(volunteerId, startTime, endTime);
            return conflictingActivities.isEmpty();
        } catch (Exception e) {
            System.err.println("Error checking availability: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<LocalDateTime> findAvailableSlots(int volunteerId, LocalDateTime date) {
        try {
            List<LocalDateTime> availableSlots = new ArrayList<>();
            LocalDate targetDate = date.toLocalDate();
            
            // Define business hours (9 AM to 5 PM)
            LocalTime startHour = LocalTime.of(9, 0);
            LocalTime endHour = LocalTime.of(17, 0);
            
            // Get existing activities for the volunteer on this date
            List<Activity> existingActivities = findScheduledActivitiesByDate(date);
            List<Activity> volunteerActivities = existingActivities.stream()
                .filter(activity -> activity.getVolunteerId() == volunteerId)
                .collect(Collectors.toList());
            
            // Generate 1-hour slots
            LocalDateTime slotStart = date.with(startHour);
            LocalDateTime endOfDay = date.with(endHour);
            
            while (slotStart.isBefore(endOfDay)) {
                final LocalDateTime currentSlot = slotStart;
                LocalDateTime slotEnd = currentSlot.plusHours(1);
                
                // Check if this slot conflicts with existing activities
                boolean isAvailable = volunteerActivities.stream()
                    .noneMatch(activity -> 
                        (currentSlot.isBefore(activity.getEndTime()) && 
                         slotEnd.isAfter(activity.getStartTime())));
                
                if (isAvailable) {
                    availableSlots.add(currentSlot);
                }
                
                slotStart = slotEnd;
            }
            
            return availableSlots;
        } catch (Exception e) {
            System.err.println("Error finding available slots: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean rescheduleActivity(int activityId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        try {
            Activity activity = activityDAO.findById(activityId);
            if (activity == null) {
                throw new IllegalArgumentException("Activity not found");
            }
            
            // Check availability for new time slot
            if (!checkAvailability(activity.getVolunteerId(), newStartTime, newEndTime)) {
                throw new IllegalArgumentException("New time slot conflicts with existing activities");
            }
            
            // Update times
            activity.setScheduledDate(newStartTime);
            activity.setStartTime(newStartTime);
            activity.setEndTime(newEndTime);
            activity.setUpdatedAt(LocalDateTime.now());
            
            return activityDAO.update(activity);
        } catch (Exception e) {
            System.err.println("Error rescheduling activity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Activity> findConflictingActivities(int volunteerId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            List<Activity> allActivities = activityDAO.findAll();
            return allActivities.stream()
                .filter(activity -> activity.getVolunteerId() == volunteerId)
                .filter(activity -> !"CANCELLED".equals(activity.getStatus()))
                .filter(activity -> 
                    (startTime.isBefore(activity.getEndTime()) && 
                     endTime.isAfter(activity.getStartTime())))
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error finding conflicting activities: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
