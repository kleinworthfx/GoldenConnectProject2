package za.ac.cput.goldenconnect.ui.components;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Rotate;

public class IntergenerationalIllustration extends Group {
    
    public IntergenerationalIllustration(double width, double height) {
        createIllustration(width, height);
    }
    
    public IntergenerationalIllustration() {
        this(200.0, 150.0); // Default size
    }
    
    private void createIllustration(double width, double height) {
        // Create the heart shape (simplified)
        Ellipse heart = new Ellipse(width * 0.5, height * 0.6, width * 0.3, height * 0.25);
        heart.setFill(Color.web("#FFE6E6")); // Light pink
        heart.setStroke(Color.web("#FF9999"));
        heart.setStrokeWidth(2);
        
        // Elderly person (left side)
        Group elderlyPerson = createPerson(width * 0.25, height * 0.7, true);
        
        // Younger person (right side)
        Group youngerPerson = createPerson(width * 0.75, height * 0.7, false);
        
        // Connection lines
        Rectangle connectionLine1 = new Rectangle(width * 0.35, height * 0.65, width * 0.1, 3);
        connectionLine1.setFill(Color.web("#2D5BFF")); // Cobalt Blue
        
        Rectangle connectionLine2 = new Rectangle(width * 0.55, height * 0.65, width * 0.1, 3);
        connectionLine2.setFill(Color.web("#2D5BFF")); // Cobalt Blue
        
        getChildren().addAll(heart, elderlyPerson, youngerPerson, connectionLine1, connectionLine2);
    }
    
    private Group createPerson(double x, double y, boolean isElderly) {
        Group person = new Group();
        
        // Head
        Circle head = new Circle(x, y - 30, 15);
        head.setFill(Color.web("#FFE5D4")); // Skin tone
        head.setStroke(Color.web("#2D5BFF"));
        head.setStrokeWidth(2);
        
        // Body
        Rectangle body = new Rectangle(x - 12, y - 15, 24, 35);
        body.setFill(Color.web("#2D5BFF")); // Blue shirt
        body.setStroke(Color.web("#0E2A47"));
        body.setStrokeWidth(1);
        body.setArcWidth(8);
        body.setArcHeight(8);
        
        // Arms
        Rectangle leftArm = new Rectangle(x - 20, y - 10, 8, 25);
        leftArm.setFill(Color.web("#2D5BFF"));
        leftArm.setStroke(Color.web("#0E2A47"));
        leftArm.setStrokeWidth(1);
        leftArm.setArcWidth(4);
        leftArm.setArcHeight(4);
        
        Rectangle rightArm = new Rectangle(x + 12, y - 10, 8, 25);
        rightArm.setFill(Color.web("#2D5BFF"));
        rightArm.setStroke(Color.web("#0E2A47"));
        rightArm.setStrokeWidth(1);
        rightArm.setArcWidth(4);
        rightArm.setArcHeight(4);
        
        // Hair
        if (isElderly) {
            // White hair for elderly
            Circle hair = new Circle(x, y - 35, 18);
            hair.setFill(Color.web("#F5F5F5")); // White
            hair.setStroke(Color.web("#E0E0E0"));
            hair.setStrokeWidth(1);
            person.getChildren().add(hair);
        } else {
            // Dark hair for younger person
            Circle hair = new Circle(x, y - 35, 18);
            hair.setFill(Color.web("#2D2D2D")); // Dark
            hair.setStroke(Color.web("#1A1A1A"));
            hair.setStrokeWidth(1);
            person.getChildren().add(hair);
        }
        
        // Eyes
        Circle leftEye = new Circle(x - 5, y - 32, 2);
        leftEye.setFill(Color.web("#2D2D2D"));
        
        Circle rightEye = new Circle(x + 5, y - 32, 2);
        rightEye.setFill(Color.web("#2D2D2D"));
        
        // Smile
        Ellipse smile = new Ellipse(x, y - 25, 6, 3);
        smile.setFill(Color.TRANSPARENT);
        smile.setStroke(Color.web("#2D2D2D"));
        smile.setStrokeWidth(1);
        
        person.getChildren().addAll(head, body, leftArm, rightArm, leftEye, rightEye, smile);
        
        return person;
    }
}
