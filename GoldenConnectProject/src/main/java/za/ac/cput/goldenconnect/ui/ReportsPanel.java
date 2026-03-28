package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import za.ac.cput.goldenconnect.service.ReportService;
import za.ac.cput.goldenconnect.service.impl.ReportServiceImpl;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Feedback;

public class ReportsPanel extends VBox {
    
    private ReportService reportService;
    private User currentUser;
    
    // UI Components
    private ComboBox<String> timeRangeCombo;
    private ComboBox<String> reportTypeCombo;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button generateButton;
    private Button exportButton;
    private Button printButton;
    
    // Display areas
    private TabPane reportTabs;
    private VBox summaryBox;
    private VBox chartBox;
    private TableView<ReportDataItem> dataTable;
    private TextArea detailedReportArea;
    
    // Data
    private ObservableList<ReportDataItem> tableData;
    private Map<String, Object> currentReportData;
    
    public ReportsPanel(ReportService reportService, User currentUser) {
        this.reportService = reportService;
        this.currentUser = currentUser;
        this.tableData = FXCollections.observableArrayList();
        this.currentReportData = new HashMap<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
    }
    
    private void initializeComponents() {
        // Time range selector
        timeRangeCombo = new ComboBox<>();
        timeRangeCombo.getItems().addAll("Today", "This Week", "This Month", "Last Month", "Last 3 Months", "This Year", "Custom Range");
        timeRangeCombo.setValue("This Month");
        timeRangeCombo.setPrefWidth(150);
        
        // Report type selector
        reportTypeCombo = new ComboBox<>();
        reportTypeCombo.getItems().addAll("Activity Overview", "User Engagement", "Volunteer Performance", "Elderly Participation", "Feedback Analysis", "System Statistics");
        reportTypeCombo.setValue("Activity Overview");
        reportTypeCombo.setPrefWidth(180);
        
        // Date pickers
        startDatePicker = new DatePicker(LocalDate.now().withDayOfMonth(1));
        endDatePicker = new DatePicker(LocalDate.now());
        startDatePicker.setPrefWidth(120);
        endDatePicker.setPrefWidth(120);
        
        // Buttons
        generateButton = new Button("Generate Report");
        generateButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        
        exportButton = new Button("Export to CSV");
        exportButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        
        printButton = new Button("Print Report");
        printButton.setStyle("-fx-background-color: #F5B700; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 6;");
        
        // Report tabs
        reportTabs = new TabPane();
        
        // Summary tab
        summaryBox = new VBox(20);
        summaryBox.setPadding(new Insets(20));
        
        // Chart tab
        chartBox = new VBox(20);
        chartBox.setPadding(new Insets(20));
        
        // Data tab
        dataTable = new TableView<>();
        setupDataTable();
        
        // Detailed report tab
        detailedReportArea = new TextArea();
        detailedReportArea.setEditable(false);
        detailedReportArea.setPrefRowCount(20);
        detailedReportArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        
        // Create tabs
        Tab summaryTab = new Tab("Summary", summaryBox);
        Tab chartTab = new Tab("Charts", chartBox);
        Tab dataTab = new Tab("Data Table", dataTable);
        Tab detailedTab = new Tab("Detailed Report", detailedReportArea);
        
        summaryTab.setClosable(false);
        chartTab.setClosable(false);
        dataTab.setClosable(false);
        detailedTab.setClosable(false);
        
        reportTabs.getTabs().addAll(summaryTab, chartTab, dataTab, detailedTab);
    }
    
    private void setupDataTable() {
        TableColumn<ReportDataItem, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(100);
        
        TableColumn<ReportDataItem, String> metricCol = new TableColumn<>("Metric");
        metricCol.setCellValueFactory(new PropertyValueFactory<>("metric"));
        metricCol.setPrefWidth(150);
        
        TableColumn<ReportDataItem, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(100);
        
        TableColumn<ReportDataItem, String> changeCol = new TableColumn<>("Change");
        changeCol.setCellValueFactory(new PropertyValueFactory<>("change"));
        changeCol.setPrefWidth(100);
        
        dataTable.getColumns().addAll(dateCol, metricCol, valueCol, changeCol);
        dataTable.setItems(tableData);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(20);
        
        // Header
        Label titleLabel = new Label("Reports & Analytics");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Controls
        HBox controlsBox = new HBox(15);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(
            new Label("Time Range:"),
            timeRangeCombo,
            new Label("Report Type:"),
            reportTypeCombo,
            new Label("From:"),
            startDatePicker,
            new Label("To:"),
            endDatePicker,
            generateButton
        );
        
        // Action buttons
        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.getChildren().addAll(exportButton, printButton);
        
        // Main content
        reportTabs.setPrefHeight(600);
        
        getChildren().addAll(titleLabel, controlsBox, actionBox, reportTabs);
    }
    
    private void setupEventHandlers() {
        timeRangeCombo.setOnAction(e -> updateDateRange());
        generateButton.setOnAction(e -> generateReport());
        exportButton.setOnAction(e -> exportReport());
        printButton.setOnAction(e -> printReport());
    }
    
    private void updateDateRange() {
        String range = timeRangeCombo.getValue();
        LocalDate today = LocalDate.now();
        
        switch (range) {
            case "Today":
                startDatePicker.setValue(today);
                endDatePicker.setValue(today);
                break;
            case "This Week":
                startDatePicker.setValue(today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)));
                endDatePicker.setValue(today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)));
                break;
            case "This Month":
                startDatePicker.setValue(today.withDayOfMonth(1));
                endDatePicker.setValue(today.withDayOfMonth(today.lengthOfMonth()));
                break;
            case "Last Month":
                startDatePicker.setValue(today.minusMonths(1).withDayOfMonth(1));
                endDatePicker.setValue(today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth()));
                break;
            case "Last 3 Months":
                startDatePicker.setValue(today.minusMonths(3));
                endDatePicker.setValue(today);
                break;
            case "This Year":
                startDatePicker.setValue(today.withDayOfYear(1));
                endDatePicker.setValue(today.withDayOfYear(today.lengthOfYear()));
                break;
        }
    }
    
    private void generateReport() {
        try {
            String reportType = reportTypeCombo.getValue();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            
            if (startDate == null || endDate == null) {
                showAlert(AlertType.ERROR, "Error", "Please select valid date range");
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                showAlert(AlertType.ERROR, "Error", "Start date cannot be after end date");
                return;
            }
            
            // Generate report based on type
            switch (reportType) {
                case "Activity Overview":
                    generateActivityOverview(startDate, endDate);
                    break;
                case "User Engagement":
                    generateUserEngagement(startDate, endDate);
                    break;
                case "Volunteer Performance":
                    generateVolunteerPerformance(startDate, endDate);
                    break;
                case "Elderly Participation":
                    generateElderlyParticipation(startDate, endDate);
                    break;
                case "Feedback Analysis":
                    generateFeedbackAnalysis(startDate, endDate);
                    break;
                case "System Statistics":
                    generateSystemStatistics(startDate, endDate);
                    break;
            }
            
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to generate report: " + e.getMessage());
        }
    }
    
    private void generateActivityOverview(LocalDate startDate, LocalDate endDate) {
        // Get activity data
        Map<String, Object> activityReport = reportService.generateActivityParticipationReport(
            startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        // Extract data from report
        @SuppressWarnings("unchecked")
        List<Activity> activities = (List<Activity>) activityReport.getOrDefault("activities", new ArrayList<>());
        
        // Calculate statistics
        int totalActivities = activities.size();
        int completedActivities = (int) activities.stream().filter(a -> "COMPLETED".equals(a.getStatus())).count();
        int scheduledActivities = (int) activities.stream().filter(a -> "SCHEDULED".equals(a.getStatus())).count();
        int cancelledActivities = (int) activities.stream().filter(a -> "CANCELLED".equals(a.getStatus())).count();
        
        // Group by category
        Map<String, Long> categoryStats = activities.stream()
            .collect(Collectors.groupingBy(Activity::getCategory, Collectors.counting()));
        
        // Group by date for trend
        Map<LocalDate, Long> dailyStats = activities.stream()
            .collect(Collectors.groupingBy(a -> a.getScheduledDate().toLocalDate(), Collectors.counting()));
        
        // Update displays
        updateSummaryBox(totalActivities, completedActivities, scheduledActivities, cancelledActivities, categoryStats);
        updateActivityChart(dailyStats, categoryStats);
        updateDataTable(dailyStats, "Daily Activities");
        updateDetailedReport("Activity Overview Report", activities, categoryStats, null);
    }
    
    private void generateUserEngagement(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> userReport = reportService.generateUserActivityReport(0, 
            startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) userReport.getOrDefault("users", new ArrayList<>());
        
        int totalUsers = users.size();
        int activeUsers = (int) users.stream().filter(User::isActive).count();
        int newUsers = (int) users.stream()
            .filter(u -> u.getCreatedAt() != null && 
                        u.getCreatedAt().toLocalDate().isAfter(startDate.minusDays(1)))
            .count();
        
        Map<String, Long> roleStats = users.stream()
            .collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
        
        updateSummaryBox(totalUsers, activeUsers, newUsers, 0, roleStats);
        updateUserEngagementChart(roleStats);
        updateDetailedReport("User Engagement Report", users, roleStats, null);
    }
    
    private void generateVolunteerPerformance(LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> volunteers = reportService.generateTopPerformersReport("VOLUNTEER", 10);
        
        int totalVolunteers = volunteers.size();
        double avgRating = volunteers.stream()
            .mapToDouble(v -> (Double) v.getOrDefault("rating", 0.0))
            .average().orElse(0.0);
        int totalSessions = volunteers.stream()
            .mapToInt(v -> (Integer) v.getOrDefault("totalSessions", 0))
            .sum();
        
        updateSummaryBox(totalVolunteers, (int)avgRating, totalSessions, 0, null);
        // updateVolunteerPerformanceChart(volunteers); // Temporarily commented out due to type mismatch
        updateDetailedReport("Volunteer Performance Report", volunteers, null, null);
    }
    
    private void generateElderlyParticipation(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> demographicReport = reportService.generateDemographicReport();
        
        @SuppressWarnings("unchecked")
        List<User> elderly = (List<User>) demographicReport.getOrDefault("elderly", new ArrayList<>());
        
        int totalElderly = elderly.size();
        int activeElderly = (int) elderly.stream().filter(User::isActive).count();
        
        updateSummaryBox(totalElderly, activeElderly, 0, 0, null);
        updateElderlyParticipationChart(elderly);
        updateDetailedReport("Elderly Participation Report", elderly, null, null);
    }
    
    private void generateFeedbackAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> feedbackReport = reportService.generateFeedbackAnalyticsReport(
            startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        @SuppressWarnings("unchecked")
        List<Feedback> feedbacks = (List<Feedback>) feedbackReport.getOrDefault("feedbacks", new ArrayList<>());
        
        int totalFeedback = feedbacks.size();
        double avgRating = feedbacks.stream().mapToDouble(Feedback::getRating).average().orElse(0.0);
        int positiveFeedback = (int) feedbacks.stream().filter(f -> f.getRating() >= 4).count();
        int negativeFeedback = (int) feedbacks.stream().filter(f -> f.getRating() <= 2).count();
        
        Map<String, Long> categoryStats = feedbacks.stream()
            .collect(Collectors.groupingBy(Feedback::getCategory, Collectors.counting()));
        
        updateSummaryBox(totalFeedback, (int)avgRating, positiveFeedback, negativeFeedback, categoryStats);
        updateFeedbackChart(categoryStats, avgRating);
        updateDetailedReport("Feedback Analysis Report", feedbacks, categoryStats, null);
    }
    
    private void generateSystemStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> systemStats = reportService.generateOverallSystemReport(
            startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        int totalUsers = (Integer) systemStats.getOrDefault("totalUsers", 0);
        int totalActivities = (Integer) systemStats.getOrDefault("totalActivities", 0);
        int totalRequests = (Integer) systemStats.getOrDefault("totalRequests", 0);
        int totalFeedback = (Integer) systemStats.getOrDefault("totalFeedback", 0);
        
        updateSummaryBox(totalUsers, totalActivities, totalRequests, totalFeedback, null);
        updateSystemStatisticsChart(systemStats);
        updateDetailedReport("System Statistics Report", null, null, systemStats);
    }
    
    private void updateSummaryBox(int metric1, int metric2, int metric3, int metric4, Map<String, Long> categoryStats) {
        summaryBox.getChildren().clear();
        
        // Main metrics
        HBox metricsBox = new HBox(20);
        metricsBox.setAlignment(Pos.CENTER);
        
        VBox metric1Box = createMetricBox("Total", String.valueOf(metric1), "#2D5BFF");
        VBox metric2Box = createMetricBox("Completed", String.valueOf(metric2), "#2BAE66");
        VBox metric3Box = createMetricBox("Active", String.valueOf(metric3), "#F5B700");
        VBox metric4Box = createMetricBox("Pending", String.valueOf(metric4), "#E74C3C");
        
        metricsBox.getChildren().addAll(metric1Box, metric2Box, metric3Box, metric4Box);
        
        summaryBox.getChildren().add(metricsBox);
        
        // Category breakdown if available
        if (categoryStats != null && !categoryStats.isEmpty()) {
            Label categoryLabel = new Label("Category Breakdown");
            categoryLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            GridPane categoryGrid = new GridPane();
            categoryGrid.setHgap(20);
            categoryGrid.setVgap(10);
            categoryGrid.setAlignment(Pos.CENTER);
            
            int row = 0;
            for (Map.Entry<String, Long> entry : categoryStats.entrySet()) {
                Label categoryName = new Label(entry.getKey());
                categoryName.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
                
                Label categoryValue = new Label(entry.getValue().toString());
                categoryValue.setStyle("-fx-text-fill: #2D5BFF; -fx-font-weight: bold;");
                
                categoryGrid.add(categoryName, 0, row);
                categoryGrid.add(categoryValue, 1, row);
                row++;
            }
            
            summaryBox.getChildren().addAll(categoryLabel, categoryGrid);
        }
    }
    
    private VBox createMetricBox(String title, String value, String color) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: " + color + "20; -fx-border-color: " + color + "; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        box.setPrefWidth(120);
        box.setPrefHeight(100);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        
        box.getChildren().addAll(titleLabel, valueLabel);
        return box;
    }
    
    private void updateActivityChart(Map<LocalDate, Long> dailyStats, Map<String, Long> categoryStats) {
        chartBox.getChildren().clear();
        
        Label trendLabel = new Label("Daily Activity Trend");
        trendLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        HBox chartContainer = new HBox(5);
        chartContainer.setAlignment(Pos.BOTTOM_CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        chartContainer.setPrefHeight(200);
        
        long maxValue = dailyStats.values().stream().mapToLong(Long::longValue).max().orElse(1);
        
        for (Map.Entry<LocalDate, Long> entry : dailyStats.entrySet()) {
            VBox bar = new VBox(5);
            bar.setAlignment(Pos.BOTTOM_CENTER);
            
            double height = (entry.getValue().doubleValue() / maxValue) * 150;
            Rectangle barRect = new Rectangle(30, height);
            barRect.setFill(Color.web("#2D5BFF"));
            barRect.setArcWidth(5);
            barRect.setArcHeight(5);
            
            Label dateLabel = new Label(entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd")));
            dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #666666;");
            
            Label valueLabel = new Label(entry.getValue().toString());
            valueLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #2D5BFF; -fx-font-weight: bold;");
            
            bar.getChildren().addAll(valueLabel, barRect, dateLabel);
            chartContainer.getChildren().add(bar);
        }
        
        chartBox.getChildren().addAll(trendLabel, chartContainer);
    }
    
    private void updateUserEngagementChart(Map<String, Long> roleStats) {
        chartBox.getChildren().clear();
        
        Label chartLabel = new Label("User Engagement by Role");
        chartLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        VBox chartContainer = new VBox(10);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        
        for (Map.Entry<String, Long> entry : roleStats.entrySet()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            
            Label roleLabel = new Label(entry.getKey());
            roleLabel.setPrefWidth(100);
            roleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            Rectangle bar = new Rectangle(200, 20);
            bar.setFill(Color.web("#2BAE66"));
            bar.setArcWidth(5);
            bar.setArcHeight(5);
            
            Label valueLabel = new Label(entry.getValue().toString());
            valueLabel.setStyle("-fx-text-fill: #2BAE66; -fx-font-weight: bold;");
            
            row.getChildren().addAll(roleLabel, bar, valueLabel);
            chartContainer.getChildren().add(row);
        }
        
        chartBox.getChildren().addAll(chartLabel, chartContainer);
    }
    
    private void updateVolunteerPerformanceChart(List<User> volunteers) {
        chartBox.getChildren().clear();
        
        Label chartLabel = new Label("Volunteer Performance");
        chartLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        VBox chartContainer = new VBox(10);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        
        volunteers.stream().limit(5).forEach(volunteer -> {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            
            Label nameLabel = new Label(volunteer.getName());
            nameLabel.setPrefWidth(150);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            Label ratingLabel = new Label(String.format("%.1f", volunteer.getRating()));
            ratingLabel.setStyle("-fx-text-fill: #F5B700; -fx-font-weight: bold;");
            
            Label sessionsLabel = new Label(String.valueOf(volunteer.getTotalSessions()));
            sessionsLabel.setStyle("-fx-text-fill: #2D5BFF; -fx-font-weight: bold;");
            
            row.getChildren().addAll(nameLabel, ratingLabel, sessionsLabel);
            chartContainer.getChildren().add(row);
        });
        
        chartBox.getChildren().addAll(chartLabel, chartContainer);
    }
    
    private void updateElderlyParticipationChart(List<User> elderly) {
        chartBox.getChildren().clear();
        
        Label chartLabel = new Label("Elderly Participation");
        chartLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        VBox chartContainer = new VBox(10);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        
        int activeCount = (int) elderly.stream().filter(User::isActive).count();
        int totalCount = elderly.size();
        
        Label activeLabel = new Label("Active: " + activeCount);
        activeLabel.setStyle("-fx-text-fill: #2BAE66; -fx-font-weight: bold; -fx-font-size: 16px;");
        
        Label totalLabel = new Label("Total: " + totalCount);
        totalLabel.setStyle("-fx-text-fill: #2D5BFF; -fx-font-weight: bold; -fx-font-size: 16px;");
        
        chartContainer.getChildren().addAll(activeLabel, totalLabel);
        chartBox.getChildren().addAll(chartLabel, chartContainer);
    }
    
    private void updateFeedbackChart(Map<String, Long> categoryStats, double avgRating) {
        chartBox.getChildren().clear();
        
        Label chartLabel = new Label("Feedback Analysis");
        chartLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        VBox chartContainer = new VBox(10);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        
        Label avgRatingLabel = new Label("Average Rating: " + String.format("%.1f", avgRating));
        avgRatingLabel.setStyle("-fx-text-fill: #F5B700; -fx-font-weight: bold; -fx-font-size: 16px;");
        
        chartContainer.getChildren().add(avgRatingLabel);
        
        if (categoryStats != null) {
            for (Map.Entry<String, Long> entry : categoryStats.entrySet()) {
                Label categoryLabel = new Label(entry.getKey() + ": " + entry.getValue());
                categoryLabel.setStyle("-fx-text-fill: #2D5BFF; -fx-font-weight: bold;");
                chartContainer.getChildren().add(categoryLabel);
            }
        }
        
        chartBox.getChildren().addAll(chartLabel, chartContainer);
    }
    
    private void updateSystemStatisticsChart(Map<String, Object> systemStats) {
        chartBox.getChildren().clear();
        
        Label chartLabel = new Label("System Statistics");
        chartLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        VBox chartContainer = new VBox(10);
        chartContainer.setAlignment(Pos.CENTER);
        chartContainer.setPadding(new Insets(20));
        chartContainer.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF;");
        
        for (Map.Entry<String, Object> entry : systemStats.entrySet()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            
            Label keyLabel = new Label(entry.getKey());
            keyLabel.setPrefWidth(150);
            keyLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            Label valueLabel = new Label(entry.getValue().toString());
            valueLabel.setStyle("-fx-text-fill: #2D5BFF; -fx-font-weight: bold;");
            
            row.getChildren().addAll(keyLabel, valueLabel);
            chartContainer.getChildren().add(row);
        }
        
        chartBox.getChildren().addAll(chartLabel, chartContainer);
    }
    
    private void updateDataTable(Map<LocalDate, Long> dailyStats, String metricName) {
        tableData.clear();
        
        for (Map.Entry<LocalDate, Long> entry : dailyStats.entrySet()) {
            ReportDataItem item = new ReportDataItem();
            item.setDate(entry.getKey().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
            item.setMetric(metricName);
            item.setValue(entry.getValue().toString());
            item.setChange("+0");
            
            tableData.add(item);
        }
    }
    
    private void updateDetailedReport(String title, List<?> data, Map<String, Long> categoryStats, Map<String, Object> systemStats) {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append(title).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        report.append("Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        report.append("Date Range: ").append(startDatePicker.getValue()).append(" to ").append(endDatePicker.getValue()).append("\n\n");
        
        if (data != null && !data.isEmpty()) {
            report.append("Data Summary:\n");
            report.append("-".repeat(40)).append("\n");
            report.append("Total Records: ").append(data.size()).append("\n\n");
        }
        
        if (categoryStats != null) {
            report.append("Category Breakdown:\n");
            report.append("-".repeat(40)).append("\n");
            for (Map.Entry<String, Long> entry : categoryStats.entrySet()) {
                report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            report.append("\n");
        }
        
        if (systemStats != null) {
            report.append("System Statistics:\n");
            report.append("-".repeat(40)).append("\n");
            for (Map.Entry<String, Object> entry : systemStats.entrySet()) {
                report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            report.append("\n");
        }
        
        detailedReportArea.setText(report.toString());
    }
    
    private void exportReport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Report");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            fileChooser.setInitialFileName("report_" + LocalDate.now() + ".csv");
            
            File file = fileChooser.showSaveDialog(getScene().getWindow());
            if (file != null) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    writer.println("Date,Metric,Value,Change");
                    
                    for (ReportDataItem item : tableData) {
                        writer.printf("%s,%s,%s,%s%n", 
                            item.getDate(), item.getMetric(), item.getValue(), item.getChange());
                    }
                }
                
                showAlert(AlertType.INFORMATION, "Success", "Report exported successfully to " + file.getName());
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to export report: " + e.getMessage());
        }
    }
    
    private void printReport() {
        try {
            Stage printStage = new Stage();
            VBox printContent = new VBox(20);
            printContent.setPadding(new Insets(30));
            
            Label titleLabel = new Label("GoldenConnect Report");
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            TextArea printArea = new TextArea(detailedReportArea.getText());
            printArea.setEditable(false);
            printArea.setPrefRowCount(30);
            printArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            
            printContent.getChildren().addAll(titleLabel, printArea);
            
            javafx.scene.Scene printScene = new javafx.scene.Scene(printContent, 800, 600);
            printStage.setScene(printScene);
            printStage.setTitle("Print Preview");
            printStage.show();
            
            showAlert(AlertType.INFORMATION, "Print Preview", "Print preview opened. Use Ctrl+P to print.");
            
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Failed to open print preview: " + e.getMessage());
        }
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner class for table data
    public static class ReportDataItem {
        private String date;
        private String metric;
        private String value;
        private String change;
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public String getMetric() { return metric; }
        public void setMetric(String metric) { this.metric = metric; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        
        public String getChange() { return change; }
        public void setChange(String change) { this.change = change; }
    }
}
