package splityourbills;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Splityourbills");
        Scene scene = new Scene(new Pane());
        /**
         * Load the icon, font and css files
         */
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icons/icon.png")));
        Font.loadFont(getClass().getResource("/fonts/Roboto-Light.ttf").toExternalForm(), 14);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        /**
         * Call LoginManager with instance of primaryStage and scene
         * Show LoginScreen through LoginManager
         */
        LoginManager loginManager = new LoginManager(scene, primaryStage);
        loginManager.showLoginScreen();
        /**
         *Set window to not resizable
         */
        primaryStage.setResizable(false);
        /**
         * Align the window to centre of screen
         */
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.setScene(scene);
        primaryStage.show();



    }
}