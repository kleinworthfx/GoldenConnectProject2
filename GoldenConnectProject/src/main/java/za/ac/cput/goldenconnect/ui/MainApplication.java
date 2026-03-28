package za.ac.cput.goldenconnect.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.geometry.Pos;

import za.ac.cput.goldenconnect.service.*;
import za.ac.cput.goldenconnect.service.impl.*;
import za.ac.cput.goldenconnect.model.User;

public class MainApplication extends Application {
    
    // Service layer
    private AuthService authService;
    private RequestService requestService;
    private ActivityService activityService;
    private FeedbackService feedbackService;
    private VideoTutorialService videoTutorialService;
    private ReportService reportService;
    private MatchingService matchingService;
    private SchedulingService schedulingService;
    private NotificationService notificationService;
    
    // UI Components
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private DashboardPanel dashboardPanel;
    private SessionBookingPanel sessionBookingPanel;
    private MyRequestsPanel myRequestsPanel;
    private SchedulePanel schedulePanel;
    private ActivitiesPanel activitiesPanel;
    private ElderlyProfilesPanel elderlyProfilesPanel;
    private VolunteersPanel volunteersPanel;
    private FeedbackPanel feedbackPanel;
    private TutorialsPanel tutorialsPanel;
    private ReportsPanel reportsPanel;
    private NavigationPanel navigationPanel;
    
    // Application state
    private User currentUser;
    private Stage primaryStage;
    private BorderPane mainLayout;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeServices();
        initializeUI();
        
        // Set application icon
        try {
            Image iconImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            primaryStage.getIcons().add(iconImage);
        } catch (Exception e) {
            System.err.println("Could not load application icon: " + e.getMessage());
        }
        
        primaryStage.setTitle("Golden Connect - Bridging Generations Through Technology");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        
        // Show login screen initially
        showLoginScreen();
        
        primaryStage.show();
    }
    
    private void initializeServices() {
        authService = new AuthServiceImpl();
        requestService = new RequestServiceImpl();
        activityService = new ActivityServiceImpl();
        feedbackService = new FeedbackServiceImpl();
        videoTutorialService = new VideoTutorialServiceImpl();
        reportService = new ReportServiceImpl();
        matchingService = new MatchingServiceImpl();
        schedulingService = new SchedulingServiceImpl();
        notificationService = new NotificationServiceImpl();
    }
    
    private void initializeUI() {
        // Initialize all UI panels with their respective services
        loginPanel = new LoginPanel(authService);
        registrationPanel = new RegistrationPanel(authService);
        dashboardPanel = new DashboardPanel(currentUser, requestService, activityService);
        sessionBookingPanel = new SessionBookingPanel(activityService, requestService, currentUser);
        myRequestsPanel = new MyRequestsPanel(requestService, currentUser);
        schedulePanel = new SchedulePanel(schedulingService, currentUser);
        activitiesPanel = new ActivitiesPanel(activityService, currentUser);
        elderlyProfilesPanel = new ElderlyProfilesPanel(currentUser);
        volunteersPanel = new VolunteersPanel(currentUser);
        feedbackPanel = new FeedbackPanel(feedbackService, currentUser);
        tutorialsPanel = new TutorialsPanel(videoTutorialService, currentUser);
        reportsPanel = new ReportsPanel(reportService, currentUser);
        navigationPanel = new NavigationPanel();
        
        // Set up navigation callbacks
        setupNavigationCallbacks();
    }
    
    private void setupNavigationCallbacks() {
        // Login panel callbacks
        loginPanel.setOnLoginSuccess(this::onLoginSuccess);
        loginPanel.setOnRegisterRequest(this::showRegistrationScreen);
        
        // Registration panel callbacks
        registrationPanel.setOnRegistrationSuccess(this::onRegistrationSuccess);
        registrationPanel.setOnBackToLogin(this::showLoginScreen);
        
        // Navigation panel callbacks
        navigationPanel.setOnDashboardRequest(this::showDashboard);
        navigationPanel.setOnRequestFormRequest(this::showSessionBooking);
        navigationPanel.setOnMyRequestsRequest(this::showMyRequests);
        navigationPanel.setOnScheduleRequest(this::showSchedule);
        navigationPanel.setOnActivitiesRequest(this::showActivities);
        navigationPanel.setOnElderlyProfilesRequest(this::showElderlyProfiles);
        navigationPanel.setOnVolunteersRequest(this::showVolunteers);
        navigationPanel.setOnFeedbackRequest(this::showFeedback);
        navigationPanel.setOnTutorialsRequest(this::showTutorials);
        navigationPanel.setOnReportsRequest(this::showReports);
        navigationPanel.setOnLogoutRequest(this::logout);
        
        // Dashboard button callbacks
        dashboardPanel.setOnScheduleClick(this::showSchedule);
        dashboardPanel.setOnBookSessionsClick(this::showSessionBooking);
        dashboardPanel.setOnReportsClick(this::showReports);
    }
    
    private void showLoginScreen() {
        // Create a new login panel instance to avoid scene conflicts
        LoginPanel newLoginPanel = new LoginPanel(authService);
        newLoginPanel.setOnLoginSuccess(this::onLoginSuccess);
        newLoginPanel.setOnRegisterRequest(this::showRegistrationScreen);
        newLoginPanel.clearFields();
        
        // The login panel now contains its own complete layout
        Scene scene = new Scene(newLoginPanel);
        primaryStage.setScene(scene);
    }
    
    private void showRegistrationScreen() {
        registrationPanel.clearFields();
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFF8EE;");
        
        // Header
        Label headerLabel = new Label("Create Account");
        headerLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        headerLabel.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        root.setTop(headerLabel);
        
        root.setCenter(registrationPanel);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
    }
    
    private void onLoginSuccess(User user) {
        this.currentUser = user;
        showDashboard();
    }
    
    private void onRegistrationSuccess(User user) {
        this.currentUser = user;
        showAlert(AlertType.INFORMATION, "Success", "Account created successfully! Welcome to GoldenConnect.");
        showDashboard();
    }
    
    private void showDashboard() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        // Update dashboard with current user
        dashboardPanel.updateUser(currentUser);
        
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ffffff;");
        
        // Set up navigation panel
        navigationPanel.updateUser(currentUser);
        mainLayout.setLeft(navigationPanel);
        
        // Set up dashboard content
        mainLayout.setCenter(dashboardPanel);
        
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
    }
    
    private void showSessionBooking() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        sessionBookingPanel.updateUser(currentUser);
        mainLayout.setCenter(sessionBookingPanel);
    }
    
    private void showMyRequests() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        myRequestsPanel.updateUser(currentUser);
        mainLayout.setCenter(myRequestsPanel);
    }
    
    private void showSchedule() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        schedulePanel.updateUser(currentUser);
        mainLayout.setCenter(schedulePanel);
    }
    
    private void showActivities() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        activitiesPanel.updateUser(currentUser);
        mainLayout.setCenter(activitiesPanel);
    }
    
    private void showElderlyProfiles() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        mainLayout.setCenter(elderlyProfilesPanel);
    }
    
    private void showVolunteers() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        mainLayout.setCenter(volunteersPanel);
    }
    
    private void showFeedback() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        feedbackPanel.updateUser(currentUser);
        mainLayout.setCenter(feedbackPanel);
    }
    
    private void showTutorials() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        tutorialsPanel.updateUser(currentUser);
        mainLayout.setCenter(tutorialsPanel);
    }
    
    private void showReports() {
        if (currentUser == null) {
            showLoginScreen();
            return;
        }
        
        reportsPanel.updateUser(currentUser);
        mainLayout.setCenter(reportsPanel);
    }
    
    private void logout() {
        currentUser = null;
        showAlert(AlertType.INFORMATION, "Logged Out", "You have been successfully logged out.");
        showLoginScreen();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
