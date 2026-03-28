package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import za.ac.cput.goldenconnect.service.AuthService;
import za.ac.cput.goldenconnect.model.User;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistrationPanel extends VBox {
    
    private AuthService authService;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;
    private Button backButton;
    
    // Callback functions
    private Consumer<User> onRegistrationSuccess;
    private Runnable onBackToLogin;
    
    public RegistrationPanel(AuthService authService) {
        this.authService = authService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void setOnRegistrationSuccess(Consumer<User> callback) {
        this.onRegistrationSuccess = callback;
    }
    
    public void setOnBackToLogin(Runnable callback) {
        this.onBackToLogin = callback;
    }
    
    private void initializeComponents() {
        nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setPrefWidth(300);
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(300);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(300);
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setPrefWidth(300);
        
        registerButton = new Button("Create Account");
        registerButton.setStyle("-fx-background-color: #F5B700; -fx-text-fill: #0E2A47; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 24; -fx-background-radius: 8;");
        registerButton.setPrefWidth(300);
        
        backButton = new Button("Already have an account? Sign In");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2D5BFF; -fx-font-size: 14px; -fx-underline: true;");
        backButton.setPrefWidth(300);
    }
    
    private void setupLayout() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setSpacing(20);
        
        // Create logo section
        VBox logoSection = createLogoSection();
        
        Label subtitleLabel = new Label("Join GoldenConnect today");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1F2A37;");
        
        GridPane formGrid = new GridPane();
        formGrid.setVgap(15);
        formGrid.setHgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37;");
        
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(emailLabel, 0, 1);
        formGrid.add(emailField, 1, 1);
        formGrid.add(passwordLabel, 0, 2);
        formGrid.add(passwordField, 1, 2);
        formGrid.add(confirmPasswordLabel, 0, 3);
        formGrid.add(confirmPasswordField, 1, 3);
        
        getChildren().addAll(logoSection, subtitleLabel, formGrid, registerButton, backButton);
    }
    
    private VBox createLogoSection() {
        VBox logoSection = new VBox(10);
        logoSection.setAlignment(Pos.CENTER);
        logoSection.setPadding(new Insets(0, 0, 20, 0));
        
        try {
            // Load the logo image
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            ImageView logoView = new ImageView(logoImage);
            logoView.setFitWidth(100);
            logoView.setFitHeight(100);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
            
            // Create app name label
            Label appNameLabel = new Label("Golden Connect");
            appNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            
            logoSection.getChildren().addAll(logoView, appNameLabel);
            
        } catch (Exception e) {
            // Fallback to text-only if logo loading fails
            System.err.println("Could not load logo: " + e.getMessage());
            Label appNameLabel = new Label("Golden Connect");
            appNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
            logoSection.getChildren().add(appNameLabel);
        }
        
        return logoSection;
    }
    
    private void setupEventHandlers() {
        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> handleBackToLogin());
    }
    
    private void handleRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all fields");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error", "Passwords do not match");
            return;
        }
        
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setRole("ADMIN"); // Only admins can register
            user.setPasswordHash(password); // This will be hashed in the service
            
            boolean success = authService.register(user);
            if (success) {
                showAlert(AlertType.INFORMATION, "Success", "Account created successfully!");
                if (onRegistrationSuccess != null) {
                    onRegistrationSuccess.accept(user);
                }
            } else {
                showAlert(AlertType.ERROR, "Error", "Registration failed");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Registration failed: " + ex.getMessage());
        }
    }
    
    private void handleBackToLogin() {
        if (onBackToLogin != null) {
            onBackToLogin.run();
        }
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
