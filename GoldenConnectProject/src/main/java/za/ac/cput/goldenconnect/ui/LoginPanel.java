package za.ac.cput.goldenconnect.ui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import za.ac.cput.goldenconnect.service.AuthService;
import za.ac.cput.goldenconnect.model.User;
import za.ac.cput.goldenconnect.ui.components.GoldenConnectLogo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.function.Consumer;

public class LoginPanel extends HBox {
    
    private AuthService authService;
    private TextField emailField;
    private PasswordField passwordField;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private Button registerButton;
    private Button helpButton;
    
    // Callback functions
    private Consumer<User> onLoginSuccess;
    private Runnable onRegisterRequest;
    
    public LoginPanel(AuthService authService) {
        this.authService = authService;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    public void setOnLoginSuccess(Consumer<User> callback) {
        this.onLoginSuccess = callback;
    }
    
    public void setOnRegisterRequest(Runnable callback) {
        this.onRegisterRequest = callback;
    }
    
    private void initializeComponents() {
        // Email field
        emailField = new TextField();
        emailField.setPromptText("Enter your email or username");
        emailField.setPrefWidth(350);
        emailField.setPrefHeight(45);
        emailField.setStyle("-fx-font-size: 16px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-background-color: white;");
        
        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(350);
        passwordField.setPrefHeight(45);
        passwordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-background-color: white;");
        
        // Remember me checkbox
        rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setStyle("-fx-font-size: 14px; -fx-text-fill: #1F2A37;");
        
        // Show password link
        Button showPasswordButton = new Button("Show password");
        showPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2D5BFF; -fx-font-size: 14px; -fx-underline: true; -fx-border-width: 0;");
        showPasswordButton.setOnAction(e -> togglePasswordVisibility());
        
        // Login button
        loginButton = new Button("Log In");
        loginButton.setPrefWidth(350);
        loginButton.setPrefHeight(50);
        loginButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        

        
        // Register button
        registerButton = new Button("New user? Register here");
        registerButton.setPrefWidth(350);
        registerButton.setPrefHeight(45);
        registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1F2A37; -fx-font-size: 16px; -fx-border-color: #F5B700; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");
        
        // Help button
        helpButton = new Button("Help");
        helpButton.setPrefWidth(80);
        helpButton.setPrefHeight(35);
        helpButton.setStyle("-fx-background-color: #F5F5F5; -fx-text-fill: #1F2A37; -fx-font-size: 14px; -fx-background-radius: 6; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 6; -fx-cursor: hand;");
    }
    
    private void setupLayout() {
        setPrefWidth(1000);
        setPrefHeight(600);
        setStyle("-fx-background-color: white;");
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        
        // Left panel - Branding
        VBox leftPanel = createBrandingPanel();
        
        // Right panel - Login form
        VBox rightPanel = createLoginFormPanel();
        
        // Force panels to fill the entire width
        leftPanel.setPrefWidth(500);
        rightPanel.setPrefWidth(500);
        leftPanel.setMinWidth(500);
        rightPanel.setMinWidth(500);
        leftPanel.setMaxWidth(Double.MAX_VALUE);
        rightPanel.setMaxWidth(Double.MAX_VALUE);
        
        // Set HBox to fill width
        setHgrow(leftPanel, javafx.scene.layout.Priority.ALWAYS);
        setHgrow(rightPanel, javafx.scene.layout.Priority.ALWAYS);
        
        getChildren().addAll(leftPanel, rightPanel);
    }
    
    private VBox createBrandingPanel() {
        VBox leftPanel = new VBox(20);
        leftPanel.setPrefWidth(500);
        leftPanel.setPrefHeight(600);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setStyle("-fx-background-color: #F5B700; -fx-background-radius: 0;"); // Core Gold, no rounded corners
        leftPanel.setMaxWidth(Double.MAX_VALUE);
        leftPanel.setMaxHeight(Double.MAX_VALUE);
        leftPanel.setMinWidth(500);
        
        // Logo
        GoldenConnectLogo logo = new GoldenConnectLogo(150);
        
        // Tagline
        Label tagline = new Label("Bridging Generations\nThrough Technology");
        tagline.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-text-alignment: center; -fx-line-spacing: 5;");
        tagline.setAlignment(Pos.CENTER);
        
        // Custom Icon Illustration - Much bigger to touch panel edges
        ImageView iconView = createCustomIcon(500, 700);
        
        leftPanel.getChildren().addAll(logo, tagline, iconView);
        
        return leftPanel;
    }
    
    private VBox createLoginFormPanel() {
        VBox rightPanel = new VBox(25);
        rightPanel.setPrefWidth(500);
        rightPanel.setPrefHeight(600);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #FFF8EE; -fx-background-radius: 0;"); // Warm Cream, no rounded corners
        rightPanel.setPadding(new Insets(40));
        rightPanel.setMaxWidth(Double.MAX_VALUE);
        rightPanel.setMaxHeight(Double.MAX_VALUE);
        rightPanel.setMinWidth(500);
        
        // Header
        Label header = new Label("Welcome to Golden Connect");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0E2A47;");
        header.setAlignment(Pos.CENTER);
        
        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPrefWidth(400);
        
        // Email field
        Label emailLabel = new Label("Email or username");
        emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1F2A37;");
        
        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1F2A37;");
        
        // Remember me and show password row
        HBox rememberRow = new HBox(20);
        rememberRow.setAlignment(Pos.CENTER_LEFT);
        rememberRow.getChildren().addAll(rememberMeCheckBox);
        
        // Add show password button to the right
        HBox showPasswordRow = new HBox();
        showPasswordRow.setAlignment(Pos.CENTER_RIGHT);
        Button showPasswordButton = new Button("Show password");
        showPasswordButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #2D5BFF; -fx-font-size: 14px; -fx-underline: true; -fx-border-width: 0;");
        showPasswordButton.setOnAction(e -> togglePasswordVisibility());
        showPasswordRow.getChildren().add(showPasswordButton);
        
        HBox optionsRow = new HBox();
        optionsRow.getChildren().addAll(rememberRow, showPasswordRow);
        HBox.setHgrow(rememberRow, javafx.scene.layout.Priority.ALWAYS);
        
        // Login button
        
        // Register section
        VBox registerSection = new VBox(10);
        registerSection.setAlignment(Pos.CENTER);
        
        Label registerLabel = new Label("New user? Register here");
        registerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1F2A37; -fx-font-weight: bold;");
        
        // Help button (positioned at bottom right)
        StackPane helpContainer = new StackPane();
        helpContainer.setAlignment(Pos.BOTTOM_RIGHT);
        helpContainer.getChildren().add(helpButton);
        
        formContainer.getChildren().addAll(
            emailLabel, emailField,
            passwordLabel, passwordField,
            optionsRow,
            loginButton,
            registerSection
        );
        
        registerSection.getChildren().addAll(registerLabel, registerButton);
        
        rightPanel.getChildren().addAll(header, formContainer, helpContainer);
        
        return rightPanel;
    }
    
    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());
        helpButton.setOnAction(e -> showHelp());
        
        // Add hover effects
        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle("-fx-background-color: #1E4BD8; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;")
        );
        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle("-fx-background-color: #2D5BFF; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;")
        );
        
        registerButton.setOnMouseEntered(e -> 
            registerButton.setStyle("-fx-background-color: #FFF0CC; -fx-text-fill: #1F2A37; -fx-font-size: 16px; -fx-border-color: #F5B700; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;")
        );
        registerButton.setOnMouseExited(e -> 
            registerButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #1F2A37; -fx-font-size: 16px; -fx-border-color: #F5B700; -fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;")
        );
    }
    
    private void togglePasswordVisibility() {
        // This would toggle between PasswordField and TextField
        // For now, just show a message
        showAlert(AlertType.INFORMATION, "Password Visibility", "Password visibility toggle will be implemented.");
    }
    
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Please fill in all fields");
            return;
        }
        
        try {
            User user = authService.login(email, password);
            if (user != null) {
                showAlert(AlertType.INFORMATION, "Success", "Login successful!");
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(user);
                }
            } else {
                showAlert(AlertType.ERROR, "Error", "Invalid credentials");
            }
        } catch (Exception ex) {
            showAlert(AlertType.ERROR, "Error", "Login failed: " + ex.getMessage());
        }
    }
    
    private void handleRegister() {
        if (onRegisterRequest != null) {
            onRegisterRequest.run();
        }
    }
    
    private void showHelp() {
        showAlert(AlertType.INFORMATION, "Help", "Need assistance? Contact support at support@goldenconnect.com");
    }
    
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public void clearFields() {
        emailField.clear();
        passwordField.clear();
        rememberMeCheckBox.setSelected(false);
    }
    
    private ImageView createCustomIcon(double width, double height) {
        try {
            // Load the custom icon from resources
            Image iconImage = new Image(getClass().getResourceAsStream("/images/icon.png"));
            ImageView iconView = new ImageView(iconImage);
            
            // Set the size while maintaining aspect ratio
            iconView.setFitWidth(width);
            iconView.setFitHeight(height);
            iconView.setPreserveRatio(true);
            iconView.setSmooth(true);
            
            return iconView;
            
        } catch (Exception e) {
            // Fallback to a simple placeholder if icon loading fails
            System.err.println("Could not load icon image: " + e.getMessage());
            return createFallbackIcon(width, height);
        }
    }
    
    private ImageView createFallbackIcon(double width, double height) {
        // Create a simple fallback icon
        javafx.scene.control.Label fallbackLabel = new javafx.scene.control.Label("🌐");
        fallbackLabel.setStyle("-fx-font-size: " + (width/2) + "px; -fx-text-fill: white;");
        fallbackLabel.setAlignment(Pos.CENTER);
        
        // Convert label to ImageView for consistency
        ImageView fallbackView = new ImageView();
        fallbackView.setFitWidth(width);
        fallbackView.setFitHeight(height);
        
        return fallbackView;
    }
}
