package za.ac.cput.goldenconnect.service.impl;

import za.ac.cput.goldenconnect.service.ReportService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Request;
import za.ac.cput.goldenconnect.model.Feedback;
import za.ac.cput.goldenconnect.model.VideoTutorial;
import za.ac.cput.goldenconnect.dao.UserDAO;
import za.ac.cput.goldenconnect.dao.RequestDAO;
import za.ac.cput.goldenconnect.dao.ActivityDAO;
import za.ac.cput.goldenconnect.dao.FeedbackDAO;
import za.ac.cput.goldenconnect.dao.VideoTutorialDAO;
import za.ac.cput.goldenconnect.dao.impl.UserDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.RequestDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.ActivityDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.FeedbackDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.VideoTutorialDAOImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReportServiceImpl implements ReportService {
    
    private UserDAO userDAO;
    private RequestDAO requestDAO;
    private ActivityDAO activityDAO;
    private FeedbackDAO feedbackDAO;
    private VideoTutorialDAO videoTutorialDAO;
    
    public ReportServiceImpl() {
        this.userDAO = new UserDAOImpl();
        this.requestDAO = new RequestDAOImpl();
        this.activityDAO = new ActivityDAOImpl();
        this.feedbackDAO = new FeedbackDAOImpl();
        this.videoTutorialDAO = new VideoTutorialDAOImpl();
    }
    
    @Override
    public Map<String, Object> generateUserActivityReport(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            User user = userDAO.findById(userId);
            if (user != null) {
                report.put("userName", user.getName());
                report.put("userEmail", user.getEmail());
                report.put("userRole", user.getRole());
                report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                         endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                
                // Get user's requests
                List<Request> requests = requestDAO.findAll().stream()
                    .filter(r -> r.getRequesterId() == userId)
                    .filter(r -> r.getCreatedAt() != null && 
                               r.getCreatedAt().isAfter(startDate) && 
                               r.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                report.put("totalRequests", requests.size());
                report.put("completedRequests", requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count());
                report.put("pendingRequests", requests.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
                report.put("cancelledRequests", requests.stream().filter(r -> "CANCELLED".equals(r.getStatus())).count());
                
                // Get user's feedback
                List<Feedback> feedbacks = feedbackDAO.findAll().stream()
                    .filter(f -> f.getUserId() == userId)
                    .filter(f -> f.getCreatedAt() != null && 
                               f.getCreatedAt().isAfter(startDate) && 
                               f.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                report.put("totalFeedback", feedbacks.size());
                report.put("averageRating", feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0));
                
                report.put("generatedAt", LocalDateTime.now());
                report.put("status", "success");
            }
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateVolunteerPerformanceReport(int volunteerId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            User volunteer = userDAO.findById(volunteerId);
            if (volunteer != null && "VOLUNTEER".equals(volunteer.getRole())) {
                report.put("volunteerName", volunteer.getName());
                report.put("volunteerEmail", volunteer.getEmail());
                report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                         endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                
                // Get activities created by volunteer
                List<Activity> activities = activityDAO.findAll().stream()
                    .filter(a -> a.getVolunteerId() == volunteerId)
                    .collect(Collectors.toList());
                
                report.put("totalActivitiesCreated", activities.size());
                report.put("activeActivities", activities.stream().filter(a -> "SCHEDULED".equals(a.getStatus()) || "IN_PROGRESS".equals(a.getStatus())).count());
                
                // Get requests handled by volunteer
                List<Request> requests = requestDAO.findAll().stream()
                    .filter(r -> r.getRequesterId() == volunteerId)
                    .filter(r -> r.getCreatedAt() != null && 
                               r.getCreatedAt().isAfter(startDate) && 
                               r.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                report.put("totalRequestsHandled", requests.size());
                report.put("completedRequests", requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count());
                report.put("completionRate", requests.size() > 0 ? 
                    (double) requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count() / requests.size() * 100 : 0.0);
                
                report.put("generatedAt", LocalDateTime.now());
                report.put("status", "success");
            }
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateElderlyEngagementReport(int elderlyId, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            User elderly = userDAO.findById(elderlyId);
            if (elderly != null) {
                report.put("elderlyName", elderly.getName());
                report.put("elderlyEmail", elderly.getEmail());
                report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                         endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                
                // Get elderly's requests
                List<Request> requests = requestDAO.findAll().stream()
                    .filter(r -> r.getRequesterId() == elderlyId)
                    .filter(r -> r.getCreatedAt() != null && 
                               r.getCreatedAt().isAfter(startDate) && 
                               r.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                report.put("totalRequests", requests.size());
                report.put("completedRequests", requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count());
                report.put("engagementLevel", calculateEngagementLevel(requests.size()));
                
                // Get feedback given by elderly
                List<Feedback> feedbacks = feedbackDAO.findAll().stream()
                    .filter(f -> f.getUserId() == elderlyId)
                    .filter(f -> f.getCreatedAt() != null && 
                               f.getCreatedAt().isAfter(startDate) && 
                               f.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                report.put("totalFeedback", feedbacks.size());
                report.put("averageRating", feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0));
                
                report.put("generatedAt", LocalDateTime.now());
                report.put("status", "success");
            }
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateOverallSystemReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            // User statistics
            List<User> allUsers = userDAO.findAll();
            report.put("totalUsers", allUsers.size());
            report.put("totalVolunteers", allUsers.stream().filter(u -> "VOLUNTEER".equals(u.getRole())).count());
            report.put("totalElderly", allUsers.stream().filter(u -> "ELDERLY".equals(u.getRole())).count());
            report.put("totalAdmins", allUsers.stream().filter(u -> "ADMIN".equals(u.getRole())).count());
            
            // Request statistics
            List<Request> allRequests = requestDAO.findAll().stream()
                .filter(r -> r.getCreatedAt() != null && 
                           r.getCreatedAt().isAfter(startDate) && 
                           r.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
            
            report.put("totalRequests", allRequests.size());
            report.put("completedRequests", allRequests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count());
            report.put("pendingRequests", allRequests.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
            report.put("cancelledRequests", allRequests.stream().filter(r -> "CANCELLED".equals(r.getStatus())).count());
            
            // Activity statistics
            List<Activity> allActivities = activityDAO.findAll();
            report.put("totalActivities", allActivities.size());
            report.put("activeActivities", allActivities.stream().filter(a -> "SCHEDULED".equals(a.getStatus()) || "IN_PROGRESS".equals(a.getStatus())).count());
            
            // Feedback statistics
            List<Feedback> allFeedbacks = feedbackDAO.findAll().stream()
                .filter(f -> f.getCreatedAt() != null && 
                           f.getCreatedAt().isAfter(startDate) && 
                           f.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
            
            report.put("totalFeedback", allFeedbacks.size());
            report.put("averageRating", allFeedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0));
            
            // Tutorial statistics
            List<VideoTutorial> allTutorials = videoTutorialDAO.findAll();
            report.put("totalTutorials", allTutorials.size());
            report.put("activeTutorials", allTutorials.stream().filter(VideoTutorial::isActive).count());
            report.put("totalViews", allTutorials.stream().mapToInt(VideoTutorial::getViewCount).sum());
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateActivityParticipationReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            List<Activity> activities = activityDAO.findAll();
            List<Request> requests = requestDAO.findAll().stream()
                .filter(r -> r.getCreatedAt() != null && 
                           r.getCreatedAt().isAfter(startDate) && 
                           r.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
            
            // Activity participation by category
            Map<String, Long> activityByCategory = activities.stream()
                .collect(Collectors.groupingBy(Activity::getCategory, Collectors.counting()));
            report.put("activityByCategory", activityByCategory);
            
            // Most popular activities
            Map<String, Long> popularActivities = requests.stream()
                .collect(Collectors.groupingBy(Request::getRequestType, Collectors.counting()));
            report.put("popularActivities", popularActivities);
            
            // Participation trends
            report.put("totalParticipations", requests.size());
            report.put("uniqueParticipants", requests.stream().mapToInt(Request::getRequesterId).distinct().count());
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateFeedbackAnalyticsReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            List<Feedback> feedbacks = feedbackDAO.findAll().stream()
                .filter(f -> f.getCreatedAt() != null && 
                           f.getCreatedAt().isAfter(startDate) && 
                           f.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
            
            report.put("totalFeedback", feedbacks.size());
            report.put("averageRating", feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0));
            
            // Rating distribution
            Map<Integer, Long> ratingDistribution = feedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));
            report.put("ratingDistribution", ratingDistribution);
            
            // Feedback by category
            Map<String, Long> feedbackByCategory = feedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getCategory, Collectors.counting()));
            report.put("feedbackByCategory", feedbackByCategory);
            
            // Satisfaction score
            double satisfactionScore = feedbacks.stream()
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0) * 20; // Convert to percentage
            report.put("satisfactionScore", satisfactionScore);
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateMatchingEffectivenessReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            List<Request> requests = requestDAO.findAll().stream()
                .filter(r -> r.getCreatedAt() != null && 
                           r.getCreatedAt().isAfter(startDate) && 
                           r.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
            
            long totalMatches = requests.size();
            long successfulMatches = requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
            
            report.put("totalMatches", totalMatches);
            report.put("successfulMatches", successfulMatches);
            report.put("matchSuccessRate", totalMatches > 0 ? (double) successfulMatches / totalMatches * 100 : 0.0);
            
            // Match duration analysis
            double avgMatchDuration = requests.stream()
                .filter(r -> r.getCreatedAt() != null && r.getUpdatedAt() != null)
                .mapToDouble(r -> java.time.Duration.between(r.getCreatedAt(), r.getUpdatedAt()).toHours())
                .average()
                .orElse(0.0);
            report.put("averageMatchDuration", avgMatchDuration);
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateVideoTutorialUsageReport(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            List<VideoTutorial> tutorials = videoTutorialDAO.findAll();
            
            report.put("totalTutorials", tutorials.size());
            report.put("activeTutorials", tutorials.stream().filter(VideoTutorial::isActive).count());
            report.put("totalViews", tutorials.stream().mapToInt(VideoTutorial::getViewCount).sum());
            report.put("averageRating", tutorials.stream().mapToDouble(VideoTutorial::getRating).average().orElse(0.0));
            
            // Tutorial usage by category
            Map<String, Long> tutorialsByCategory = tutorials.stream()
                .collect(Collectors.groupingBy(VideoTutorial::getCategory, Collectors.counting()));
            report.put("tutorialsByCategory", tutorialsByCategory);
            
            // Most viewed tutorials
            List<VideoTutorial> topViewed = tutorials.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getViewCount(), t1.getViewCount()))
                .limit(5)
                .collect(Collectors.toList());
            report.put("topViewedTutorials", topViewed);
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public List<Map<String, Object>> generateTopPerformersReport(String category, int limit) {
        List<Map<String, Object>> report = new ArrayList<>();
        try {
            List<User> users = userDAO.findAll();
            
            if ("VOLUNTEER".equals(category)) {
                List<User> volunteers = users.stream()
                    .filter(u -> "VOLUNTEER".equals(u.getRole()))
                    .collect(Collectors.toList());
                
                for (User volunteer : volunteers) {
                    Map<String, Object> volunteerData = new HashMap<>();
                    volunteerData.put("name", volunteer.getName());
                    volunteerData.put("email", volunteer.getEmail());
                    
                    // Calculate performance metrics
                    List<Request> requests = requestDAO.findAll().stream()
                        .filter(r -> r.getRequesterId() == volunteer.getId())
                        .collect(Collectors.toList());
                    
                    long completedRequests = requests.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
                    double completionRate = requests.size() > 0 ? (double) completedRequests / requests.size() * 100 : 0.0;
                    
                    volunteerData.put("totalRequests", requests.size());
                    volunteerData.put("completedRequests", completedRequests);
                    volunteerData.put("completionRate", completionRate);
                    
                    report.add(volunteerData);
                }
                
                // Sort by completion rate and limit
                report.sort((a, b) -> Double.compare((Double) b.get("completionRate"), (Double) a.get("completionRate")));
                if (report.size() > limit) {
                    report = report.subList(0, limit);
                }
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("error", e.getMessage());
            report.add(error);
        }
        return report;
    }

    @Override
    public Map<String, Object> generateGeographicDistributionReport() {
        Map<String, Object> report = new HashMap<>();
        try {
            List<User> users = userDAO.findAll();
            
            // For now, we'll use a simple distribution since we don't have location data
            report.put("totalUsers", users.size());
            report.put("volunteers", users.stream().filter(u -> "VOLUNTEER".equals(u.getRole())).count());
            report.put("elderly", users.stream().filter(u -> "ELDERLY".equals(u.getRole())).count());
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateDemographicReport() {
        Map<String, Object> report = new HashMap<>();
        try {
            List<User> users = userDAO.findAll();
            
            // Age distribution
            Map<String, Long> ageDistribution = users.stream()
                .filter(u -> u.getDateOfBirth() != null)
                .collect(Collectors.groupingBy(u -> {
                    int age = LocalDateTime.now().getYear() - u.getDateOfBirth().getYear();
                    if (age < 25) return "18-24";
                    else if (age < 35) return "25-34";
                    else if (age < 50) return "35-49";
                    else if (age < 65) return "50-64";
                    else return "65+";
                }, Collectors.counting()));
            report.put("ageDistribution", ageDistribution);
            
            // Role distribution
            Map<String, Long> roleDistribution = users.stream()
                .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
            report.put("roleDistribution", roleDistribution);
            
            report.put("totalUsers", users.size());
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public Map<String, Object> generateTrendAnalysisReport(String metric, LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> report = new HashMap<>();
        try {
            report.put("metric", metric);
            report.put("reportPeriod", startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) + " - " + 
                                     endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            
            if ("requests".equals(metric)) {
                List<Request> requests = requestDAO.findAll().stream()
                    .filter(r -> r.getCreatedAt() != null && 
                               r.getCreatedAt().isAfter(startDate) && 
                               r.getCreatedAt().isBefore(endDate))
                    .collect(Collectors.toList());
                
                Map<String, Long> dailyRequests = requests.stream()
                    .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.counting()
                    ));
                report.put("trendData", dailyRequests);
            }
            
            report.put("generatedAt", LocalDateTime.now());
            report.put("status", "success");
        } catch (Exception e) {
            report.put("status", "error");
            report.put("error", e.getMessage());
        }
        return report;
    }

    @Override
    public boolean exportReportToCSV(Map<String, Object> report, String filePath) {
        // TODO: Implement CSV export logic
        return false;
    }

    @Override
    public boolean exportReportToPDF(Map<String, Object> report, String filePath) {
        // TODO: Implement PDF export logic
        return false;
    }

    @Override
    public boolean scheduleReportGeneration(String reportType, String frequency, String email) {
        // TODO: Implement scheduled report generation
        return false;
    }
    
    private String calculateEngagementLevel(int requestCount) {
        if (requestCount >= 10) return "High";
        else if (requestCount >= 5) return "Medium";
        else if (requestCount >= 1) return "Low";
        else return "None";
    }
}
