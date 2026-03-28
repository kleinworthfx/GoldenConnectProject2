package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Feedback;
import za.ac.cput.goldenconnect.service.RequestService;
import za.ac.cput.goldenconnect.service.ActivityService;
import za.ac.cput.goldenconnect.dao.UserDAO;
import za.ac.cput.goldenconnect.dao.ActivityDAO;
import za.ac.cput.goldenconnect.dao.FeedbackDAO;
import za.ac.cput.goldenconnect.dao.impl.UserDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.ActivityDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.FeedbackDAOImpl;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends VBox {
    
    private User currentUser;
    private RequestService requestService;
    private ActivityService activityService;
    
    // DAO instances for database connectivity
    private UserDAO userDAO;
    private ActivityDAO activityDAO;
    private FeedbackDAO feedbackDAO;
    
    // Navigation callbacks
    private Runnable onScheduleClick;
    private Runnable onBookSessionsClick;
    private Runnable onReportsClick;
    
    public DashboardPanel(User currentUser, RequestService requestService, ActivityService activityService) {
        this.currentUser = currentUser;
        this.requestService = requestService;
        this.activityService = activityService;
        
        // Initialize DAO instances
        this.userDAO = new UserDAOImpl();
        this.activityDAO = new ActivityDAOImpl();
        this.feedbackDAO = new FeedbackDAOImpl();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        // Update UI elements that depend on user data
        updateUserInfo();
    }
    
    // Setter methods for navigation callbacks
    public void setOnScheduleClick(Runnable callback) {
        this.onScheduleClick = callback;
    }
    
    public void setOnBookSessionsClick(Runnable callback) {
        this.onBookSessionsClick = callback;
    }
    
    public void setOnReportsClick(Runnable callback) {
        this.onReportsClick = callback;
    }
    
    private void updateUserInfo() {
        // Update greeting message when user changes
        if (currentUser != null) {
            // Refresh the entire layout to update the greeting
            getChildren().clear();
            setupLayout();
        }
    }
    
    private void initializeComponents() {
        // Components will be initialized in setupLayout
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(40));
        setSpacing(30);
        
        // Dynamic greeting based on time of day
        Label greetingLabel = createDynamicGreeting();
        
        Label descriptionLabel = new Label("Bridging Generations Through Technology");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        // Overview Section
        VBox overviewSection = createOverviewSection();
        
        // Quick Action Buttons
        HBox actionButtons = new HBox(20);
        actionButtons.setAlignment(Pos.CENTER);
        
        Button scheduleButton = createButtonWithIcon("Schedule", "schedule.png", "#2D5BFF");
        scheduleButton.setPrefWidth(220);
        scheduleButton.setPrefHeight(220);
        
        Button bookSessionsButton = createButtonWithIcon("Book Sessions", "requests.png", "#2BAE66");
        bookSessionsButton.setPrefWidth(220);
        bookSessionsButton.setPrefHeight(220);
        
        Button reportsButton = createButtonWithIcon("Reports", "reports.png", "#F5B700");
        reportsButton.setPrefWidth(220);
        reportsButton.setPrefHeight(220);
        
        actionButtons.getChildren().addAll(scheduleButton, bookSessionsButton, reportsButton);
        
        // Setup button event handlers
        setupButtonEventHandlers(scheduleButton, bookSessionsButton, reportsButton);
        
        getChildren().addAll(greetingLabel, descriptionLabel, overviewSection, actionButtons);
    }
    
    private Label createDynamicGreeting() {
        String greeting = getGreetingBasedOnTime();
        String userName = currentUser != null ? currentUser.getName() : "User";
        
        Label greetingLabel = new Label(greeting + ", " + userName);
        greetingLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        return greetingLabel;
    }
    
    private String getGreetingBasedOnTime() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        
        if (hour >= 5 && hour < 12) {
            return "Good morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good afternoon";
        } else {
            return "Good evening";
        }
    }
    
    private VBox createOverviewSection() {
        VBox overviewSection = new VBox(20);
        overviewSection.setAlignment(Pos.CENTER);
        overviewSection.setPadding(new Insets(30));
        overviewSection.setStyle("-fx-background-color: #0E2A47; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);");
        
        // Overview Title
        Label overviewTitle = new Label("Overview");
        overviewTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        // Overview Cards Container
        HBox overviewCards = new HBox(20);
        overviewCards.setAlignment(Pos.CENTER);
        
        // Create the four metric cards
        StackPane totalUsersCard = createMetricCard("Total Users", getTotalUsersCount());
        StackPane upcomingSessionsCard = createMetricCard("Upcoming Sessions", getUpcomingSessionsCount());
        StackPane elderlyCountCard = createMetricCard("Number of Elderly", getElderlyCount());
        StackPane pendingFeedbackCard = createMetricCard("Pending Feedback", getPendingFeedbackCount());
        
        overviewCards.getChildren().addAll(totalUsersCard, upcomingSessionsCard, elderlyCountCard, pendingFeedbackCard);
        overviewSection.getChildren().addAll(overviewTitle, overviewCards);
        
        return overviewSection;
    }
    
    private StackPane createMetricCard(String label, int value) {
        StackPane card = new StackPane();
        card.setPrefWidth(200);
        card.setPrefHeight(120);
        card.setStyle("-fx-background-color: #E0F2F7; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 2);");
        
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        
        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1F2A37;");
        
        Label descriptionLabel = new Label(label);
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        content.getChildren().addAll(valueLabel, descriptionLabel);
        card.getChildren().add(content);
        
        return card;
    }
    
    // Metric calculation methods connected to database
    private int getTotalUsersCount() {
        try {
            List<User> allUsers = userDAO.findAll();
            return allUsers != null ? allUsers.size() : 0;
        } catch (Exception e) {
            System.err.println("Error getting total users count: " + e.getMessage());
            return 0;
        }
    }
    
    private int getUpcomingSessionsCount() {
        try {
            // Get activities that are scheduled for future dates
            List<Activity> allActivities = activityDAO.findAll();
            if (allActivities != null) {
                return (int) allActivities.stream()
                    .filter(activity -> activity.getScheduledDate() != null)
                    .count();
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error getting upcoming sessions count: " + e.getMessage());
            return 0;
        }
    }
    
    private int getElderlyCount() {
        try {
            List<User> allUsers = userDAO.findAll();
            if (allUsers != null) {
                return (int) allUsers.stream()
                    .filter(user -> {
                        if (user.getDateOfBirth() != null) {
                            int age = java.time.LocalDateTime.now().getYear() - user.getDateOfBirth().getYear();
                            return age >= 65;
                        }
                        return false;
                    })
                    .count();
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error getting elderly count: " + e.getMessage());
            return 0;
        }
    }
    
    private int getPendingFeedbackCount() {
        try {
            List<Feedback> allFeedback = feedbackDAO.findAll();
            if (allFeedback != null) {
                return (int) allFeedback.stream()
                    .filter(feedback -> "PENDING".equals(feedback.getStatus()))
                    .count();
            }
            return 0;
        } catch (Exception e) {
            System.err.println("Error getting pending feedback count: " + e.getMessage());
            return 0;
        }
    }
    
    private Button createButtonWithIcon(String text, String iconPath, String backgroundColor) {
        Button button = new Button();
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 25 40; -fx-background-radius: 8;");
        
        // Create VBox to hold icon and text
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        
        // Load and create icon
        try {
            Image iconImage = new Image(getClass().getResourceAsStream("/images/" + iconPath));
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(48);
            iconView.setFitHeight(48);
            iconView.setPreserveRatio(true);
            
            // Create text label
            Label textLabel = new Label(text);
            textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            
            content.getChildren().addAll(iconView, textLabel);
        } catch (Exception e) {
            // Fallback to text-only if icon fails to load
            System.err.println("Could not load icon: " + iconPath + " - " + e.getMessage());
            Label textLabel = new Label(text);
            textLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
            content.getChildren().add(textLabel);
        }
        
        button.setGraphic(content);
        return button;
    }
    
    private void setupEventHandlers() {
        // Event handlers will be implemented later
    }
    
    private void setupButtonEventHandlers(Button scheduleButton, Button bookSessionsButton, Button reportsButton) {
        // Schedule button click handler
        scheduleButton.setOnAction(e -> {
            if (onScheduleClick != null) {
                onScheduleClick.run();
            }
        });
        
        // Book Sessions button click handler
        bookSessionsButton.setOnAction(e -> {
            if (onBookSessionsClick != null) {
                onBookSessionsClick.run();
            }
        });
        
        // Reports button click handler
        reportsButton.setOnAction(e -> {
            if (onReportsClick != null) {
                onReportsClick.run();
            }
        });
    }
}
