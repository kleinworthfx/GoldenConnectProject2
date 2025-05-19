public class ActivityBookingController {
    @FXML private DatePicker datePicker;
    @FXML private ListView<Activity> activityListView;
    @FXML private Button bookButton;
    
    @Autowired
    private ActivityService activityService;
    @Autowired
    private BookingService bookingService;

    public void initialize() {
        setupDatePicker();
        setupActivityList();
        setupBookingHandler();
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> 
            loadActivities(newVal));
    }

    private void loadActivities(LocalDate date) {
        List<Activity> activities = activityService
            .getActivitiesByDate(date);
        activityListView.setItems(
            FXCollections.observableArrayList(activities));
    }
}