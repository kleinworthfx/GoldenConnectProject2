public class LoginScreen extends VBox {
    public LoginScreen() {
        this.setSpacing(20);
        this.setPadding(new Insets(40));
        this.setStyle("-fx-background-color: #F9F9F9;");
        ImageView logo = new ImageView("logo.png");
        logo.setFitWidth(200);
        logo.setFitHeight(100);

        Label welcomeLabel = new Label("Welcome Back! Please Sign In");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Arial';");

        TextField usernameField = createAccessibleTextField("Username");
        PasswordField passwordField = createAccessibleTextField("Password");

        Button loginButton = new Button("Sign In");
        loginButton.setStyle("""
            -fx-background-color: #2E5077;
            -fx-text-fill: white;
            -fx-font-size: 18px;
            -fx-padding: 10 20 10 20;
            -fx-min-width: 200px;
        """);

        getChildren().addAll(logo, welcomeLabel, usernameField, 
                           passwordField, loginButton);
    }

    private TextField createAccessibleTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("""
            -fx-font-size: 16px;
            -fx-padding: 10;
            -fx-background-radius: 5;
            -fx-min-width: 300px;
        """);
        return field;
    }
}