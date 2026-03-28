package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import za.ac.cput.goldenconnect.service.RequestService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Request;
import java.util.List;

public class MyRequestsPanel extends VBox {
    
    private RequestService requestService;
    private User currentUser;
    private ListView<String> requestsListView;
    private Button refreshButton;
    private Button newRequestButton;
    
    public MyRequestsPanel(RequestService requestService, User currentUser) {
        this.requestService = requestService;
        this.currentUser = currentUser;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadRequests();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadRequests();
    }
    
    private void initializeComponents() {
        requestsListView = new ListView<>();
        requestsListView.setPrefHeight(400);
        
        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
        
        newRequestButton = new Button("New Request");
        newRequestButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");
    }
    
    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setSpacing(20);
        
        Label titleLabel = new Label("My Requests");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        Label subtitleLabel = new Label("View and manage your service requests");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(refreshButton, newRequestButton);
        
        getChildren().addAll(titleLabel, subtitleLabel, requestsListView, buttonBox);
    }
    
    private void setupEventHandlers() {
        refreshButton.setOnAction(e -> loadRequests());
        newRequestButton.setOnAction(e -> handleNewRequest());
    }
    
    private void loadRequests() {
        if (currentUser == null) {
            requestsListView.getItems().clear();
            requestsListView.getItems().add("Please log in to view your requests");
            return;
        }
        
        try {
            List<Request> requests = requestService.findRequestsByUser(currentUser.getId());
            requestsListView.getItems().clear();
            
            if (requests.isEmpty()) {
                requestsListView.getItems().add("No requests found. Create your first request!");
            } else {
                for (Request request : requests) {
                    String requestText = String.format("ID: %d | Type: %s | Status: %s | Date: %s",
                        request.getId(),
                        request.getRequestType(),
                        request.getStatus(),
                        request.getRequestedDate() != null ? request.getRequestedDate().toString() : "N/A"
                    );
                    requestsListView.getItems().add(requestText);
                }
            }
        } catch (Exception ex) {
            requestsListView.getItems().clear();
            requestsListView.getItems().add("Error loading requests: " + ex.getMessage());
        }
    }
    
    private void handleNewRequest() {
        showAlert(AlertType.INFORMATION, "New Request", "Navigate to the request form to create a new request.");
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
