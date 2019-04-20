package splityourbills;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;
        Main.primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("Login-screen.fxml"));
        Font.loadFont(getClass().getResourceAsStream("/resources/fonts/Roboto-Light.ttf"), 14);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 487, 169));
        primaryStage.show();
    }


}