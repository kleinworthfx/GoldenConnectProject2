package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import za.ac.cput.goldenconnect.service.SchedulingService;
import za.ac.cput.goldenconnect.service.impl.SchedulingServiceImpl;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;

public class SchedulePanel extends VBox {
    
    private SchedulingService schedulingService;
    private User currentUser;
    
    // Calendar view components
    private ComboBox<String> viewTypeCombo;
    private ComboBox<String> filterCombo;
    private Button previousButton;
    private Button nextButton;
    private Button todayButton;
    private Label currentPeriodLabel;
    
    // Calendar display
    private GridPane calendarGrid;
    private ListView<ActivityItem> dayViewList;
    private GridPane weekViewGrid;
    private GridPane monthViewGrid;
    private StackPane calendarContainer;
    
    // Current date tracking
    private LocalDate currentDate;
    private LocalDate displayDate;
    private String currentViewType = "Month";
    
    // Activity data
    private ObservableList<ActivityItem> dayViewData;
    private List<Activity> currentActivities;
    
    // Add activity components
    private Button addActivityButton;
    private Dialog<Activity> addActivityDialog;
    
    public SchedulePanel(SchedulingService schedulingService, User currentUser) {
        this.schedulingService = schedulingService;
        this.currentUser = currentUser;
        this.currentDate = LocalDate.now();
        this.displayDate = LocalDate.now();
        this.dayViewData = FXCollections.observableArrayList();
        this.currentActivities = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCalendarData();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadCalendarData();
    }
    
    private void initializeComponents() {
        // View type selector
        viewTypeCombo = new ComboBox<>();
        viewTypeCombo.getItems().addAll("Day", "Week", "Month");
        viewTypeCombo.setValue("Month");
        viewTypeCombo.setPrefWidth(120);
        
        // Filter selector
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All Activities", "My Activities", "Today", "This Week", "This Month");
        filterCombo.setValue("All Activities");
        filterCombo.setPrefWidth(150);
        
        // Navigation buttons
        previousButton = new Button("◀");
        previousButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 12; -fx-background-radius: 6;");
        
        nextButton = new Button("▶");
        nextButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 8 12; -fx-background-radius: 6;");
        
        todayButton = new Button("Today");
        todayButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        
        // Current period label
        currentPeriodLabel = new Label();
        currentPeriodLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Add activity button
        addActivityButton = new Button("+ Add Activity");
        addActivityButton.setStyle("-fx-background-color: #F5B700; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        
        // Calendar components
        calendarGrid = new GridPane();
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        calendarGrid.setAlignment(Pos.CENTER);
        
        dayViewList = new ListView<>();
        dayViewList.setPrefHeight(500);
        dayViewList.setCellFactory(param -> new ActivityListCell());
        
        weekViewGrid = new GridPane();
        weekViewGrid.setHgap(2);
        weekViewGrid.setVgap(2);
        weekViewGrid.setAlignment(Pos.CENTER);
        
        monthViewGrid = new GridPane();
        monthViewGrid.setHgap(2);
        monthViewGrid.setVgap(2);
        monthViewGrid.setAlignment(Pos.CENTER);
        
        calendarContainer = new StackPane();
        calendarContainer.setPrefHeight(500);
        
        // Initialize add activity dialog
        initializeAddActivityDialog();
    }
    
    private void initializeAddActivityDialog() {
        addActivityDialog = new Dialog<>();
        addActivityDialog.setTitle("Add New Activity");
        addActivityDialog.setHeaderText("Schedule a new activity");
        
        DialogPane dialogPane = addActivityDialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Form components
        TextField nameField = new TextField();
        nameField.setPromptText("Activity name");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("COMPANIONSHIP", "TECH_HELP", "LEARNING", "HEALTH", "ENTERTAINMENT");
        categoryCombo.setPromptText("Category");
        
        DatePicker datePicker = new DatePicker(displayDate);
        
        ComboBox<String> startTimeCombo = new ComboBox<>();
        startTimeCombo.getItems().addAll("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
        startTimeCombo.setPromptText("Start time");
        
        ComboBox<String> endTimeCombo = new ComboBox<>();
        endTimeCombo.getItems().addAll("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");
        endTimeCombo.setPromptText("End time");
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefRowCount(3);
        
        // Form layout
        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(10);
        formGrid.setPadding(new Insets(20));
        
        formGrid.add(new Label("Name:"), 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(new Label("Category:"), 0, 1);
        formGrid.add(categoryCombo, 1, 1);
        formGrid.add(new Label("Date:"), 0, 2);
        formGrid.add(datePicker, 1, 2);
        formGrid.add(new Label("Start Time:"), 0, 3);
        formGrid.add(startTimeCombo, 1, 3);
        formGrid.add(new Label("End Time:"), 0, 4);
        formGrid.add(endTimeCombo, 1, 4);
        formGrid.add(new Label("Description:"), 0, 5);
        formGrid.add(descriptionArea, 1, 5);
        
        dialogPane.setContent(formGrid);
        
        // Handle OK button
        addActivityDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    Activity activity = new Activity();
                    activity.setName(nameField.getText().trim());
                    activity.setCategory(categoryCombo.getValue());
                    activity.setDescription(descriptionArea.getText().trim());
                    activity.setVolunteerId(currentUser.getId());
                    
                    LocalDate selectedDate = datePicker.getValue();
                    LocalTime startTime = LocalTime.parse(startTimeCombo.getValue());
                    LocalTime endTime = LocalTime.parse(endTimeCombo.getValue());
                    
                    LocalDateTime startDateTime = selectedDate.atTime(startTime);
                    LocalDateTime endDateTime = selectedDate.atTime(endTime);
                    
                    activity.setScheduledDate(startDateTime);
                    activity.setStartTime(startDateTime);
                    activity.setEndTime(endDateTime);
                    
                    return activity;
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Error", "Please fill all required fields correctly");
                    return null;
                }
            }
            return null;
        });
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(20);
        
        // Header
        Label titleLabel = new Label("Calendar & Schedule");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Calendar controls
        HBox controlsBox = new HBox(15);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.getChildren().addAll(
            new Label("View:"),
            viewTypeCombo,
            new Label("Filter:"),
            filterCombo,
            previousButton,
            currentPeriodLabel,
            nextButton,
            todayButton,
            addActivityButton
        );
        
        // Calendar container
        calendarContainer.getChildren().add(monthViewGrid);
        
        getChildren().addAll(titleLabel, controlsBox, calendarContainer);
    }
    
    private void setupEventHandlers() {
        viewTypeCombo.setOnAction(e -> {
            currentViewType = viewTypeCombo.getValue();
            updateCalendarView();
        });
        
        filterCombo.setOnAction(e -> loadCalendarData());
        
        previousButton.setOnAction(e -> {
            navigatePrevious();
            updateCalendarView();
        });
        
        nextButton.setOnAction(e -> {
            navigateNext();
            updateCalendarView();
        });
        
        todayButton.setOnAction(e -> {
            displayDate = LocalDate.now();
            updateCalendarView();
        });
        
        addActivityButton.setOnAction(e -> handleAddActivity());
    }
    
    private void navigatePrevious() {
        switch (currentViewType) {
            case "Day":
                displayDate = displayDate.minusDays(1);
                break;
            case "Week":
                displayDate = displayDate.minusWeeks(1);
                break;
            case "Month":
                displayDate = displayDate.minusMonths(1);
                break;
        }
    }
    
    private void navigateNext() {
        switch (currentViewType) {
            case "Day":
                displayDate = displayDate.plusDays(1);
                break;
            case "Week":
                displayDate = displayDate.plusWeeks(1);
                break;
            case "Month":
                displayDate = displayDate.plusMonths(1);
                break;
        }
    }
    
    private void updateCalendarView() {
        calendarContainer.getChildren().clear();
        
        switch (currentViewType) {
            case "Day":
                updateDayView();
                calendarContainer.getChildren().add(dayViewList);
                break;
            case "Week":
                updateWeekView();
                calendarContainer.getChildren().add(weekViewGrid);
                break;
            case "Month":
                updateMonthView();
                calendarContainer.getChildren().add(monthViewGrid);
                break;
        }
        
        updatePeriodLabel();
        loadCalendarData();
    }
    
    private void updateDayView() {
        dayViewData.clear();
        LocalDate targetDate = displayDate;
        
        List<Activity> dayActivities = schedulingService.findScheduledActivitiesByDate(targetDate.atStartOfDay());
        
        for (Activity activity : dayActivities) {
            dayViewData.add(new ActivityItem(activity));
        }
        
        dayViewList.setItems(dayViewData);
    }
    
    private void updateWeekView() {
        weekViewGrid.getChildren().clear();
        
        // Header row with day names
        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47; -fx-padding: 10;");
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefWidth(150);
            dayLabel.setPrefHeight(40);
            weekViewGrid.add(dayLabel, i, 0);
        }
        
        // Get start of week
        LocalDate weekStart = displayDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        
        // Create day cells
        for (int day = 0; day < 7; day++) {
            LocalDate currentDay = weekStart.plusDays(day);
            VBox dayCell = createDayCell(currentDay);
            weekViewGrid.add(dayCell, day, 1);
        }
    }
    
    private void updateMonthView() {
        monthViewGrid.getChildren().clear();
        
        // Header row with day names
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayNames[i]);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47; -fx-padding: 5;");
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefWidth(120);
            dayLabel.setPrefHeight(30);
            monthViewGrid.add(dayLabel, i, 0);
        }
        
        // Get first day of month and start of calendar grid
        LocalDate firstDayOfMonth = displayDate.withDayOfMonth(1);
        LocalDate startOfCalendar = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        
        int row = 1;
        LocalDate currentDay = startOfCalendar;
        
        // Fill calendar grid
        while (currentDay.getMonth() == displayDate.getMonth() || 
               currentDay.isBefore(firstDayOfMonth)) {
            
            for (int col = 0; col < 7; col++) {
                VBox dayCell = createMonthDayCell(currentDay);
                monthViewGrid.add(dayCell, col, row);
                currentDay = currentDay.plusDays(1);
            }
            row++;
        }
    }
    
    private VBox createDayCell(LocalDate date) {
        VBox cell = new VBox(5);
        cell.setPrefWidth(150);
        cell.setPrefHeight(200);
        cell.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-padding: 5;");
        cell.setAlignment(Pos.TOP_CENTER);
        
        // Date label
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Highlight today
        if (date.equals(LocalDate.now())) {
            cell.setStyle("-fx-border-color: #2D5BFF; -fx-border-width: 2; -fx-background-color: #EAE6FF; -fx-padding: 5;");
        }
        
        // Activities for this day
        List<Activity> dayActivities = schedulingService.findScheduledActivitiesByDate(date.atStartOfDay());
        VBox activitiesBox = new VBox(2);
        
        for (Activity activity : dayActivities) {
            Label activityLabel = new Label(activity.getName());
            activityLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #2D5BFF; -fx-background-color: #F0F8FF; -fx-padding: 2 4; -fx-background-radius: 3;");
            activityLabel.setWrapText(true);
            activitiesBox.getChildren().add(activityLabel);
        }
        
        cell.getChildren().addAll(dateLabel, activitiesBox);
        return cell;
    }
    
    private VBox createMonthDayCell(LocalDate date) {
        VBox cell = new VBox(2);
        cell.setPrefWidth(120);
        cell.setPrefHeight(80);
        cell.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-padding: 2;");
        cell.setAlignment(Pos.TOP_LEFT);
        
        // Date label
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Highlight today
        if (date.equals(LocalDate.now())) {
            cell.setStyle("-fx-border-color: #2D5BFF; -fx-border-width: 2; -fx-background-color: #EAE6FF; -fx-padding: 2;");
        }
        
        // Highlight current month
        if (date.getMonth() != displayDate.getMonth()) {
            dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #CCCCCC;");
        }
        
        // Activities for this day
        List<Activity> dayActivities = schedulingService.findScheduledActivitiesByDate(date.atStartOfDay());
        VBox activitiesBox = new VBox(1);
        
        for (int i = 0; i < Math.min(dayActivities.size(), 3); i++) {
            Activity activity = dayActivities.get(i);
            Rectangle activityIndicator = new Rectangle(8, 8);
            activityIndicator.setFill(Color.web("#2D5BFF"));
            activityIndicator.setArcWidth(4);
            activityIndicator.setArcHeight(4);
            activitiesBox.getChildren().add(activityIndicator);
        }
        
        if (dayActivities.size() > 3) {
            Label moreLabel = new Label("+" + (dayActivities.size() - 3));
            moreLabel.setStyle("-fx-font-size: 8px; -fx-text-fill: #666666;");
            activitiesBox.getChildren().add(moreLabel);
        }
        
        cell.getChildren().addAll(dateLabel, activitiesBox);
        return cell;
    }
    
    private void updatePeriodLabel() {
        switch (currentViewType) {
            case "Day":
                currentPeriodLabel.setText(displayDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
                break;
            case "Week":
                LocalDate weekStart = displayDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                LocalDate weekEnd = weekStart.plusDays(6);
                currentPeriodLabel.setText(weekStart.format(DateTimeFormatter.ofPattern("MMM dd")) + " - " + 
                                         weekEnd.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
                break;
            case "Month":
                currentPeriodLabel.setText(displayDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
                break;
        }
    }
    
    private void loadCalendarData() {
        try {
            String filter = filterCombo.getValue();
            LocalDate startDate = null;
            LocalDate endDate = null;
            
            switch (filter) {
                case "Today":
                    startDate = LocalDate.now();
                    endDate = LocalDate.now();
                    break;
                case "This Week":
                    startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                    endDate = startDate.plusDays(6);
                    break;
                case "This Month":
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
                    break;
                default:
                    // All activities or My activities - no date filter
                    break;
            }
            
            if (startDate != null && endDate != null) {
                currentActivities = schedulingService.findScheduledActivitiesByDateRange(
                    startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
            } else {
                currentActivities = schedulingService.findUpcomingActivities();
            }
            
            // Filter by user if "My Activities" is selected
            if ("My Activities".equals(filter) && currentUser != null) {
                currentActivities = currentActivities.stream()
                    .filter(activity -> activity.getVolunteerId() == currentUser.getId())
                    .collect(java.util.stream.Collectors.toList());
            }
            
        } catch (Exception e) {
            System.err.println("Error loading calendar data: " + e.getMessage());
            currentActivities = new ArrayList<>();
        }
    }
    
    private void handleAddActivity() {
        if (currentUser == null) {
            showAlert(AlertType.ERROR, "Error", "User not logged in");
            return;
        }
        
        Optional<Activity> result = addActivityDialog.showAndWait();
        if (result.isPresent()) {
            Activity activity = result.get();
            try {
                Activity scheduledActivity = schedulingService.scheduleActivity(activity);
                if (scheduledActivity != null) {
                    showAlert(AlertType.INFORMATION, "Success", "Activity scheduled successfully!");
                    loadCalendarData();
                    updateCalendarView();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to schedule activity");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to schedule activity: " + e.getMessage());
            }
        }
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner classes
    public static class ActivityItem {
        private Activity activity;
        
        public ActivityItem(Activity activity) {
            this.activity = activity;
        }
        
        public Activity getActivity() { return activity; }
        
        @Override
        public String toString() {
            return activity.getName() + " (" + 
                   activity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                   activity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
        }
    }
    
    private class ActivityListCell extends ListCell<ActivityItem> {
        @Override
        protected void updateItem(ActivityItem item, boolean empty) {
            super.updateItem(item, empty);
            
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                Activity activity = item.getActivity();
                
                VBox cellContent = new VBox(5);
                cellContent.setPadding(new Insets(10));
                cellContent.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-background-color: #FFFFFF; -fx-background-radius: 5;");
                
                Label nameLabel = new Label(activity.getName());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A47;");
                
                Label timeLabel = new Label(activity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                                          activity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                timeLabel.setStyle("-fx-text-fill: #666666;");
                
                Label categoryLabel = new Label(activity.getCategory());
                categoryLabel.setStyle("-fx-text-fill: #2D5BFF; -fx-font-size: 12px;");
                
                cellContent.getChildren().addAll(nameLabel, timeLabel, categoryLabel);
                setGraphic(cellContent);
                setText(null);
            }
        }
    }
}
