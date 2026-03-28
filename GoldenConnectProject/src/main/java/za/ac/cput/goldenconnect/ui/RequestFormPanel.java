package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import za.ac.cput.goldenconnect.service.RequestService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Request;

public class RequestFormPanel extends VBox {
    
    private RequestService requestService;
    private User currentUser;
    
    private TextField titleField;
    private ComboBox<String> typeCombo;
    private TextArea descriptionArea;
    private ComboBox<String> locationCombo;
    private TextField durationField;
    private TextArea requirementsArea;
    private Button submitButton;
    private Button clearButton;
    
    public RequestFormPanel(RequestService requestService, User currentUser) {
        this.requestService = requestService;
        this.currentUser = currentUser;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
    }
    
    private void initializeComponents() {
        titleField = new TextField();
        titleField.setPromptText("Enter request title");
        titleField.setPrefWidth(400);
        
        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("COMPANIONSHIP", "TECH_HELP", "ACTIVITY", "HEALTH_CHECK");
        typeCombo.setValue("COMPANIONSHIP");
        typeCombo.setPrefWidth(400);
        
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe your request in detail");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPrefWidth(400);
        
        locationCombo = new ComboBox<>();
        locationCombo.getItems().addAll("Virtual", "Physical", "Hybrid");
        locationCombo.setValue("Virtual");
        locationCombo.setPrefWidth(400);
        
        durationField = new TextField();
        durationField.setPromptText("Duration in minutes (e.g., 60)");
        durationField.setPrefWidth(400);
        
        requirementsArea = new TextArea();
        requirementsArea.setPromptText("Any special requirements or preferences");
        requirementsArea.setPrefRowCount(3);
        requirementsArea.setPrefWidth(400);
        
        submitButton = new Button("Submit Request");
        submitButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        submitButton.setPrefWidth(200);
        
        clearButton = new Button("Clear Form");
        clearButton.setStyle("-fx-background-color: #6B7280; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        clearButton.setPrefWidth(200);
    }
    
    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setSpacing(20);
        
        Label titleLabel = new Label("Create New Request");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        Label titleFieldLabel = new Label("Title:");
        titleFieldLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label typeLabel = new Label("Request Type:");
        typeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label locationLabel = new Label("Location:");
        locationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label durationLabel = new Label("Duration (minutes):");
        durationLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label requirementsLabel = new Label("Special Requirements:");
        requirementsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        formGrid.add(titleFieldLabel, 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(typeLabel, 0, 1);
        formGrid.add(typeCombo, 1, 1);
        formGrid.add(descriptionLabel, 0, 2);
        formGrid.add(descriptionArea, 1, 2);
        formGrid.add(locationLabel, 0, 3);
        formGrid.add(locationCombo, 1, 3);
        formGrid.add(durationLabel, 0, 4);
        formGrid.add(durationField, 1, 4);
        formGrid.add(requirementsLabel, 0, 5);
        formGrid.add(requirementsArea, 1, 5);
        
        // Button layout
        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(submitButton, clearButton);
        
        getChildren().addAll(titleLabel, formGrid, buttonBox);
    }
    
    private void setupEventHandlers() {
        submitButton.setOnAction(e -> handleSubmit());
        clearButton.setOnAction(e -> clearForm());
    }
    
    private void handleSubmit() {
        if (currentUser == null) {
            showAlert(AlertType.ERROR, "Error", "User not logged in");
            return;
        }
        
        String title = titleField.getText();
        String type = typeCombo.getValue();
        String description = descriptionArea.getText();
        String location = locationCombo.getValue();
        String durationText = durationField.getText();
        String requirements = requirementsArea.getText();
        
        if (title.isEmpty() || description.isEmpty() || durationText.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
            return;
        }
        
        try {
            int duration = Integer.parseInt(durationText);
            
            Request request = new Request();
            request.setRequesterId(currentUser.getId());
            request.setRequestType(type);
            request.setDescription(description);
            request.setLocation(location);
            request.setDuration(duration);
            request.setSpecialRequirements(requirements);
            
            Request createdRequest = requestService.createRequest(request);
            if (createdRequest != null && createdRequest.getId() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Request submitted successfully!");
                clearForm();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to submit request");
            }
        } catch (NumberFormatException ex) {
            showAlert(AlertType.ERROR, "Error", "Please enter a valid duration");
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Failed to submit request: " + ex.getMessage());
        }
    }
    
    private void clearForm() {
        titleField.clear();
        typeCombo.setValue("COMPANIONSHIP");
        descriptionArea.clear();
        locationCombo.setValue("Virtual");
        durationField.clear();
        requirementsArea.clear();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
