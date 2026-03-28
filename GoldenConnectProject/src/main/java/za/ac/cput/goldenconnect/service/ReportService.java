package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Request;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, Object> generateUserActivityReport(int userId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateVolunteerPerformanceReport(int volunteerId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateElderlyEngagementReport(int elderlyId, LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateOverallSystemReport(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateActivityParticipationReport(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateFeedbackAnalyticsReport(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateMatchingEffectivenessReport(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> generateVideoTutorialUsageReport(LocalDateTime startDate, LocalDateTime endDate);
    List<Map<String, Object>> generateTopPerformersReport(String category, int limit);
    Map<String, Object> generateGeographicDistributionReport();
    Map<String, Object> generateDemographicReport();
    Map<String, Object> generateTrendAnalysisReport(String metric, LocalDateTime startDate, LocalDateTime endDate);
    boolean exportReportToCSV(Map<String, Object> report, String filePath);
    boolean exportReportToPDF(Map<String, Object> report, String filePath);
    boolean scheduleReportGeneration(String reportType, String frequency, String email);
}
