@Controller
public class ProfileController {
    @FXML private ImageView profilePhoto;
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private ListView<EmergencyContact> contactsList;
    @FXML private ComboBox<String> fontSizeCombo;
    @FXML private ComboBox<String> themeCombo;

    @Autowired
    private ProfileService profileService;
    @Autowired
    private PreferencesService preferencesService;

    public void initialize() {
        loadUserProfile();
        setupPreferences();
        setupEmergencyContacts();
    }

    private void loadUserProfile() {
        UserProfile profile = profileService.getCurrentUserProfile();
        nameField.setText(profile.getName());
        ageField.setText(String.valueOf(profile.getAge()));
        loadProfilePhoto(profile.getPhotoUrl());
    }

    @FXML
    private void handlePhotoUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            profileService.updateProfilePhoto(selectedFile);
            loadProfilePhoto(selectedFile.toURI().toString());
        }
    }
}