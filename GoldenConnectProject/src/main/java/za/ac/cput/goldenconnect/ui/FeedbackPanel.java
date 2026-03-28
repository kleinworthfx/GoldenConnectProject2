package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import za.ac.cput.goldenconnect.service.FeedbackService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Feedback;
import za.ac.cput.goldenconnect.dao.FeedbackDAO;
import za.ac.cput.goldenconnect.dao.UserDAO;
import za.ac.cput.goldenconnect.dao.impl.FeedbackDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.UserDAOImpl;

public class FeedbackPanel extends VBox {
    
    private FeedbackService feedbackService;
    private User currentUser;
    private FeedbackDAO feedbackDAO;
    private UserDAO userDAO;
    
    // Form components
    private ComboBox<User> elderlyCombo;
    private Spinner<Integer> ratingSpinner;
    private TextArea commentsArea;
    private Button submitFeedbackButton;
    
    // Table components
    private TableView<FeedbackTableItem> feedbackTable;
    private ObservableList<FeedbackTableItem> feedbackData;
    private FilteredList<FeedbackTableItem> filteredFeedbackData;
    
    public FeedbackPanel(FeedbackService feedbackService, User currentUser) {
        this.feedbackService = feedbackService;
        this.currentUser = currentUser;
        this.feedbackDAO = new FeedbackDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.feedbackData = FXCollections.observableArrayList();
        this.filteredFeedbackData = new FilteredList<>(feedbackData, p -> true);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadFeedbackData();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadFeedbackData();
    }
    
    private void initializeComponents() {
        // Elderly dropdown
        elderlyCombo = new ComboBox<>();
        elderlyCombo.setPrefWidth(250);
        loadElderlyUsers();
        
        // Rating spinner (1-5)
        ratingSpinner = new Spinner<>();
        ratingSpinner.setPrefWidth(100);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5);
        ratingSpinner.setValueFactory(valueFactory);
        ratingSpinner.setEditable(true);
        
        // Comments area
        commentsArea = new TextArea();
        commentsArea.setPromptText("Enter your feedback comments here...");
        commentsArea.setPrefRowCount(4);
        commentsArea.setPrefWidth(400);
        commentsArea.setWrapText(true);
        
        // Submit Feedback button
        submitFeedbackButton = new Button("Submit Feedback");
        submitFeedbackButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        
        // Initialize table
        initializeTable();
    }
    
    private void initializeTable() {
        feedbackTable = new TableView<>();
        feedbackTable.setPrefHeight(300);
        feedbackTable.setItems(filteredFeedbackData);
        
        // Create table columns
        TableColumn<FeedbackTableItem, String> elderlyCol = new TableColumn<>("ELDERLY");
        elderlyCol.setCellValueFactory(new PropertyValueFactory<>("elderlyName"));
        elderlyCol.setPrefWidth(150);
        
        TableColumn<FeedbackTableItem, String> ratingCol = new TableColumn<>("RATING");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingCol.setPrefWidth(100);
        
        TableColumn<FeedbackTableItem, String> commentsCol = new TableColumn<>("COMMENTS");
        commentsCol.setCellValueFactory(new PropertyValueFactory<>("comments"));
        commentsCol.setPrefWidth(250);
        
        TableColumn<FeedbackTableItem, String> dateCol = new TableColumn<>("DATE");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(200);
        
        // Actions column with Edit and Delete buttons
        TableColumn<FeedbackTableItem, Void> actionsCol = new TableColumn<>("ACTIONS");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<FeedbackTableItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5);
            
            {
                editButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                deleteButton.setStyle("-fx-background-color: #FF5A4A; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                buttonBox.getChildren().addAll(editButton, deleteButton);
                
                editButton.setOnAction(e -> {
                    FeedbackTableItem feedback = getTableView().getItems().get(getIndex());
                    handleEditFeedback(feedback);
                });
                
                deleteButton.setOnAction(e -> {
                    FeedbackTableItem feedback = getTableView().getItems().get(getIndex());
                    handleDeleteFeedback(feedback);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
        
        feedbackTable.getColumns().addAll(elderlyCol, ratingCol, commentsCol, dateCol, actionsCol);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(30);
        
        // Feedback Form Section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.TOP_LEFT);
        
        Label formTitle = new Label("Feedback");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(20);
        formGrid.setAlignment(Pos.CENTER_LEFT);
        
        // Form labels
        Label elderlyLabel = new Label("Elderly:");
        elderlyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label ratingLabel = new Label("Rating (1-5):");
        ratingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label commentsLabel = new Label("Comments:");
        commentsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Add form elements to grid
        formGrid.add(elderlyLabel, 0, 0);
        formGrid.add(elderlyCombo, 1, 0);
        formGrid.add(ratingLabel, 0, 1);
        formGrid.add(ratingSpinner, 1, 1);
        formGrid.add(commentsLabel, 0, 2);
        formGrid.add(commentsArea, 1, 2);
        
        // Submit button
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().add(submitFeedbackButton);
        
        formSection.getChildren().addAll(formTitle, formGrid, buttonBox);
        
        // Previous Feedback Section
        VBox tableSection = new VBox(15);
        tableSection.setAlignment(Pos.TOP_LEFT);
        
        Label tableTitle = new Label("Previous Feedback");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        tableSection.getChildren().addAll(tableTitle, feedbackTable);
        
        getChildren().addAll(formSection, tableSection);
    }
    
    private void setupEventHandlers() {
        submitFeedbackButton.setOnAction(e -> handleSubmitFeedback());
    }
    
    private void loadElderlyUsers() {
        try {
            List<User> allUsers = userDAO.findAll();
            if (allUsers != null) {
                List<User> elderlyUsers = allUsers.stream()
                    .filter(user -> {
                        if (user.getDateOfBirth() != null) {
                            int age = java.time.LocalDateTime.now().getYear() - user.getDateOfBirth().getYear();
                            return age >= 65;
                        }
                        return false;
                    })
                    .toList();
                
                elderlyCombo.getItems().addAll(elderlyUsers);
                if (!elderlyUsers.isEmpty()) {
                    elderlyCombo.setValue(elderlyUsers.get(0));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading elderly users: " + e.getMessage());
        }
    }
    
    private void loadFeedbackData() {
        feedbackData.clear();
        try {
            // Load actual feedback from database
            List<Feedback> feedbacks = feedbackDAO.findAll();
            if (feedbacks != null) {
                for (Feedback feedback : feedbacks) {
                    // Get elderly user name
                    String elderlyName = "Unknown";
                    try {
                        User elderlyUser = userDAO.findById(feedback.getUserId());
                        if (elderlyUser != null) {
                            elderlyName = elderlyUser.getName();
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting user name: " + e.getMessage());
                    }
                    
                    // Format date
                    String formattedDate = "N/A";
                    if (feedback.getCreatedAt() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
                        formattedDate = feedback.getCreatedAt().format(formatter);
                    }
                    
                    feedbackData.add(new FeedbackTableItem(
                        elderlyName,
                        String.valueOf(feedback.getRating()),
                        feedback.getComment(),
                        formattedDate,
                        String.valueOf(feedback.getId())
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading feedback data: " + e.getMessage());
            // Fallback to sample data
            feedbackData.add(new FeedbackTableItem("Mary Smith", "4", "Very pleasant session", "8/23/2025, 11:43:39 AM", "1"));
            feedbackData.add(new FeedbackTableItem("John Doe", "5", "Excellent experience", "8/22/2025, 2:15:30 PM", "2"));
            feedbackData.add(new FeedbackTableItem("Robert Johnson", "3", "Good but could be better", "8/21/2025, 9:30:15 AM", "3"));
        }
    }
    
    private void handleSubmitFeedback() {
        if (currentUser == null) {
            showAlert(AlertType.ERROR, "Error", "User not logged in");
            return;
        }
        
        User selectedElderly = elderlyCombo.getValue();
        Integer rating = ratingSpinner.getValue();
        String comments = commentsArea.getText().trim();
        
        if (selectedElderly == null || comments.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
            return;
        }
        
        if (rating == null || rating < 1 || rating > 5) {
            showAlert(AlertType.ERROR, "Error", "Please provide a valid rating between 1 and 5");
            return;
        }
        
        try {
            // Create a new feedback
            Feedback feedback = new Feedback();
            feedback.setUserId(selectedElderly.getId());
            feedback.setRating(rating);
            feedback.setComment(comments);
            feedback.setCategory("GENERAL");
            feedback.setStatus("ACTIVE");
            feedback.setCreatedAt(LocalDateTime.now());
            
            boolean saved = feedbackDAO.save(feedback);
            if (saved) {
                showAlert(AlertType.INFORMATION, "Success", "Feedback submitted successfully!");
                resetForm();
                loadFeedbackData();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to submit feedback");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Failed to submit feedback: " + ex.getMessage());
        }
    }
    
    private void handleEditFeedback(FeedbackTableItem feedbackItem) {
        // Create edit dialog
        Dialog<FeedbackTableItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Feedback");
        dialog.setHeaderText("Edit feedback for " + feedbackItem.getElderlyName());
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the custom content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Form fields for editing
        Spinner<Integer> editRatingSpinner = new Spinner<>();
        editRatingSpinner.setPrefWidth(100);
        SpinnerValueFactory<Integer> editValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, Integer.parseInt(feedbackItem.getRating()));
        editRatingSpinner.setValueFactory(editValueFactory);
        editRatingSpinner.setEditable(true);
        
        TextArea editCommentsArea = new TextArea(feedbackItem.getComments());
        editCommentsArea.setPromptText("Enter your feedback comments here...");
        editCommentsArea.setPrefRowCount(4);
        editCommentsArea.setWrapText(true);
        
        grid.add(new Label("Rating (1-5):"), 0, 0);
        grid.add(editRatingSpinner, 1, 0);
        grid.add(new Label("Comments:"), 0, 1);
        grid.add(editCommentsArea, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the rating field by default
        editRatingSpinner.requestFocus();
        
        // Convert the result to a FeedbackTableItem when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Integer newRating = editRatingSpinner.getValue();
                String newComments = editCommentsArea.getText().trim();
                
                if (newComments.isEmpty()) {
                    showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
                    return null;
                }
                
                if (newRating == null || newRating < 1 || newRating > 5) {
                    showAlert(AlertType.ERROR, "Error", "Please provide a valid rating between 1 and 5");
                    return null;
                }
                
                feedbackItem.setRating(String.valueOf(newRating));
                feedbackItem.setComments(newComments);
                
                // Update in database
                try {
                    Feedback feedback = feedbackDAO.findById(Integer.parseInt(feedbackItem.getFeedbackId()));
                    if (feedback != null) {
                        feedback.setRating(newRating);
                        feedback.setComment(newComments);
                        feedbackDAO.update(feedback);
                        showAlert(AlertType.INFORMATION, "Success", "Feedback updated successfully!");
                    }
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Error", "Failed to update feedback: " + e.getMessage());
                }
                
                return feedbackItem;
            }
            return null;
        });
        
        Optional<FeedbackTableItem> result = dialog.showAndWait();
        result.ifPresent(updatedFeedback -> {
            // Refresh the table
            loadFeedbackData();
        });
    }
    
    private void handleDeleteFeedback(FeedbackTableItem feedbackItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Feedback");
        alert.setContentText("Are you sure you want to delete the feedback for " + feedbackItem.getElderlyName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                boolean deleted = feedbackDAO.delete(Integer.parseInt(feedbackItem.getFeedbackId()));
                if (deleted) {
                    showAlert(AlertType.INFORMATION, "Success", "Feedback deleted successfully!");
                    loadFeedbackData();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete feedback");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete feedback: " + e.getMessage());
            }
        }
    }
    
    private void resetForm() {
        elderlyCombo.setValue(null);
        ratingSpinner.getValueFactory().setValue(5);
        commentsArea.clear();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner class for table data
    public static class FeedbackTableItem {
        private String elderlyName;
        private String rating;
        private String comments;
        private String date;
        private String feedbackId; // Store the actual feedback ID for database operations
        
        public FeedbackTableItem(String elderlyName, String rating, String comments, String date, String feedbackId) {
            this.elderlyName = elderlyName;
            this.rating = rating;
            this.comments = comments;
            this.date = date;
            this.feedbackId = feedbackId;
        }
        
        // Getters
        public String getElderlyName() { return elderlyName; }
        public String getRating() { return rating; }
        public String getComments() { return comments; }
        public String getDate() { return date; }
        public String getFeedbackId() { return feedbackId; }
        
        // Setters
        public void setElderlyName(String elderlyName) { this.elderlyName = elderlyName; }
        public void setRating(String rating) { this.rating = rating; }
        public void setComments(String comments) { this.comments = comments; }
        public void setDate(String date) { this.date = date; }
        public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    }
}
