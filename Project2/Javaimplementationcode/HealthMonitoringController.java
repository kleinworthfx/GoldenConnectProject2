public class HealthMonitoringController {
    @FXML private TextField systolicField;
    @FXML private TextField diastolicField;
    @FXML private TextField temperatureField;
    @FXML private TextArea notesArea;
    @FXML private ToggleGroup moodGroup;

    @Autowired
    private HealthService healthService;

    public void initialize() {
        setupInputValidation();
        setupMoodSelection();
        loadLatestHealth();
    }

    private void setupInputValidation() {
        systolicField.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.matches("\\d*")) {
                systolicField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    @FXML
    private void handleSaveHealth() {
        HealthMetrics metrics = new HealthMetrics(
            Integer.parseInt(systolicField.getText()),
            Integer.parseInt(diastolicField.getText()),
            Double.parseDouble(temperatureField.getText()),
            getSelectedMood(),
            notesArea.getText()
        );

        healthService.saveHealthMetrics(metrics);
        showConfirmationAlert("Health data saved successfully!");
    }
}