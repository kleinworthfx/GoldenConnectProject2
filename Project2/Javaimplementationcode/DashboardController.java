public class DashboardController {
    @FXML private VBox dashboardContainer;
    @FXML private Label welcomeLabel;
    @FXML private ListView<Activity> activitiesList;
    @FXML private GridPane healthMetricsGrid;
    @FXML private VBox notificationsBox;

    @Autowired
    private ActivityService activityService;
    @Autowired
    private HealthService healthService;

    public void initialize() {
        setupDashboard();
        loadTodayActivities();
        loadHealthMetrics();
        setupNotifications();
    }

    private void setupDashboard() {
        dashboardContainer.setStyle("""
            -fx-background-color: #F9F9F9;
            -fx-padding: 20;
            -fx-spacing: 15;
        """);

        welcomeLabel.setText("Welcome, " + UserSession.getCurrentUser().getName());
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    }

    private void loadTodayActivities() {
        List<Activity> activities = activityService.getTodayActivities();
        activitiesList.setItems(FXCollections.observableArrayList(activities));
        activitiesList.setCellFactory(param -> new ActivityListCell());
    }
}