package za.ac.cput.goldenconnect.ui.components;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class GoldenConnectLogo extends Group {
    
    private ImageView logoView;
    
    public GoldenConnectLogo(double size) {
        createLogo(size);
    }
    
    public GoldenConnectLogo() {
        this(150.0); // Default size
    }
    
    private void createLogo(double size) {
        try {
            // Load the custom logo from resources
            Image logoImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoView = new ImageView(logoImage);
            
            // Set the size while maintaining aspect ratio
            logoView.setFitWidth(size);
            logoView.setFitHeight(size);
            logoView.setPreserveRatio(true);
            logoView.setSmooth(true);
            
            // Center the logo
            logoView.setTranslateX(-size/2);
            logoView.setTranslateY(-size/2);
            
            getChildren().add(logoView);
            
        } catch (Exception e) {
            // Fallback to a simple text logo if image loading fails
            System.err.println("Could not load logo image: " + e.getMessage());
            createFallbackLogo(size);
        }
    }
    
    private void createFallbackLogo(double size) {
        // Create a simple fallback logo
        javafx.scene.control.Label fallbackLabel = new javafx.scene.control.Label("GC");
        fallbackLabel.setStyle("-fx-font-size: " + (size/2) + "px; -fx-font-weight: bold; -fx-text-fill: white;");
        fallbackLabel.setTranslateX(-size/4);
        fallbackLabel.setTranslateY(-size/4);
        getChildren().add(fallbackLabel);
    }
    
    public void setColor(Color color) {
        // This method is kept for compatibility but may not work with image logos
        if (logoView != null) {
            // Could apply color effects to the image if needed
        }
    }
    
    public void setGradient(Color color1, Color color2) {
        // This method is kept for compatibility but may not work with image logos
        if (logoView != null) {
            // Could apply gradient effects to the image if needed
        }
    }
}
