package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Volunteer;
import za.ac.cput.goldenconnect.service.VolunteerService;
import za.ac.cput.goldenconnect.service.impl.VolunteerServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VolunteersPanel extends VBox {
    
    private User currentUser;
    private VolunteerService volunteerService;
    
    // UI Components
    private TextField searchField;
    private ComboBox<String> filterCombo;
    private TableView<VolunteerTableItem> volunteerTable;
    private ObservableList<VolunteerTableItem> tableData;
    
    // Form fields
    private TextField fullNameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField addressField;
    private DatePicker dateOfBirthPicker;
    private TextArea skillsArea;
    private TextArea interestsArea;
    private TextArea availabilityArea;
    private TextField emergencyContactField;
    private TextField emergencyPhoneField;
    private TextField backgroundCheckField;
    private TextField trainingCompletedField;
    private TextArea preferencesArea;
    private TextArea notesArea;
    private ComboBox<String> experienceLevelCombo;
    private TextField certificationField;
    private CheckBox activeCheckBox;
    
    // Buttons
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;
    private Button viewHistoryButton;
    
    // Statistics
    private Label totalVolunteersLabel;
    private Label activeVolunteersLabel;
    private Label totalSessionsLabel;
    private Label totalHoursLabel;
    private Label averageRatingLabel;
    private Label backgroundCheckLabel;
    private Label trainingCompletedLabel;
    
    private Volunteer selectedVolunteer;
    
    public VolunteersPanel(User currentUser) {
        this.currentUser = currentUser;
        this.volunteerService = new VolunteerServiceImpl();
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
        searchField.setPromptText("Search by name or email...");
        searchField.setPrefWidth(300);
        
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All Volunteers", "Active Only", "Inactive Only", "By Skills", "By Experience", "With Background Check", "With Training");
        filterCombo.setValue("All Volunteers");
        filterCombo.setPrefWidth(180);
        
        // Form fields
        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        
        emailField = new TextField();
        emailField.setPromptText("Email Address");
        
        phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        
        addressField = new TextField();
        addressField.setPromptText("Address");
        
        dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setPromptText("Date of Birth");
        
        skillsArea = new TextArea();
        skillsArea.setPromptText("Skills");
        skillsArea.setPrefRowCount(3);
        skillsArea.setWrapText(true);
        
        interestsArea = new TextArea();
        interestsArea.setPromptText("Interests");
        interestsArea.setPrefRowCount(2);
        interestsArea.setWrapText(true);
        
        availabilityArea = new TextArea();
        availabilityArea.setPromptText("Availability (e.g., Weekdays 9-5, Weekends)");
        availabilityArea.setPrefRowCount(2);
        availabilityArea.setWrapText(true);
        
        emergencyContactField = new TextField();
        emergencyContactField.setPromptText("Emergency Contact Name");
        
        emergencyPhoneField = new TextField();
        emergencyPhoneField.setPromptText("Emergency Contact Phone");
        
        backgroundCheckField = new TextField();
        backgroundCheckField.setPromptText("Background Check Status");
        
        trainingCompletedField = new TextField();
        trainingCompletedField.setPromptText("Training Completed");
        
        preferencesArea = new TextArea();
        preferencesArea.setPromptText("Preferences");
        preferencesArea.setPrefRowCount(2);
        preferencesArea.setWrapText(true);
        
        notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(2);
        notesArea.setWrapText(true);
        
        experienceLevelCombo = new ComboBox<>();
        experienceLevelCombo.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        experienceLevelCombo.setPromptText("Experience Level");
        
        certificationField = new TextField();
        certificationField.setPromptText("Certifications");
        
        activeCheckBox = new CheckBox("Active Volunteer");
        activeCheckBox.setSelected(true);
        
        // Buttons
        addButton = new Button("Add Volunteer");
        addButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-weight: bold;");
        
        updateButton = new Button("Update Volunteer");
        updateButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-weight: bold;");
        updateButton.setDisable(true);
        
        deleteButton = new Button("Delete Volunteer");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setDisable(true);
        
        clearButton = new Button("Clear Form");
        clearButton.setStyle("-fx-background-color: #F5B700; -fx-text-fill: white; -fx-font-weight: bold;");
        
        viewHistoryButton = new Button("View Session History");
        viewHistoryButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold;");
        viewHistoryButton.setDisable(true);
        
        // Statistics labels
        totalVolunteersLabel = new Label("Total: 0");
        activeVolunteersLabel = new Label("Active: 0");
        totalSessionsLabel = new Label("Total Sessions: 0");
        totalHoursLabel = new Label("Total Hours: 0");
        averageRatingLabel = new Label("Avg Rating: 0.0");
        backgroundCheckLabel = new Label("Background Check: 0");
        trainingCompletedLabel = new Label("Training Completed: 0");
        
        // Style statistics labels
        String statsStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;";
        totalVolunteersLabel.setStyle(statsStyle);
        activeVolunteersLabel.setStyle(statsStyle);
        totalSessionsLabel.setStyle(statsStyle);
        totalHoursLabel.setStyle(statsStyle);
        averageRatingLabel.setStyle(statsStyle);
        backgroundCheckLabel.setStyle(statsStyle);
        trainingCompletedLabel.setStyle(statsStyle);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(20));
        setSpacing(20);
        
        // Header
        Label headerLabel = new Label("Volunteers Management");
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
        HBox mainContent = new HBox(15);
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
        
        Label statsTitle = new Label("Volunteer Statistics");
        statsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        HBox statsGrid = new HBox(20);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.getChildren().addAll(totalVolunteersLabel, activeVolunteersLabel, totalSessionsLabel, totalHoursLabel, averageRatingLabel, backgroundCheckLabel, trainingCompletedLabel);
        
        // Style the statistics labels for the stats section
        String statsStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;";
        totalVolunteersLabel.setStyle(statsStyle);
        activeVolunteersLabel.setStyle(statsStyle);
        totalSessionsLabel.setStyle(statsStyle);
        totalHoursLabel.setStyle(statsStyle);
        averageRatingLabel.setStyle(statsStyle);
        backgroundCheckLabel.setStyle(statsStyle);
        trainingCompletedLabel.setStyle(statsStyle);
        
        statsSection.getChildren().addAll(statsTitle, statsGrid);
        return statsSection;
    }
    
    private VBox createFormSection() {
        VBox formSection = new VBox(10);
        formSection.setAlignment(Pos.TOP_LEFT);
        formSection.setPadding(new Insets(15));
        formSection.setStyle("-fx-background-color: #F8F9FA; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10;");
        formSection.setPrefWidth(400);
        formSection.setMaxHeight(600); // Limit height to ensure buttons are visible
        
        Label formTitle = new Label("Volunteer Information");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        
        // Row 0
        formGrid.add(new Label("Full Name*:"), 0, 0);
        formGrid.add(fullNameField, 1, 0);
        formGrid.add(new Label("Email*:"), 2, 0);
        formGrid.add(emailField, 3, 0);
        
        // Row 1
        formGrid.add(new Label("Phone*:"), 0, 1);
        formGrid.add(phoneField, 1, 1);
        formGrid.add(new Label("Date of Birth:"), 2, 1);
        formGrid.add(dateOfBirthPicker, 3, 1);
        
        // Row 2
        formGrid.add(new Label("Address:"), 0, 2);
        formGrid.add(addressField, 1, 2, 3, 1);
        
        // Row 3
        formGrid.add(new Label("Skills:"), 0, 3);
        formGrid.add(skillsArea, 1, 3, 3, 1);
        
        // Row 4
        formGrid.add(new Label("Interests:"), 0, 4);
        formGrid.add(interestsArea, 1, 4, 3, 1);
        
        // Row 5
        formGrid.add(new Label("Availability:"), 0, 5);
        formGrid.add(availabilityArea, 1, 5, 3, 1);
        
        // Row 6
        formGrid.add(new Label("Emergency Contact:"), 0, 6);
        formGrid.add(emergencyContactField, 1, 6);
        formGrid.add(new Label("Emergency Phone:"), 2, 6);
        formGrid.add(emergencyPhoneField, 3, 6);
        
        // Row 7
        formGrid.add(new Label("Background Check:"), 0, 7);
        formGrid.add(backgroundCheckField, 1, 7);
        formGrid.add(new Label("Training Completed:"), 2, 7);
        formGrid.add(trainingCompletedField, 3, 7);
        
        // Row 8
        formGrid.add(new Label("Experience Level:"), 0, 8);
        formGrid.add(experienceLevelCombo, 1, 8);
        formGrid.add(new Label("Certifications:"), 2, 8);
        formGrid.add(certificationField, 3, 8);
        
        // Row 9
        formGrid.add(new Label("Preferences:"), 0, 9);
        formGrid.add(preferencesArea, 1, 9, 3, 1);
        
        // Row 10
        formGrid.add(new Label("Notes:"), 0, 10);
        formGrid.add(notesArea, 1, 10, 3, 1);
        
        // Row 11
        formGrid.add(activeCheckBox, 1, 11, 3, 1);
        
        // Buttons
        HBox buttonRow1 = new HBox(8);
        buttonRow1.setAlignment(Pos.CENTER);
        buttonRow1.getChildren().addAll(addButton, updateButton, deleteButton);
        
        HBox buttonRow2 = new HBox(8);
        buttonRow2.setAlignment(Pos.CENTER);
        buttonRow2.getChildren().addAll(clearButton, viewHistoryButton);
        
        // Create a scrollable container for the form
        VBox formContent = new VBox(10);
        formContent.getChildren().addAll(formTitle, formGrid, buttonRow1, buttonRow2);
        
        ScrollPane formScrollPane = new ScrollPane(formContent);
        formScrollPane.setFitToWidth(true);
        formScrollPane.setFitToHeight(true);
        formScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        formScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        formScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        formScrollPane.setPadding(Insets.EMPTY);
        
        formSection.getChildren().add(formScrollPane);
        return formSection;
    }
    
    private VBox createTableSection() {
        VBox tableSection = new VBox(10);
        tableSection.setAlignment(Pos.TOP_LEFT);
        tableSection.setPadding(new Insets(20));
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #DEE2E6; -fx-border-radius: 10;");
        tableSection.setPrefWidth(600);
        
        Label tableTitle = new Label("Volunteers");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Create table
        volunteerTable = new TableView<>();
        volunteerTable.setPlaceholder(new Label("No volunteers found"));
        
        // Create columns
        TableColumn<VolunteerTableItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(150);
        
        TableColumn<VolunteerTableItem, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(180);
        
        TableColumn<VolunteerTableItem, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneCol.setPrefWidth(120);
        
        TableColumn<VolunteerTableItem, String> registrationCol = new TableColumn<>("Registration Date");
        registrationCol.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        registrationCol.setPrefWidth(120);
        
        TableColumn<VolunteerTableItem, Integer> sessionsCol = new TableColumn<>("Sessions");
        sessionsCol.setCellValueFactory(new PropertyValueFactory<>("totalSessionsCompleted"));
        sessionsCol.setPrefWidth(80);
        
        TableColumn<VolunteerTableItem, Integer> hoursCol = new TableColumn<>("Hours");
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        hoursCol.setPrefWidth(80);
        
        TableColumn<VolunteerTableItem, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        ratingCol.setPrefWidth(80);
        
        TableColumn<VolunteerTableItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);
        
        volunteerTable.getColumns().addAll(nameCol, emailCol, phoneCol, registrationCol, sessionsCol, hoursCol, ratingCol, statusCol);
        volunteerTable.setItems(tableData);
        
        tableSection.getChildren().addAll(tableTitle, volunteerTable);
        return tableSection;
    }
    
    private void setupEventHandlers() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchVolunteers();
        });
        
        // Filter functionality
        filterCombo.setOnAction(e -> {
            applyFilter();
        });
        
        // Table selection
        volunteerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadVolunteerToForm(newValue.getVolunteer());
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
        addButton.setOnAction(e -> addVolunteer());
        updateButton.setOnAction(e -> updateVolunteer());
        deleteButton.setOnAction(e -> deleteVolunteer());
        clearButton.setOnAction(e -> clearForm());
        viewHistoryButton.setOnAction(e -> viewSessionHistory());
    }
    
    private void loadData() {
        try {
            List<Volunteer> volunteers = volunteerService.getAllVolunteers();
            tableData.clear();
            
            for (Volunteer volunteer : volunteers) {
                tableData.add(new VolunteerTableItem(volunteer));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load volunteers: " + e.getMessage());
        }
    }
    
    private void searchVolunteers() {
        try {
            String searchTerm = searchField.getText().trim();
            List<Volunteer> volunteers = volunteerService.searchVolunteersByName(searchTerm);
            
            tableData.clear();
            for (Volunteer volunteer : volunteers) {
                tableData.add(new VolunteerTableItem(volunteer));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to search volunteers: " + e.getMessage());
        }
    }
    
    private void applyFilter() {
        try {
            String filter = filterCombo.getValue();
            List<Volunteer> volunteers = null;
            
            switch (filter) {
                case "All Volunteers":
                    volunteers = volunteerService.getAllVolunteers();
                    break;
                case "Active Only":
                    volunteers = volunteerService.getActiveVolunteers();
                    break;
                case "Inactive Only":
                    volunteers = volunteerService.getAllVolunteers().stream()
                        .filter(v -> !v.isActive())
                        .toList();
                    break;
                case "By Skills":
                    // This would need a dialog to select skills
                    volunteers = volunteerService.getAllVolunteers();
                    break;
                case "By Experience":
                    // This would need a dialog to select experience level
                    volunteers = volunteerService.getAllVolunteers();
                    break;
                case "With Background Check":
                    volunteers = volunteerService.getVolunteersWithBackgroundCheck();
                    break;
                case "With Training":
                    volunteers = volunteerService.getVolunteersWithTrainingCompleted();
                    break;
            }
            
            if (volunteers != null) {
                tableData.clear();
                for (Volunteer volunteer : volunteers) {
                    tableData.add(new VolunteerTableItem(volunteer));
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to apply filter: " + e.getMessage());
        }
    }
    
    private void addVolunteer() {
        try {
            Volunteer volunteer = createVolunteerFromForm();
            if (volunteer != null) {
                Volunteer createdVolunteer = volunteerService.createVolunteer(volunteer);
                if (createdVolunteer != null) {
                    tableData.add(new VolunteerTableItem(createdVolunteer));
                    clearForm();
                    updateStatistics();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Volunteer created successfully!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create volunteer: " + e.getMessage());
        }
    }
    
    private void updateVolunteer() {
        try {
            if (selectedVolunteer == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a volunteer to update");
                return;
            }
            
            Volunteer volunteer = createVolunteerFromForm();
            if (volunteer != null) {
                volunteer.setId(selectedVolunteer.getId());
                Volunteer updatedVolunteer = volunteerService.updateVolunteer(volunteer);
                if (updatedVolunteer != null) {
                    // Update the table item
                    VolunteerTableItem selectedItem = volunteerTable.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        selectedItem.updateFromVolunteer(updatedVolunteer);
                        volunteerTable.refresh();
                    }
                    clearForm();
                    updateStatistics();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Volunteer updated successfully!");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update volunteer: " + e.getMessage());
        }
    }
    
    private void deleteVolunteer() {
        try {
            if (selectedVolunteer == null) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please select a volunteer to delete");
                return;
            }
            
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setHeaderText("Delete Volunteer");
            confirmDialog.setContentText("Are you sure you want to delete the volunteer " + selectedVolunteer.getFullName() + "?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = volunteerService.deleteVolunteer(selectedVolunteer.getId());
                    if (deleted) {
                        VolunteerTableItem selectedItem = volunteerTable.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            tableData.remove(selectedItem);
                        }
                        clearForm();
                        updateStatistics();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Volunteer deleted successfully!");
                    }
                }
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete volunteer: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        dateOfBirthPicker.setValue(null);
        skillsArea.clear();
        interestsArea.clear();
        availabilityArea.clear();
        emergencyContactField.clear();
        emergencyPhoneField.clear();
        backgroundCheckField.clear();
        trainingCompletedField.clear();
        preferencesArea.clear();
        notesArea.clear();
        experienceLevelCombo.setValue(null);
        certificationField.clear();
        activeCheckBox.setSelected(true);
        
        selectedVolunteer = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        viewHistoryButton.setDisable(true);
    }
    
    private void viewSessionHistory() {
        if (selectedVolunteer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a volunteer to view history");
            return;
        }
        
        // This would open a new window/dialog showing session history
        showAlert(Alert.AlertType.INFORMATION, "Session History", 
            "Session history for " + selectedVolunteer.getFullName() + ":\n" +
            "Total Sessions Completed: " + selectedVolunteer.getTotalSessionsCompleted() + "\n" +
            "Total Hours: " + selectedVolunteer.getTotalHours() + "\n" +
            "Average Rating: " + String.format("%.1f", selectedVolunteer.getAverageRating()) + "\n" +
            "Registration Date: " + selectedVolunteer.getRegistrationDateFormatted() + "\n" +
            "Last Active: " + selectedVolunteer.getLastActiveDateFormatted());
    }
    
    private Volunteer createVolunteerFromForm() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all required fields (*)");
            return null;
        }
        
        Volunteer volunteer = new Volunteer(fullName, email, phone);
        volunteer.setAddress(addressField.getText().trim());
        
        if (dateOfBirthPicker.getValue() != null) {
            volunteer.setDateOfBirth(dateOfBirthPicker.getValue().atStartOfDay());
        }
        
        volunteer.setSkills(skillsArea.getText().trim());
        volunteer.setInterests(interestsArea.getText().trim());
        volunteer.setAvailability(availabilityArea.getText().trim());
        volunteer.setEmergencyContact(emergencyContactField.getText().trim());
        volunteer.setEmergencyContactPhone(emergencyPhoneField.getText().trim());
        volunteer.setBackgroundCheck(backgroundCheckField.getText().trim());
        volunteer.setTrainingCompleted(trainingCompletedField.getText().trim());
        volunteer.setPreferences(preferencesArea.getText().trim());
        volunteer.setNotes(notesArea.getText().trim());
        volunteer.setExperienceLevel(experienceLevelCombo.getValue());
        volunteer.setCertification(certificationField.getText().trim());
        volunteer.setActive(activeCheckBox.isSelected());
        
        return volunteer;
    }
    
    private void loadVolunteerToForm(Volunteer volunteer) {
        selectedVolunteer = volunteer;
        fullNameField.setText(volunteer.getFullName());
        emailField.setText(volunteer.getEmail());
        phoneField.setText(volunteer.getPhoneNumber());
        addressField.setText(volunteer.getAddress());
        
        if (volunteer.getDateOfBirth() != null) {
            dateOfBirthPicker.setValue(volunteer.getDateOfBirth().toLocalDate());
        }
        
        skillsArea.setText(volunteer.getSkills());
        interestsArea.setText(volunteer.getInterests());
        availabilityArea.setText(volunteer.getAvailability());
        emergencyContactField.setText(volunteer.getEmergencyContact());
        emergencyPhoneField.setText(volunteer.getEmergencyContactPhone());
        backgroundCheckField.setText(volunteer.getBackgroundCheck());
        trainingCompletedField.setText(volunteer.getTrainingCompleted());
        preferencesArea.setText(volunteer.getPreferences());
        notesArea.setText(volunteer.getNotes());
        experienceLevelCombo.setValue(volunteer.getExperienceLevel());
        certificationField.setText(volunteer.getCertification());
        activeCheckBox.setSelected(volunteer.isActive());
    }
    
    private void updateStatistics() {
        try {
            VolunteerService.VolunteerStatistics stats = volunteerService.getVolunteerStatistics();
            totalVolunteersLabel.setText("Total: " + stats.getTotalVolunteers());
            activeVolunteersLabel.setText("Active: " + stats.getActiveVolunteers());
            totalSessionsLabel.setText("Total Sessions: " + stats.getTotalSessionsCompleted());
            totalHoursLabel.setText("Total Hours: " + stats.getTotalHours());
            averageRatingLabel.setText("Avg Rating: " + String.format("%.1f", stats.getAverageRating()));
            backgroundCheckLabel.setText("Background Check: " + stats.getVolunteersWithBackgroundCheck());
            trainingCompletedLabel.setText("Training Completed: " + stats.getVolunteersWithTraining());
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
    public static class VolunteerTableItem {
        private final Volunteer volunteer;
        
        public VolunteerTableItem(Volunteer volunteer) {
            this.volunteer = volunteer;
        }
        
        public void updateFromVolunteer(Volunteer updatedVolunteer) {
            // Update the underlying volunteer
            volunteer.setFullName(updatedVolunteer.getFullName());
            volunteer.setEmail(updatedVolunteer.getEmail());
            volunteer.setPhoneNumber(updatedVolunteer.getPhoneNumber());
            volunteer.setTotalSessionsCompleted(updatedVolunteer.getTotalSessionsCompleted());
            volunteer.setTotalHours(updatedVolunteer.getTotalHours());
            volunteer.setAverageRating(updatedVolunteer.getAverageRating());
            volunteer.setActive(updatedVolunteer.isActive());
        }
        
        // Getters for table columns
        public String getFullName() { return volunteer.getFullName(); }
        public String getEmail() { return volunteer.getEmail(); }
        public String getPhoneNumber() { return volunteer.getPhoneNumber(); }
        public String getRegistrationDate() { return volunteer.getRegistrationDateFormatted(); }
        public Integer getTotalSessionsCompleted() { return volunteer.getTotalSessionsCompleted(); }
        public Integer getTotalHours() { return volunteer.getTotalHours(); }
        public Double getAverageRating() { return volunteer.getAverageRating(); }
        public String getStatus() { return volunteer.isActive() ? "Active" : "Inactive"; }
        
        public Volunteer getVolunteer() { return volunteer; }
    }
}
