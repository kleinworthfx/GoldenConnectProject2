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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.Hyperlink;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import za.ac.cput.goldenconnect.service.ActivityService;
import za.ac.cput.goldenconnect.service.RequestService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.model.Activity;
import za.ac.cput.goldenconnect.model.Request;
import za.ac.cput.goldenconnect.dao.UserDAO;
import za.ac.cput.goldenconnect.dao.RequestDAO;
import za.ac.cput.goldenconnect.dao.impl.UserDAOImpl;
import za.ac.cput.goldenconnect.dao.impl.RequestDAOImpl;

public class SessionBookingPanel extends VBox {
    
    private ActivityService activityService;
    private RequestService requestService;
    private User currentUser;
    private UserDAO userDAO;
    private RequestDAO requestDAO;
    
    // Form components
    private TextField searchField;
    private ComboBox<User> elderlyCombo;
    private DatePicker datePicker;
    private TextField timeField;
    private ComboBox<String> activityCombo;
    private ComboBox<String> statusCombo;
    private Button createBookingButton;
    private Hyperlink resetLink;
    
    // Table components
    private TableView<BookingTableItem> bookingsTable;
    private ObservableList<BookingTableItem> bookingsData;
    private FilteredList<BookingTableItem> filteredBookingsData;
    
    // Current editing booking
    private BookingTableItem editingBooking;
    
    public SessionBookingPanel(ActivityService activityService, RequestService requestService, User currentUser) {
        this.activityService = activityService;
        this.requestService = requestService;
        this.currentUser = currentUser;
        this.userDAO = new UserDAOImpl();
        this.requestDAO = new RequestDAOImpl();
        this.bookingsData = FXCollections.observableArrayList();
        this.filteredBookingsData = new FilteredList<>(bookingsData, p -> true);
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadBookingsData();
    }
    
    public void updateUser(User user) {
        this.currentUser = user;
        loadBookingsData();
    }
    
    private void initializeComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search by name or activity");
        searchField.setPrefWidth(300);
        
        // Elderly dropdown
        elderlyCombo = new ComboBox<>();
        elderlyCombo.setPrefWidth(200);
        loadElderlyUsers();
        
        // Date picker
        datePicker = new DatePicker();
        datePicker.setPrefWidth(200);
        
        // Time field
        timeField = new TextField();
        timeField.setPromptText("HH:MM AM/PM");
        timeField.setPrefWidth(100);
        
        // Activity dropdown
        activityCombo = new ComboBox<>();
        activityCombo.getItems().addAll("Yoga", "Walk", "Reading", "Music", "Cooking", "Gardening", "Technology Help", "Companionship");
        activityCombo.setValue("Yoga");
        activityCombo.setPrefWidth(200);
        
        // Status dropdown
        statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Scheduled", "Completed", "Cancelled", "Pending");
        statusCombo.setValue("Scheduled");
        statusCombo.setPrefWidth(200);
        
        // Buttons
        createBookingButton = new Button("Create Booking");
        createBookingButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        
        resetLink = new Hyperlink("Reset");
        resetLink.setStyle("-fx-text-fill: #2D5BFF; -fx-font-size: 14px;");
        
        // Initialize table
        initializeTable();
    }
    
    private void initializeTable() {
        bookingsTable = new TableView<>();
        bookingsTable.setPrefHeight(300);
        bookingsTable.setItems(filteredBookingsData);
        
        // Create table columns
        TableColumn<BookingTableItem, String> nameCol = new TableColumn<>("NAME");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);
        
        TableColumn<BookingTableItem, String> activityCol = new TableColumn<>("ACTIVITY");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activity"));
        activityCol.setPrefWidth(150);
        
        TableColumn<BookingTableItem, String> dateCol = new TableColumn<>("DATE");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(200);
        
        TableColumn<BookingTableItem, String> statusCol = new TableColumn<>("STATUS");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);
        
        // Actions column with Edit and Delete buttons
        TableColumn<BookingTableItem, Void> actionsCol = new TableColumn<>("ACTIONS");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<BookingTableItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(5);
            
            {
                editButton.setStyle("-fx-background-color: #2BAE66; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                deleteButton.setStyle("-fx-background-color: #FF5A4A; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                buttonBox.getChildren().addAll(editButton, deleteButton);
                
                editButton.setOnAction(e -> {
                    BookingTableItem booking = getTableView().getItems().get(getIndex());
                    handleEditBooking(booking);
                });
                
                deleteButton.setOnAction(e -> {
                    BookingTableItem booking = getTableView().getItems().get(getIndex());
                    handleDeleteBooking(booking);
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
        
        bookingsTable.getColumns().addAll(nameCol, activityCol, dateCol, statusCol, actionsCol);
    }
    
    private void setupLayout() {
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(30));
        setSpacing(30);
        
        // Session Booking Form Section
        VBox formSection = new VBox(20);
        formSection.setAlignment(Pos.TOP_LEFT);
        
        Label formTitle = new Label("Session Booking");
        formTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.getChildren().addAll(new Label("Search:"), searchField);
        
        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(20);
        formGrid.setAlignment(Pos.CENTER_LEFT);
        
        // Left column
        Label elderlyLabel = new Label("Elderly:");
        elderlyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label dateTimeLabel = new Label("Date & Time:");
        dateTimeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Right column
        Label activityLabel = new Label("Activity:");
        activityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Date and time row
        HBox dateTimeBox = new HBox(10);
        dateTimeBox.getChildren().addAll(datePicker, timeField);
        
        formGrid.add(elderlyLabel, 0, 0);
        formGrid.add(elderlyCombo, 1, 0);
        formGrid.add(dateTimeLabel, 0, 1);
        formGrid.add(dateTimeBox, 1, 1);
        formGrid.add(activityLabel, 2, 0);
        formGrid.add(activityCombo, 3, 0);
        formGrid.add(statusLabel, 2, 1);
        formGrid.add(statusCombo, 3, 1);
        
        // Action buttons
        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        actionBox.getChildren().addAll(createBookingButton, resetLink);
        
        formSection.getChildren().addAll(formTitle, searchBox, formGrid, actionBox);
        
        // Previous Bookings Section
        VBox tableSection = new VBox(15);
        tableSection.setAlignment(Pos.TOP_LEFT);
        
        Label tableTitle = new Label("Previous Bookings");
        tableTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        
        tableSection.getChildren().addAll(tableTitle, bookingsTable);
        
        getChildren().addAll(formSection, tableSection);
    }
    
    private void setupEventHandlers() {
        createBookingButton.setOnAction(e -> handleCreateBooking());
        resetLink.setOnAction(e -> resetForm());
        searchField.setOnKeyReleased(e -> handleSearch());
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
    
    private void loadBookingsData() {
        bookingsData.clear();
        try {
            // Load actual bookings from database
            List<Request> requests = requestDAO.findAll();
            if (requests != null) {
                for (Request request : requests) {
                    // Get elderly user name
                    String elderlyName = "Unknown";
                    try {
                        User elderlyUser = userDAO.findById(request.getRequesterId());
                        if (elderlyUser != null) {
                            elderlyName = elderlyUser.getName();
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting user name: " + e.getMessage());
                    }
                    
                    // Format date
                    String formattedDate = "N/A";
                    if (request.getCreatedAt() != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
                        formattedDate = request.getCreatedAt().format(formatter);
                    }
                    
                    bookingsData.add(new BookingTableItem(
                        elderlyName,
                        request.getRequestType(),
                        formattedDate,
                        request.getStatus(),
                        String.valueOf(request.getId())
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading bookings data: " + e.getMessage());
            // Fallback to sample data
            bookingsData.add(new BookingTableItem("John Doe", "Walk", "8/24/2025, 6:36:13 PM", "Scheduled", "1"));
            bookingsData.add(new BookingTableItem("Mary Smith", "Yoga", "8/25/2025, 10:00:00 AM", "Scheduled", "2"));
            bookingsData.add(new BookingTableItem("Robert Johnson", "Reading", "8/23/2025, 2:00:00 PM", "Completed", "3"));
        }
    }
    
    private void handleCreateBooking() {
        if (currentUser == null) {
            showAlert(AlertType.ERROR, "Error", "User not logged in");
            return;
        }
        
        User selectedElderly = elderlyCombo.getValue();
        String activity = activityCombo.getValue();
        String status = statusCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();
        String timeText = timeField.getText();
        
        if (selectedElderly == null || activity == null || status == null) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all required fields");
            return;
        }
        
        try {
            // Create a new booking/request
            Request request = new Request();
            request.setRequesterId(currentUser.getId());
            request.setRequestType(activity.toUpperCase());
            request.setDescription("Session booking for " + activity + " with " + selectedElderly.getName());
            request.setStatus(status.toUpperCase());
            
            // Set date and time if provided
            if (selectedDate != null && !timeText.isEmpty()) {
                try {
                    LocalTime time = parseTime(timeText);
                    LocalDateTime dateTime = selectedDate.atTime(time);
                    request.setCreatedAt(dateTime);
                } catch (Exception e) {
                    System.err.println("Error parsing date/time: " + e.getMessage());
                }
            }
            
            Request createdRequest = requestService.createRequest(request);
            if (createdRequest != null && createdRequest.getId() > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Booking created successfully!");
                resetForm();
                loadBookingsData();
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to create booking");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Failed to create booking: " + ex.getMessage());
        }
    }
    
    private void handleEditBooking(BookingTableItem booking) {
        editingBooking = booking;
        
        // Create edit dialog
        Dialog<BookingTableItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Booking");
        dialog.setHeaderText("Edit booking for " + booking.getName());
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the custom content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Form fields for editing
        ComboBox<String> editActivityCombo = new ComboBox<>();
        editActivityCombo.getItems().addAll("Yoga", "Walk", "Reading", "Music", "Cooking", "Gardening", "Technology Help", "Companionship");
        editActivityCombo.setValue(booking.getActivity());
        
        ComboBox<String> editStatusCombo = new ComboBox<>();
        editStatusCombo.getItems().addAll("Scheduled", "Completed", "Cancelled", "Pending");
        editStatusCombo.setValue(booking.getStatus());
        
        DatePicker editDatePicker = new DatePicker();
        TextField editTimeField = new TextField();
        editTimeField.setPromptText("HH:MM AM/PM");
        
        // Try to parse existing date
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
            LocalDateTime dateTime = LocalDateTime.parse(booking.getDate(), formatter);
            editDatePicker.setValue(dateTime.toLocalDate());
            editTimeField.setText(dateTime.format(DateTimeFormatter.ofPattern("h:mm a")));
        } catch (Exception e) {
            // If parsing fails, leave empty
        }
        
        grid.add(new Label("Activity:"), 0, 0);
        grid.add(editActivityCombo, 1, 0);
        grid.add(new Label("Status:"), 0, 1);
        grid.add(editStatusCombo, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(editDatePicker, 1, 2);
        grid.add(new Label("Time:"), 0, 3);
        grid.add(editTimeField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the activity field by default
        editActivityCombo.requestFocus();
        
        // Convert the result to a BookingTableItem when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                booking.setActivity(editActivityCombo.getValue());
                booking.setStatus(editStatusCombo.getValue());
                
                // Update date and time
                if (editDatePicker.getValue() != null && !editTimeField.getText().isEmpty()) {
                    try {
                        LocalTime time = parseTime(editTimeField.getText());
                        LocalDateTime dateTime = editDatePicker.getValue().atTime(time);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy, h:mm:ss a");
                        booking.setDate(dateTime.format(formatter));
                    } catch (Exception e) {
                        System.err.println("Error parsing date/time: " + e.getMessage());
                    }
                }
                
                // Update in database
                try {
                    Request request = requestDAO.findById(Integer.parseInt(booking.getRequestId()));
                    if (request != null) {
                        request.setRequestType(editActivityCombo.getValue().toUpperCase());
                        request.setStatus(editStatusCombo.getValue().toUpperCase());
                        requestDAO.update(request);
                        showAlert(AlertType.INFORMATION, "Success", "Booking updated successfully!");
                    }
                } catch (Exception e) {
                    showAlert(AlertType.ERROR, "Error", "Failed to update booking: " + e.getMessage());
                }
                
                return booking;
            }
            return null;
        });
        
        Optional<BookingTableItem> result = dialog.showAndWait();
        result.ifPresent(updatedBooking -> {
            // Refresh the table
            loadBookingsData();
        });
    }
    
    private void handleDeleteBooking(BookingTableItem booking) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Booking");
        alert.setContentText("Are you sure you want to delete the booking for " + booking.getName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                boolean deleted = requestDAO.delete(Integer.parseInt(booking.getRequestId()));
                if (deleted) {
                    showAlert(AlertType.INFORMATION, "Success", "Booking deleted successfully!");
                    loadBookingsData();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to delete booking");
                }
            } catch (Exception e) {
                showAlert(AlertType.ERROR, "Error", "Failed to delete booking: " + e.getMessage());
            }
        }
    }
    
    private LocalTime parseTime(String timeText) {
        // Parse time in format "HH:MM AM/PM" or "HH:MM"
        timeText = timeText.trim();
        try {
            if (timeText.toLowerCase().contains("am") || timeText.toLowerCase().contains("pm")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                return LocalTime.parse(timeText, formatter);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                return LocalTime.parse(timeText, formatter);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:MM AM/PM or HH:MM");
        }
    }
    
    private void resetForm() {
        elderlyCombo.setValue(null);
        datePicker.setValue(null);
        timeField.clear();
        activityCombo.setValue("Yoga");
        statusCombo.setValue("Scheduled");
    }
    
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        filteredBookingsData.setPredicate(booking -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true;
            }
            return booking.getName().toLowerCase().contains(searchTerm) ||
                   booking.getActivity().toLowerCase().contains(searchTerm) ||
                   booking.getStatus().toLowerCase().contains(searchTerm);
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
    public static class BookingTableItem {
        private String name;
        private String activity;
        private String date;
        private String status;
        private String requestId; // Store the actual request ID for database operations
        
        public BookingTableItem(String name, String activity, String date, String status, String requestId) {
            this.name = name;
            this.activity = activity;
            this.date = date;
            this.status = status;
            this.requestId = requestId;
        }
        
        // Getters
        public String getName() { return name; }
        public String getActivity() { return activity; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
        public String getRequestId() { return requestId; }
        
        // Setters
        public void setName(String name) { this.name = name; }
        public void setActivity(String activity) { this.activity = activity; }
        public void setDate(String date) { this.date = date; }
        public void setStatus(String status) { this.status = status; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
    }
}
