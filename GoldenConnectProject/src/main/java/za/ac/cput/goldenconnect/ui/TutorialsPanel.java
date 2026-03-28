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
import javafx.scene.control.TableCell;
import javafx.scene.control.Hyperlink;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import za.ac.cput.goldenconnect.service.VideoTutorialService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.VideoTutorial;
import za.ac.cput.goldenconnect.dao.VideoTutorialDAO;
import za.ac.cput.goldenconnect.dao.impl.VideoTutorialDAOImpl;

public class TutorialsPanel extends VBox {
    
    private VideoTutorialService videoTutorialService;
    private User currentUser;
    private VideoTutorialDAO videoTutorialDAO;
    
    // Form components
    private TextField titleField;
    private ComboBox<String> categoryCombo;
    private TextField linkField;
    private Button addTutorialButton;
    
    // Table components
    private TableView<TutorialTableItem> tutorialsTable;
    private ObservableList<TutorialTableItem> tutorialsData;
    private FilteredList<TutorialTableItem> filteredTutorialsData;
    
    public TutorialsPanel(VideoTutorialService videoTutorialService, User currentUser) {
        this.videoTutorialService = videoTutorialService;
        this.currentUser = currentUser;
        this.videoTutorialDAO = new VideoTutorialDAOImpl();
        this.tutorialsData = FXCollections.observableArrayList();
        this.filteredTutorialsData = new FilteredList<>(tutorialsData, p -> true);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadTutorialsData();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadTutorialsData();
    }
    
    private void initializeComponents() {
        // Title field
        titleField = new TextField();
        titleField.setPromptText("Enter tutorial title");
        titleField.setPrefWidth(300);
        
        // Category combo
        categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Training", "Technology", "Social Media", "Banking", "Communication", "Entertainment", "Health", "Safety");
        categoryCombo.setPromptText("Select category");
        categoryCombo.setPrefWidth(200);
        
        // Link field
        linkField = new TextField();
        linkField.setPromptText("YouTube URL or file path");
        linkField.setPrefWidth(400);
        
        // Add Tutorial button
        addTutorialButton = new Button("Add Tutorial");
        addTutorialButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        
        // Initialize table
        initializeTable();
    }
    
    private void initializeTable() {
        tutorialsTable = new TableView<>();
        tutorialsTable.setPrefHeight(300);
        tutorialsTable.setItems(filteredTutorialsData);
        
        // Create table columns
        TableColumn<TutorialTableItem, String> titleCol = new TableColumn<>("TITLE");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        
        TableColumn<TutorialTableItem, String> categoryCol = new TableColumn<>("CATEGORY");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        TableColumn<TutorialTableItem, String> linkCol = new TableColumn<>("LINK");
        linkCol.setCellValueFactory(new PropertyValueFactory<>("link"));
        linkCol.setPrefWidth(150);
        linkCol.setCellFactory(param -> new TableCell<TutorialTableItem, String>() {
            private final Hyperlink link = new Hyperlink("Open");
            
            {
                link.setStyle("-fx-text-fill: #2D5BFF; -fx-underline: true;");
                link.setOnAction(e -> {
                    TutorialTableItem tutorial = getTableView().getItems().get(getIndex());
                    handleOpenLink(tutorial);
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(link);
                }
            }
        });
        
        // Actions column with Edit and Delete buttons
        TableColumn<TutorialTableItem, Void> actionsCol = new TableColumn<>("ACTIONS");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<TutorialTableItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5);
            
            {
                editButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                deleteButton.setStyle("-fx-background-color: #FF5A4A; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10; -fx-background-radius: 4;");
                buttonBox.getChildren().addAll(editButton, deleteButton);
                
                editButton.setOnAction(e -> {
                    TutorialTableItem tutorial = getTableView().getItems().get(getIndex());
                    handleEditTutorial(tutorial);
                });
                
                deleteButton.setOnAction(e -> {
                    TutorialTableItem tutorial = getTableView().getItems().get(getIndex());
                    handleDeleteTutorial(tutorial);
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
        
        tutorialsTable.getColumns().addAll(titleCol, categoryCol, linkCol, actionsCol);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(30);
        
        // Tutorials Form Section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.TOP_LEFT);
        
        Label formTitle = new Label("Tutorials");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
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
        
        Label linkLabel = new Label("Link (YouTube/File):");
        linkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Add form elements to grid
        formGrid.add(titleLabel, 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(categoryLabel, 0, 1);
        formGrid.add(categoryCombo, 1, 1);
        formGrid.add(linkLabel, 0, 2);
        formGrid.add(linkField, 1, 2);
        
        // Add Tutorial button
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().add(addTutorialButton);
        
        formSection.getChildren().addAll(formTitle, formGrid, buttonBox);
        
        // Existing Tutorials Section
        VBox tableSection = new VBox(15);
        tableSection.setAlignment(Pos.TOP_LEFT);
        
        Label tableTitle = new Label("Existing Tutorials");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        tableSection.getChildren().addAll(tableTitle, tutorialsTable);
        
        getChildren().addAll(formSection, tableSection);
    }
    
    private void setupEventHandlers() {
        addTutorialButton.setOnAction(e -> handleAddTutorial());
    }
    
    private void loadTutorialsData() {
        tutorialsData.clear();
        try {
            // Load actual tutorials from database
            List<VideoTutorial> tutorials = videoTutorialDAO.findAll();
            if (tutorials != null) {
                for (VideoTutorial tutorial : tutorials) {
                    tutorialsData.add(new TutorialTableItem(
                        tutorial.getTitle(),
                        tutorial.getCategory(),
                        tutorial.getVideoUrl(),
                        String.valueOf(tutorial.getId())
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tutorials data: " + e.getMessage());
            // Fallback to sample data
            tutorialsData.add(new TutorialTableItem("Session Guide", "Training", "https://youtube.com/watch?v=example", "1"));
        }
    }
    
    private void handleAddTutorial() {
        if (currentUser == null) {
            showAlert(AlertType.ERROR, "Error", "User not logged in");
            return;
        }
        
        String title = titleField.getText().trim();
        String category = categoryCombo.getValue();
        String link = linkField.getText().trim();
        
        if (title.isEmpty() || category == null || link.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
            return;
        }
        
        try {
            // Create a new tutorial
            VideoTutorial tutorial = new VideoTutorial();
            tutorial.setTitle(title);
            tutorial.setCategory(category);
            tutorial.setVideoUrl(link);
            tutorial.setDescription("Tutorial for " + title);
            tutorial.setDifficulty("BEGINNER");
            tutorial.setLanguage("English");
            tutorial.setInstructor("System");
            tutorial.setInstructorId(currentUser.getId());
            tutorial.setStatus("PUBLISHED");
            tutorial.setTargetAudience("ALL");
            tutorial.setCreatedAt(LocalDateTime.now());
            tutorial.setUpdatedAt(LocalDateTime.now());
            
            boolean saved = videoTutorialDAO.save(tutorial);
            if (saved) {
                showAlert(AlertType.INFORMATION, "Success", "Tutorial added successfully!");
                resetForm();
                loadTutorialsData();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to add tutorial");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Failed to add tutorial: " + ex.getMessage());
        }
    }
    
    private void handleEditTutorial(TutorialTableItem tutorialItem) {
        // Create edit dialog
        Dialog<TutorialTableItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Tutorial");
        dialog.setHeaderText("Edit tutorial: " + tutorialItem.getTitle());
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the custom content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Form fields for editing
        TextField editTitleField = new TextField(tutorialItem.getTitle());
        editTitleField.setPromptText("Enter tutorial title");
        
        ComboBox<String> editCategoryCombo = new ComboBox<>();
        editCategoryCombo.getItems().addAll("Training", "Technology", "Social Media", "Banking", "Communication", "Entertainment", "Health", "Safety");
        editCategoryCombo.setValue(tutorialItem.getCategory());
        
        TextField editLinkField = new TextField(tutorialItem.getLink());
        editLinkField.setPromptText("YouTube URL or file path");
        
        grid.add(new Label("Title:"), 0, 0);
        grid.add(editTitleField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(editCategoryCombo, 1, 1);
        grid.add(new Label("Link:"), 0, 2);
        grid.add(editLinkField, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the title field by default
        editTitleField.requestFocus();
        
        // Convert the result to a TutorialTableItem when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String newTitle = editTitleField.getText().trim();
                String newCategory = editCategoryCombo.getValue();
                String newLink = editLinkField.getText().trim();
                
                if (newTitle.isEmpty() || newCategory == null || newLink.isEmpty()) {
                    showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
                    return null;
                }
                
                tutorialItem.setTitle(newTitle);
                tutorialItem.setCategory(newCategory);
                tutorialItem.setLink(newLink);
                
                // Update in database
                try {
                    VideoTutorial tutorial = videoTutorialDAO.findById(Integer.parseInt(tutorialItem.getTutorialId()));
                    if (tutorial != null) {
                        tutorial.setTitle(newTitle);
                        tutorial.setCategory(newCategory);
                        tutorial.setVideoUrl(newLink);
                        tutorial.setUpdatedAt(LocalDateTime.now());
                        videoTutorialDAO.update(tutorial);
                        showAlert(AlertType.INFORMATION, "Success", "Tutorial updated successfully!");
                    }
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Error", "Failed to update tutorial: " + e.getMessage());
                }
                
                return tutorialItem;
            }
            return null;
        });
        
        Optional<TutorialTableItem> result = dialog.showAndWait();
        result.ifPresent(updatedTutorial -> {
            // Refresh the table
            loadTutorialsData();
        });
    }
    
    private void handleDeleteTutorial(TutorialTableItem tutorialItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Tutorial");
        alert.setContentText("Are you sure you want to delete the tutorial '" + tutorialItem.getTitle() + "'?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                boolean deleted = videoTutorialDAO.delete(Integer.parseInt(tutorialItem.getTutorialId()));
                if (deleted) {
                    showAlert(AlertType.INFORMATION, "Success", "Tutorial deleted successfully!");
                    loadTutorialsData();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete tutorial");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete tutorial: " + e.getMessage());
            }
        }
    }
    
    private void handleOpenLink(TutorialTableItem tutorialItem) {
        try {
            // Open the link in default browser
            java.awt.Desktop.getDesktop().browse(new java.net.URI(tutorialItem.getLink()));
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Error", "Could not open link: " + e.getMessage());
        }
    }
    
    private void resetForm() {
        titleField.clear();
        categoryCombo.setValue(null);
        linkField.clear();
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Inner class for table data
    public static class TutorialTableItem {
        private String title;
        private String category;
        private String link;
        private String tutorialId; // Store the actual tutorial ID for database operations
        
        public TutorialTableItem(String title, String category, String link, String tutorialId) {
            this.title = title;
            this.category = category;
            this.link = link;
            this.tutorialId = tutorialId;
        }
        
        // Getters
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public String getLink() { return link; }
        public String getTutorialId() { return tutorialId; }
        
        // Setters
        public void setTitle(String title) { this.title = title; }
        public void setCategory(String category) { this.category = category; }
        public void setLink(String link) { this.link = link; }
        public void setTutorialId(String tutorialId) { this.tutorialId = tutorialId; }
    }
}
