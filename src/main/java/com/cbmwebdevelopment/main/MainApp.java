package com.cbmwebdevelopment.main;

import com.cbmwebdevelopment.menus.CustomMenuBar;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static String USER_ID;
    public static boolean IS_SIGNED_IN;
    private Group group;
    private final String OS = System.getProperty("os.name").toLowerCase();
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
    public static final ObservableList<String> STATES = FXCollections.observableArrayList("Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming");
    public static final ObservableList<String> PREFIXES = FXCollections.observableArrayList("-", "Mr.", "Mrs.", "Ms.", "Dr.", "Hon.");
    public static final ObservableList<String> SUFFIXES = FXCollections.observableArrayList("-", "Jr.", "II", "III", "IV", "CPA", "DDS", "Esq", "JD", "LLD", "MD", "PhD", "Ret", "RN", "Sr", "DO");

    public static final String ERROR_LABEL = "-fx-text-fill:#ff0000";

    @Override
    public void start(Stage stage) throws Exception {
        if (IS_SIGNED_IN) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainFXML.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            MainFXMLController controller = (MainFXMLController) loader.getController();
            group = new Group();
            group.setLayoutX(80);
            group.setLayoutY(0);
            controller.group = group;

            anchorPane.getChildren().add(group);

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            int width = (int) primaryScreenBounds.getWidth() - 150;
            int height = (int) primaryScreenBounds.getHeight() - 200;

            CustomMenuBar customMenuBar = new CustomMenuBar();
            customMenuBar.group = group;
            MenuBar menuBar = customMenuBar.menuBar();
            anchorPane.getChildren().add(0, menuBar);
            menuBar.toFront();
            if (OS.contains("mac")) {
                menuBar.setUseSystemMenuBar(true);
            } else {
                AnchorPane.setLeftAnchor(menuBar, 0.0);
                AnchorPane.setRightAnchor(menuBar, 0.0);
                AnchorPane.setTopAnchor(controller.navigationAnchorPane, 25.00);
            }

            Scene scene = new Scene(anchorPane, 1250, height);
            scene.setFill(Color.DARKGRAY);
            stage.setTitle("Auctioneer");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest((value) -> {
                Platform.exit();
            });
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/UserSignInFXML.fxml"));

            Scene scene = new Scene(root);

            stage.setTitle("User Sign In | Auctioneer");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static String randomPasswordGenerator(int length) {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }
        return sb.toString();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
