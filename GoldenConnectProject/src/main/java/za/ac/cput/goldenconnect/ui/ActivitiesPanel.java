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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.List;
import java.util.Optional;

import za.ac.cput.goldenconnect.service.ActivityService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.dao.ActivityDAO;
import za.ac.cput.goldenconnect.dao.impl.ActivityDAOImpl;

public class ActivitiesPanel extends VBox {
    
    private ActivityService activityService;
    private User currentUser;
    private ActivityDAO activityDAO;
    
    // Form components
    private TextField searchField;
    private TextField titleField;
    private ComboBox<String> categoryCombo;
    private TextArea descriptionArea;
    private Button addActivityButton;
    
    // Table components
    private TableView<ActivityTableItem> activitiesTable;
    private ObservableList<ActivityTableItem> activitiesData;
    private FilteredList<ActivityTableItem> filteredActivitiesData;
    
    public ActivitiesPanel(ActivityService activityService, User currentUser) {
        this.activityService = activityService;
        this.currentUser = currentUser;
        this.activityDAO = new ActivityDAOImpl();
        this.activitiesData = FXCollections.observableArrayList();
        this.filteredActivitiesData = new FilteredList<>(activitiesData, p -> true);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadActivitiesData();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadActivitiesData();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search activity");
        searchField.setPrefWidth(300);
        
        // Title field
        titleField = new TextField();
        titleField.setPromptText("e.g. Yoga");
        titleField.setPrefWidth(250);
        
        // Category dropdown
        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Wellness", "Technology", "Social", "Education", "Entertainment", "Physical", "Creative", "Outdoor");
        categoryCombo.setValue("Wellness");
        categoryCombo.setPrefWidth(200);
        
        // Description area
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Short description");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setPrefWidth(300);
        descriptionArea.setWrapText(true);
        
        // Add Activity button
        addActivityButton = new Button("Add Activity");
        addActivityButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        
        // Initialize table
        initializeTable();
    }
    
    private void initializeTable() {
        activitiesTable = new TableView<>();
        activitiesTable.setPrefHeight(300);
        activitiesTable.setItems(filteredActivitiesData);
        
        // Create table columns
        TableColumn<ActivityTableItem, String> titleCol = new TableColumn<>("TITLE");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        
        TableColumn<ActivityTableItem, String> categoryCol = new TableColumn<>("CATEGORY");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        TableColumn<ActivityTableItem, String> descriptionCol = new TableColumn<>("DESCRIPTION");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setPrefWidth(300);
        
        // Actions column with Edit and Delete buttons
        TableColumn<ActivityTableItem, Void> actionsCol = new TableColumn<>("ACTIONS");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<ActivityTableItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5);
            
            {
                editButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                deleteButton.setStyle("-fx-background-color: #FF5A4A; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                buttonBox.getChildren().addAll(editButton, deleteButton);
                
                editButton.setOnAction(e -> {
                    ActivityTableItem activity = getTableView().getItems().get(getIndex());
                    handleEditActivity(activity);
                });
                
                deleteButton.setOnAction(e -> {
                    ActivityTableItem activity = getTableView().getItems().get(getIndex());
                    handleDeleteActivity(activity);
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
        
        activitiesTable.getColumns().addAll(titleCol, categoryCol, descriptionCol, actionsCol);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(30);
        
        // Activity Management Section
        VBox managementSection = new VBox(20);
        managementSection.setAlignment(Pos.TOP_LEFT);
        
        Label managementTitle = new Label("Activity Management");
        managementTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getChildren().addAll(new Label("Search:"), searchField);
        
        // Add Activity Form
        VBox formSection = new VBox(15);
        formSection.setAlignment(Pos.TOP_LEFT);
        
        Label formTitle = new Label("Add New Activity");
        formTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1F2A37;");
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(20);
        formGrid.setAlignment(Pos.CENTER_LEFT);
        
        // Form labels
        Label titleLabel = new Label("Title:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Add form elements to grid
        formGrid.add(titleLabel, 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(categoryLabel, 0, 1);
        formGrid.add(categoryCombo, 1, 1);
        formGrid.add(descriptionLabel, 0, 2);
        formGrid.add(descriptionArea, 1, 2);
        
        // Add Activity button
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().add(addActivityButton);
        
        formSection.getChildren().addAll(formTitle, formGrid, buttonBox);
        
        managementSection.getChildren().addAll(managementTitle, searchBox, formSection);
        
        // Existing Activities Section
        VBox tableSection = new VBox(15);
        tableSection.setAlignment(Pos.TOP_LEFT);
        
        Label tableTitle = new Label("Existing Activities");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        tableSection.getChildren().addAll(tableTitle, activitiesTable);
        
        getChildren().addAll(managementSection, tableSection);
    }
    
    private void setupEventHandlers() {
        addActivityButton.setOnAction(e -> handleAddActivity());
        searchField.setOnKeyReleased(e -> handleSearch());
    }
    
    private void loadActivitiesData() {
        activitiesData.clear();
        try {
            // Load actual activities from database
            List<Activity> activities = activityDAO.findAll();
            if (activities != null) {
                for (Activity activity : activities) {
                    activitiesData.add(new ActivityTableItem(
                        activity.getName(),
                        activity.getCategory(),
                        activity.getDescription(),
                        String.valueOf(activity.getId())
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading activities data: " + e.getMessage());
            // Fallback to sample data
            activitiesData.add(new ActivityTableItem("Yoga Session", "Wellness", "Gentle yoga for seniors", "1"));
            activitiesData.add(new ActivityTableItem("Technology Workshop", "Technology", "Learn to use smartphones and tablets", "2"));
            activitiesData.add(new ActivityTableItem("Gardening Club", "Outdoor", "Community gardening activities", "3"));
            activitiesData.add(new ActivityTableItem("Book Club", "Social", "Reading and discussion group", "4"));
            activitiesData.add(new ActivityTableItem("Music Therapy", "Entertainment", "Singing and music appreciation", "5"));
        }
    }
    
    private void handleAddActivity() {
        String title = titleField.getText().trim();
        String category = categoryCombo.getValue();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty() || description.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
            return;
        }
        
        try {
            // Create a new activity
            Activity activity = new Activity();
            activity.setName(title);
            activity.setCategory(category);
            activity.setDescription(description);
            activity.setVolunteerId(currentUser != null ? currentUser.getId() : 1);
            
            boolean saved = activityDAO.save(activity);
            if (saved) {
                showAlert(AlertType.INFORMATION, "Success", "Activity added successfully!");
                resetForm();
                loadActivitiesData();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to add activity");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Failed to add activity: " + ex.getMessage());
        }
    }
    
    private void handleEditActivity(ActivityTableItem activityItem) {
        // Create edit dialog
        Dialog<ActivityTableItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Activity");
        dialog.setHeaderText("Edit activity: " + activityItem.getTitle());
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the custom content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Form fields for editing
        TextField editTitleField = new TextField(activityItem.getTitle());
        editTitleField.setPromptText("e.g. Yoga");
        
        ComboBox<String> editCategoryCombo = new ComboBox<>();
        editCategoryCombo.getItems().addAll("Wellness", "Technology", "Social", "Education", "Entertainment", "Physical", "Creative", "Outdoor");
        editCategoryCombo.setValue(activityItem.getCategory());
        
        TextArea editDescriptionArea = new TextArea(activityItem.getDescription());
        editDescriptionArea.setPromptText("Short description");
        editDescriptionArea.setPrefRowCount(3);
        editDescriptionArea.setWrapText(true);
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(editTitleField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(editCategoryCombo, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(editDescriptionArea, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the title field by default
        editTitleField.requestFocus();
        
        // Convert the result to an ActivityTableItem when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String newTitle = editTitleField.getText().trim();
                String newCategory = editCategoryCombo.getValue();
                String newDescription = editDescriptionArea.getText().trim();
                
                if (newTitle.isEmpty() || newDescription.isEmpty()) {
                    showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
                    return null;
                }
                
                activityItem.setTitle(newTitle);
                activityItem.setCategory(newCategory);
                activityItem.setDescription(newDescription);
                
                                 // Update in database
                 try {
                     Activity activity = activityDAO.findById(Integer.parseInt(activityItem.getActivityId()));
                     if (activity != null) {
                         activity.setName(newTitle);
                         activity.setCategory(newCategory);
                         activity.setDescription(newDescription);
                         activityDAO.update(activity);
                         showAlert(AlertType.INFORMATION, "Success", "Activity updated successfully!");
                     }
                 } catch (Exception e) {
                     showAlert(AlertType.ERROR, "Error", "Failed to update activity: " + e.getMessage());
                 }
                
                return activityItem;
            }
            return null;
        });
        
        Optional<ActivityTableItem> result = dialog.showAndWait();
        result.ifPresent(updatedActivity -> {
            // Refresh the table
            loadActivitiesData();
        });
    }
    
    private void handleDeleteActivity(ActivityTableItem activityItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Activity");
        alert.setContentText("Are you sure you want to delete the activity '" + activityItem.getTitle() + "'?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                boolean deleted = activityDAO.delete(Integer.parseInt(activityItem.getActivityId()));
                if (deleted) {
                    showAlert(AlertType.INFORMATION, "Success", "Activity deleted successfully!");
                    loadActivitiesData();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete activity");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete activity: " + e.getMessage());
            }
        }
    }
    
    private void resetForm() {
        titleField.clear();
        categoryCombo.setValue("Wellness");
        descriptionArea.clear();
    }
    
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        filteredActivitiesData.setPredicate(activity -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true;
            }
            return activity.getTitle().toLowerCase().contains(searchTerm) ||
                   activity.getCategory().toLowerCase().contains(searchTerm) ||
                   activity.getDescription().toLowerCase().contains(searchTerm);
        });
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner class for table data
    public static class ActivityTableItem {
        private String title;
        private String category;
        private String description;
        private String activityId; // Store the actual activity ID for database operations
        
        public ActivityTableItem(String title, String category, String description, String activityId) {
            this.title = title;
            this.category = category;
            this.description = description;
            this.activityId = activityId;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public String getActivityId() { return activityId; }
        
        // Setters
        public void setTitle(String title) { this.title = title; }
        public void setCategory(String category) { this.category = category; }
        public void setDescription(String description) { this.description = description; }
        public void setActivityId(String activityId) { this.activityId = activityId; }
    }
}
