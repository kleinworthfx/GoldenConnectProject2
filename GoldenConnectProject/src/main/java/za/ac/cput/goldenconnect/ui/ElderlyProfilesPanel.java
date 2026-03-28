package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.ElderlyProfile;
import za.ac.cput.goldenconnect.service.ElderlyProfileService;
import za.ac.cput.goldenconnect.service.impl.ElderlyProfileServiceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ElderlyProfilesPanel extends VBox {
    
    private User currentUser;
    private ElderlyProfileService elderlyProfileService;
    
    // UI Components
    private TextField searchField;
    private ComboBox<String> filterCombo;
    private TableView<ElderlyProfileTableItem> profileTable;
    private ObservableList<ElderlyProfileTableItem> tableData;
    
    // Form fields
    private TextField fullNameField;
    private Spinner<Integer> ageSpinner;
    private TextArea medicalConditionArea;
    private TextField retirementVillageField;
    private TextField addressField;
    private TextField phoneField;
    private TextField emergencyContactField;
    private TextField emergencyPhoneField;
    private TextArea preferencesArea;
    private TextArea notesArea;
    private CheckBox activeCheckBox;
    
    // Buttons
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;
    private Button viewHistoryButton;
    
    // Statistics
    private Label totalProfilesLabel;
    private Label activeProfilesLabel;
    private Label averageAgeLabel;
    private Label totalSessionsLabel;
    
    private ElderlyProfile selectedProfile;
    
    public ElderlyProfilesPanel(User currentUser) {
        this.currentUser = currentUser;
        this.elderlyProfileService = new ElderlyProfileServiceImpl();
        this.tableData = FXCollections.observableArrayList();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        updateStatistics();
    }
    
    private void initializeComponents() {
        // Search and filter components
        searchField = new TextField();
        searchField.setPromptText("Search by name...");
        searchField.setPrefWidth(250);
        
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All Profiles", "Active Only", "Inactive Only", "By Village", "By Age Range");
        filterCombo.setValue("All Profiles");
        filterCombo.setPrefWidth(150);
        
        // Form fields
        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        
        ageSpinner = new Spinner<>(65, 120, 75);
        ageSpinner.setEditable(true);
        ageSpinner.setPrefWidth(100);
        
        medicalConditionArea = new TextArea();
        medicalConditionArea.setPromptText("Medical Condition");
        medicalConditionArea.setPrefRowCount(3);
        medicalConditionArea.setWrapText(true);
        
        retirementVillageField = new TextField();
        retirementVillageField.setPromptText("Retirement Village/Home");
        
        addressField = new TextField();
        addressField.setPromptText("Address");
        
        phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        
        emergencyContactField = new TextField();
        emergencyContactField.setPromptText("Emergency Contact Name");
        
        emergencyPhoneField = new TextField();
        emergencyPhoneField.setPromptText("Emergency Contact Phone");
        
        preferencesArea = new TextArea();
        preferencesArea.setPromptText("Preferences");
        preferencesArea.setPrefRowCount(2);
        preferencesArea.setWrapText(true);
        
        notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(2);
        notesArea.setWrapText(true);
        
        activeCheckBox = new CheckBox("Active Profile");
        activeCheckBox.setSelected(true);
        
        // Buttons
        addButton = new Button("Add Profile");
        addButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-weight: bold;");
        
        updateButton = new Button("Update Profile");
        updateButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-weight: bold;");
        updateButton.setDisable(true);
        
        deleteButton = new Button("Delete Profile");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setDisable(true);
        
        clearButton = new Button("Clear Form");
        clearButton.setStyle("-fx-background-color: #F5B700; -fx-text-fill: white; -fx-font-weight: bold;");
        
        viewHistoryButton = new Button("View Session History");
        viewHistoryButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold;");
        viewHistoryButton.setDisable(true);
        
        // Statistics labels
        totalProfilesLabel = new Label("Total: 0");
        activeProfilesLabel = new Label("Active: 0");
        averageAgeLabel = new Label("Avg Age: 0");
        totalSessionsLabel = new Label("Total Sessions: 0");
        
        // Style statistics labels
        String statsStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;";
        totalProfilesLabel.setStyle(statsStyle);
        activeProfilesLabel.setStyle(statsStyle);
        averageAgeLabel.setStyle(statsStyle);
        totalSessionsLabel.setStyle(statsStyle);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(20));
        setSpacing(20);
        
        // Header
        Label headerLabel = new Label("Elderly Profiles Management");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Statistics section
        VBox statsSection = createStatisticsSection();
        
        // Search and filter section
        HBox searchSection = new HBox(10);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        searchSection.getChildren().addAll(
            new Label("Search:"), searchField,
            new Label("Filter:"), filterCombo
        );
        
        // Main content area
        HBox mainContent = new HBox(20);
        mainContent.setAlignment(Pos.TOP_LEFT);
        
        // Left side - Form
        VBox formSection = createFormSection();
        
        // Right side - Table
        VBox tableSection = createTableSection();
        
        mainContent.getChildren().addAll(formSection, tableSection);
        
        getChildren().addAll(headerLabel, statsSection, searchSection, mainContent);
    }
    
    private VBox createStatisticsSection() {
        VBox statsSection = new VBox(10);
        statsSection.setAlignment(Pos.CENTER);
        statsSection.setPadding(new Insets(15));
        statsSection.setStyle("-fx-background-color: #0E2A47; -fx-background-radius: 10;");
        
        Label statsTitle = new Label("Profile Statistics");
        statsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        HBox statsGrid = new HBox(30);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.getChildren().addAll(totalProfilesLabel, activeProfilesLabel, averageAgeLabel, totalSessionsLabel);
        
        // Style the statistics labels for the stats section
        String statsStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;";
        totalProfilesLabel.setStyle(statsStyle);
        activeProfilesLabel.setStyle(statsStyle);
        averageAgeLabel.setStyle(statsStyle);
        totalSessionsLabel.setStyle(statsStyle);
        
        statsSection.getChildren().addAll(statsTitle, statsGrid);
        return statsSection;
    }
    
    private VBox createFormSection() {
        VBox formSection = new VBox(15);
        formSection.setAlignment(Pos.TOP_LEFT);
        formSection.setPadding(new Insets(20));
        formSection.setStyle("-fx-background-color: #F8F9FA; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10;");
        formSection.setPrefWidth(400);
        
        Label formTitle = new Label("Profile Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        
        // Row 0
        formGrid.add(new Label("Full Name*:"), 0, 0);
        formGrid.add(fullNameField, 1, 0);
        formGrid.add(new Label("Age*:"), 2, 0);
        formGrid.add(ageSpinner, 3, 0);
        
        // Row 1
        formGrid.add(new Label("Medical Condition*:"), 0, 1);
        formGrid.add(medicalConditionArea, 1, 1, 3, 1);
        
        // Row 2
        formGrid.add(new Label("Retirement Village*:"), 0, 2);
        formGrid.add(retirementVillageField, 1, 2, 3, 1);
        
        // Row 3
        formGrid.add(new Label("Address:"), 0, 3);
        formGrid.add(addressField, 1, 3, 3, 1);
        
        // Row 4
        formGrid.add(new Label("Phone:"), 0, 4);
        formGrid.add(phoneField, 1, 4);
        formGrid.add(new Label("Emergency Contact:"), 2, 4);
        formGrid.add(emergencyContactField, 3, 4);
        
        // Row 5
        formGrid.add(new Label("Emergency Phone:"), 0, 5);
        formGrid.add(emergencyPhoneField, 1, 5, 3, 1);
        
        // Row 6
        formGrid.add(new Label("Preferences:"), 0, 6);
        formGrid.add(preferencesArea, 1, 6, 3, 1);
        
        // Row 7
        formGrid.add(new Label("Notes:"), 0, 7);
        formGrid.add(notesArea, 1, 7, 3, 1);
        
        // Row 8
        formGrid.add(activeCheckBox, 1, 8, 3, 1);
        
        // Buttons
        HBox buttonRow1 = new HBox(10);
        buttonRow1.getChildren().addAll(addButton, updateButton, deleteButton);
        
        HBox buttonRow2 = new HBox(10);
        buttonRow2.getChildren().addAll(clearButton, viewHistoryButton);
        
        formSection.getChildren().addAll(formTitle, formGrid, buttonRow1, buttonRow2);
        return formSection;
    }
    
    private VBox createTableSection() {
        VBox tableSection = new VBox(10);
        tableSection.setAlignment(Pos.TOP_LEFT);
        tableSection.setPadding(new Insets(20));
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10;");
        tableSection.setPrefWidth(600);
        
        Label tableTitle = new Label("Elderly Profiles");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Create table
        profileTable = new TableView<>();
        profileTable.setPlaceholder(new Label("No elderly profiles found"));
        
        // Create columns
        TableColumn<ElderlyProfileTableItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(150);
        
        TableColumn<ElderlyProfileTableItem, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(60);
        
        TableColumn<ElderlyProfileTableItem, String> villageCol = new TableColumn<>("Village");
        villageCol.setCellValueFactory(new PropertyValueFactory<>("retirementVillage"));
        villageCol.setPrefWidth(120);
        
        TableColumn<ElderlyProfileTableItem, String> medicalCol = new TableColumn<>("Medical Condition");
        medicalCol.setCellValueFactory(new PropertyValueFactory<>("medicalCondition"));
        medicalCol.setPrefWidth(150);
        
        TableColumn<ElderlyProfileTableItem, Integer> sessionsCol = new TableColumn<>("Sessions");
        sessionsCol.setCellValueFactory(new PropertyValueFactory<>("totalSessions"));
        sessionsCol.setPrefWidth(80);
        
        TableColumn<ElderlyProfileTableItem, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        ratingCol.setPrefWidth(80);
        
        TableColumn<ElderlyProfileTableItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);
        
        profileTable.getColumns().addAll(nameCol, ageCol, villageCol, medicalCol, sessionsCol, ratingCol, statusCol);
        profileTable.setItems(tableData);
        
        tableSection.getChildren().addAll(tableTitle, profileTable);
        return tableSection;
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProfiles();
        });
        
        // Filter functionality
        filterCombo.setOnAction(e -> {
            applyFilter();
        });
        
        // Table selection
        profileTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadProfileToForm(newValue.getProfile());
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                viewHistoryButton.setDisable(false);
            } else {
                clearForm();
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                viewHistoryButton.setDisable(true);
            }
        });
        
        // Button handlers
        addButton.setOnAction(e -> addProfile());
        updateButton.setOnAction(e -> updateProfile());
        deleteButton.setOnAction(e -> deleteProfile());
        clearButton.setOnAction(e -> clearForm());
        viewHistoryButton.setOnAction(e -> viewSessionHistory());
    }
    
    private void loadData() {
        try {
            List<ElderlyProfile> profiles = elderlyProfileService.getAllProfiles();
            tableData.clear();
            
            for (ElderlyProfile profile : profiles) {
                tableData.add(new ElderlyProfileTableItem(profile));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load profiles: " + e.getMessage());
        }
    }
    
    private void searchProfiles() {
        try {
            String searchTerm = searchField.getText().trim();
            List<ElderlyProfile> profiles = elderlyProfileService.searchProfilesByName(searchTerm);
            
            tableData.clear();
            for (ElderlyProfile profile : profiles) {
                tableData.add(new ElderlyProfileTableItem(profile));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search profiles: " + e.getMessage());
        }
    }
    
    private void applyFilter() {
        try {
            String filter = filterCombo.getValue();
            List<ElderlyProfile> profiles = null;
            
            switch (filter) {
                case "All Profiles":
                    profiles = elderlyProfileService.getAllProfiles();
                    break;
                case "Active Only":
                    profiles = elderlyProfileService.getActiveProfiles();
                    break;
                case "Inactive Only":
                    profiles = elderlyProfileService.getAllProfiles().stream()
                        .filter(p -> !p.isActive())
                        .toList();
                    break;
                case "By Village":
                    // This would need a dialog to select village
                    profiles = elderlyProfileService.getAllProfiles();
                    break;
                case "By Age Range":
                    // This would need a dialog to select age range
                    profiles = elderlyProfileService.getAllProfiles();
                    break;
            }
            
            if (profiles != null) {
                tableData.clear();
                for (ElderlyProfile profile : profiles) {
                    tableData.add(new ElderlyProfileTableItem(profile));
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to apply filter: " + e.getMessage());
        }
    }
    
    private void addProfile() {
        try {
            ElderlyProfile profile = createProfileFromForm();
            if (profile != null) {
                ElderlyProfile createdProfile = elderlyProfileService.createProfile(profile);
                if (createdProfile != null) {
                    tableData.add(new ElderlyProfileTableItem(createdProfile));
                    clearForm();
                    updateStatistics();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile created successfully!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create profile: " + e.getMessage());
        }
    }
    
    private void updateProfile() {
        try {
            if (selectedProfile == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a profile to update");
                return;
            }
            
            ElderlyProfile profile = createProfileFromForm();
            if (profile != null) {
                profile.setId(selectedProfile.getId());
                ElderlyProfile updatedProfile = elderlyProfileService.updateProfile(profile);
                if (updatedProfile != null) {
                    // Update the table item
                    ElderlyProfileTableItem selectedItem = profileTable.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        selectedItem.updateFromProfile(updatedProfile);
                        profileTable.refresh();
                    }
                    clearForm();
                    updateStatistics();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile: " + e.getMessage());
        }
    }
    
    private void deleteProfile() {
        try {
            if (selectedProfile == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a profile to delete");
                return;
            }
            
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setHeaderText("Delete Profile");
            confirmDialog.setContentText("Are you sure you want to delete the profile for " + selectedProfile.getFullName() + "?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = elderlyProfileService.deleteProfile(selectedProfile.getId());
                    if (deleted) {
                        ElderlyProfileTableItem selectedItem = profileTable.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            tableData.remove(selectedItem);
                        }
                        clearForm();
                        updateStatistics();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Profile deleted successfully!");
                    }
                }
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete profile: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        fullNameField.clear();
        ageSpinner.getValueFactory().setValue(75);
        medicalConditionArea.clear();
        retirementVillageField.clear();
        addressField.clear();
        phoneField.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        preferencesArea.clear();
        notesArea.clear();
        activeCheckBox.setSelected(true);
        
        selectedProfile = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        viewHistoryButton.setDisable(true);
    }
    
    private void viewSessionHistory() {
        if (selectedProfile == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a profile to view history");
            return;
        }
        
        // This would open a new window/dialog showing session history
        showAlert(Alert.AlertType.INFORMATION, "Session History", 
            "Session history for " + selectedProfile.getFullName() + ":\n" +
            "Total Sessions: " + selectedProfile.getTotalSessions() + "\n" +
            "Average Rating: " + String.format("%.1f", selectedProfile.getAverageRating()));
    }
    
    private ElderlyProfile createProfileFromForm() {
        String fullName = fullNameField.getText().trim();
        int age = ageSpinner.getValue();
        String medicalCondition = medicalConditionArea.getText().trim();
        String retirementVillage = retirementVillageField.getText().trim();
        
        if (fullName.isEmpty() || medicalCondition.isEmpty() || retirementVillage.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all required fields (*)");
            return null;
        }
        
        ElderlyProfile profile = new ElderlyProfile(fullName, age, medicalCondition, retirementVillage);
        profile.setAddress(addressField.getText().trim());
        profile.setPhoneNumber(phoneField.getText().trim());
        profile.setEmergencyContact(emergencyContactField.getText().trim());
        profile.setEmergencyContactPhone(emergencyPhoneField.getText().trim());
        profile.setPreferences(preferencesArea.getText().trim());
        profile.setNotes(notesArea.getText().trim());
        profile.setActive(activeCheckBox.isSelected());
        
        return profile;
    }
    
    private void loadProfileToForm(ElderlyProfile profile) {
        selectedProfile = profile;
        fullNameField.setText(profile.getFullName());
        ageSpinner.getValueFactory().setValue(profile.getAge());
        medicalConditionArea.setText(profile.getMedicalCondition());
        retirementVillageField.setText(profile.getRetirementVillage());
        addressField.setText(profile.getAddress());
        phoneField.setText(profile.getPhoneNumber());
        emergencyContactField.setText(profile.getEmergencyContact());
        emergencyPhoneField.setText(profile.getEmergencyContactPhone());
        preferencesArea.setText(profile.getPreferences());
        notesArea.setText(profile.getNotes());
        activeCheckBox.setSelected(profile.isActive());
    }
    
    private void updateStatistics() {
        try {
            ElderlyProfileService.ProfileStatistics stats = elderlyProfileService.getProfileStatistics();
            totalProfilesLabel.setText("Total: " + stats.getTotalProfiles());
            activeProfilesLabel.setText("Active: " + stats.getActiveProfiles());
            averageAgeLabel.setText("Avg Age: " + String.format("%.1f", stats.getAverageAge()));
            totalSessionsLabel.setText("Total Sessions: " + stats.getTotalSessions());
        } catch (Exception e) {
            System.err.println("Error updating statistics: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner class for table items
    public static class ElderlyProfileTableItem {
        private final ElderlyProfile profile;
        
        public ElderlyProfileTableItem(ElderlyProfile profile) {
            this.profile = profile;
        }
        
        public void updateFromProfile(ElderlyProfile updatedProfile) {
            // Update the underlying profile
            profile.setFullName(updatedProfile.getFullName());
            profile.setAge(updatedProfile.getAge());
            profile.setMedicalCondition(updatedProfile.getMedicalCondition());
            profile.setRetirementVillage(updatedProfile.getRetirementVillage());
            profile.setTotalSessions(updatedProfile.getTotalSessions());
            profile.setAverageRating(updatedProfile.getAverageRating());
            profile.setActive(updatedProfile.isActive());
        }
        
        // Getters for table columns
        public String getFullName() { return profile.getFullName(); }
        public Integer getAge() { return profile.getAge(); }
        public String getRetirementVillage() { return profile.getRetirementVillage(); }
        public String getMedicalCondition() { return profile.getMedicalCondition(); }
        public Integer getTotalSessions() { return profile.getTotalSessions(); }
        public Double getAverageRating() { return profile.getAverageRating(); }
        public String getStatus() { return profile.isActive() ? "Active" : "Inactive"; }
        
        public ElderlyProfile getProfile() { return profile; }
    }
}
