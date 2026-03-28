package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import za.ac.cput.goldenconnect.model.User;
import java.util.function.Consumer;

public class NavigationPanel extends VBox {
    
    private User currentUser;
    private Button dashboardButton;
    private Button requestsButton;
    private Button scheduleButton;
    private Button activitiesButton;
    private Button elderlyProfilesButton;
    private Button volunteersButton;
    private Button tutorialsButton;
    private Button feedbackButton;
    private Button reportsButton;
    private Button logoutButton;
    
    // Callback functions
    private Runnable onDashboardRequest;
    private Runnable onRequestFormRequest;
    private Runnable onMyRequestsRequest;
    private Runnable onScheduleRequest;
    private Runnable onActivitiesRequest;
    private Runnable onElderlyProfilesRequest;
    private Runnable onVolunteersRequest;
    private Runnable onFeedbackRequest;
    private Runnable onTutorialsRequest;
    private Runnable onReportsRequest;
    private Runnable onLogoutRequest;
    
    public NavigationPanel() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void setOnDashboardRequest(Runnable callback) {
        this.onDashboardRequest = callback;
    }
    
    public void setOnRequestFormRequest(Runnable callback) {
        this.onRequestFormRequest = callback;
    }
    
    public void setOnMyRequestsRequest(Runnable callback) {
        this.onMyRequestsRequest = callback;
    }
    
    public void setOnScheduleRequest(Runnable callback) {
        this.onScheduleRequest = callback;
    }
    
    public void setOnActivitiesRequest(Runnable callback) {
        this.onActivitiesRequest = callback;
    }
    
    public void setOnElderlyProfilesRequest(Runnable callback) {
        this.onElderlyProfilesRequest = callback;
    }
    
    public void setOnVolunteersRequest(Runnable callback) {
        this.onVolunteersRequest = callback;
    }
    
    public void setOnFeedbackRequest(Runnable callback) {
        this.onFeedbackRequest = callback;
    }
    
    public void setOnTutorialsRequest(Runnable callback) {
        this.onTutorialsRequest = callback;
    }
    
    public void setOnReportsRequest(Runnable callback) {
        this.onReportsRequest = callback;
    }
    
    public void setOnLogoutRequest(Runnable callback) {
        this.onLogoutRequest = callback;
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        // User info is no longer displayed in navigation
    }
    
    private void initializeComponents() {
        // Dashboard Button with Icon
        dashboardButton = createButtonWithIcon("Dashboard", "/images/dashboard.png");
        
        // Session Booking Button with Icon
        requestsButton = createButtonWithIcon("Session Booking", "/images/requests.png");
        
        // Schedule Button with Icon
        scheduleButton = createButtonWithIcon("Schedule", "/images/schedule.png");
        
        // Activities Button with Icon
        activitiesButton = createButtonWithIcon("Activities", "/images/activities.png");
        
        // Elderly Profiles Button with Icon
        elderlyProfilesButton = createButtonWithIcon("Elderly Profiles", "/images/elderly.png");
        
        // Volunteers Button with Icon
        volunteersButton = createButtonWithIcon("Volunteers", "/images/volunteers.png");
        
        // Video Tutorials Button with Icon
        tutorialsButton = createButtonWithIcon("Video Tutorials", "/images/tutorials.png");
        
        // Feedback Button with Icon
        feedbackButton = createButtonWithIcon("Feedback", "/images/feedback.png");
        
        // Reports Button with Icon
        reportsButton = createButtonWithIcon("Reports", "/images/reports.png");
        
        // Logout Button (no icon, red background)
        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #FF5A4A; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 20; -fx-background-radius: 8;");
        logoutButton.setPrefWidth(250);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(0));
        setSpacing(0);
        setStyle("-fx-background-color: #FFF8EE; -fx-border-color: #FFF8EE; -fx-border-width: 0 1 0 0;");
        
        // Create fixed header with logo
        VBox headerSection = createHeaderSection();
        
        // Create scrollable middle section with navigation buttons
        VBox scrollableSection = createScrollableSection();
        
        // Create fixed footer with logout button
        VBox footerSection = createFooterSection();
        
        // Main layout with fixed header, scrollable middle, and fixed footer
        VBox mainLayout = new VBox(0);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getChildren().addAll(headerSection, scrollableSection, footerSection);
        
        // Set VBox constraints to make middle section expand and others fixed
        VBox.setVgrow(scrollableSection, javafx.scene.layout.Priority.ALWAYS);
        
        getChildren().add(mainLayout);
    }
    
    private VBox createHeaderSection() {
        VBox headerSection = new VBox(0);
        headerSection.setAlignment(Pos.TOP_CENTER);
        headerSection.setPadding(new Insets(15, 10, 10, 10));
        headerSection.setStyle("-fx-background-color: #FFF8EE; -fx-border-color: #DEE2E6; -fx-border-width: 0 0 1 0;");
        
        // Create logo section
        HBox logoSection = createLogoSection();
        headerSection.getChildren().add(logoSection);
        
        return headerSection;
    }
    
    private VBox createScrollableSection() {
        VBox scrollableSection = new VBox(0);
        scrollableSection.setAlignment(Pos.TOP_CENTER);
        scrollableSection.setPadding(new Insets(10));
        
        // Create content VBox for scrollable navigation items
        VBox contentBox = new VBox(8);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(5));
        contentBox.getChildren().addAll(
            dashboardButton,
            new Separator(),
            requestsButton,
            scheduleButton,
            activitiesButton,
            elderlyProfilesButton,
            volunteersButton,
            tutorialsButton,
            feedbackButton,
            reportsButton
        );
        
        // Create ScrollPane for the middle section
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setPadding(Insets.EMPTY);
        
        scrollableSection.getChildren().add(scrollPane);
        return scrollableSection;
    }
    
    private VBox createFooterSection() {
        VBox footerSection = new VBox(0);
        footerSection.setAlignment(Pos.BOTTOM_CENTER);
        footerSection.setPadding(new Insets(10, 10, 15, 10));
        footerSection.setStyle("-fx-background-color: #FFF8EE; -fx-border-color: #DEE2E6; -fx-border-width: 1 0 0 0;");
        
        footerSection.getChildren().add(logoutButton);
        return footerSection;
    }
    
    private HBox createLogoSection() {
        HBox logoSection = new HBox(10);
        logoSection.setAlignment(Pos.CENTER_LEFT);
        logoSection.setPadding(new Insets(20, 0, 20, 0));
        
        try {
            // Load the logo image
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(40);
            logoView.setFitHeight(40);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
            
            // Create app name label
            Label appNameLabel = new Label("Golden Connect");
            appNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            logoSection.getChildren().addAll(logoView, appNameLabel);
            
        } catch (Exception e) {
            // Fallback to text-only if logo loading fails
            System.err.println("Could not load logo: " + e.getMessage());
            Label appNameLabel = new Label("Golden Connect");
            appNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            logoSection.getChildren().add(appNameLabel);
        }
        
        return logoSection;
    }
    
    private Button createButtonWithIcon(String text, String iconPath) {
        Button button = new Button();
        button.setPrefWidth(250);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #0E2A47; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 20; -fx-alignment: CENTER_LEFT;");
        
        try {
            // Load the icon image
            Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(28);
            iconView.setFitHeight(28);
            iconView.setPreserveRatio(true);
            iconView.setSmooth(true);
            
            // Create HBox to hold icon and text
            HBox content = new HBox(10);
            content.setAlignment(Pos.CENTER_LEFT);
            Label textLabel = new Label(text);
            textLabel.setStyle("-fx-text-fill: #0E2A47;");
            content.getChildren().addAll(iconView, textLabel);
            
            button.setGraphic(content);
            
        } catch (Exception e) {
            // Fallback to text-only if icon loading fails
            System.err.println("Could not load icon: " + iconPath + " - " + e.getMessage());
            button.setText(text);
        }
        
        return button;
    }
    
    private void setupEventHandlers() {
        dashboardButton.setOnAction(e -> navigateToDashboard());
        requestsButton.setOnAction(e -> navigateToRequests());
        scheduleButton.setOnAction(e -> navigateToSchedule());
        activitiesButton.setOnAction(e -> navigateToActivities());
        elderlyProfilesButton.setOnAction(e -> navigateToElderlyProfiles());
        volunteersButton.setOnAction(e -> navigateToVolunteers());
        tutorialsButton.setOnAction(e -> navigateToTutorials());
        feedbackButton.setOnAction(e -> navigateToFeedback());
        reportsButton.setOnAction(e -> navigateToReports());
        logoutButton.setOnAction(e -> logout());
    }
    
    private void navigateToDashboard() {
        if (onDashboardRequest != null) {
            onDashboardRequest.run();
        }
    }
    
    private void navigateToRequests() {
        if (onRequestFormRequest != null) {
            onRequestFormRequest.run();
        }
    }
    
    private void navigateToSchedule() {
        if (onScheduleRequest != null) {
            onScheduleRequest.run();
        }
    }
    
    private void navigateToActivities() {
        if (onActivitiesRequest != null) {
            onActivitiesRequest.run();
        }
    }
    
    private void navigateToElderlyProfiles() {
        if (onElderlyProfilesRequest != null) {
            onElderlyProfilesRequest.run();
        }
    }
    
    private void navigateToVolunteers() {
        if (onVolunteersRequest != null) {
            onVolunteersRequest.run();
        }
    }
    
    private void navigateToTutorials() {
        if (onTutorialsRequest != null) {
            onTutorialsRequest.run();
        }
    }
    
    private void navigateToFeedback() {
        if (onFeedbackRequest != null) {
            onFeedbackRequest.run();
        }
    }
    
    private void navigateToReports() {
        if (onReportsRequest != null) {
            onReportsRequest.run();
        }
    }
    
    private void logout() {
        if (onLogoutRequest != null) {
            onLogoutRequest.run();
        }
    }
}
