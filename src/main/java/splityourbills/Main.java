package splityourbills;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

//    // Primary Stage
//    Stage window;
//    // Two scenes
//    Scene scene1, scene2, scene3;
//    // The panes are associated with the respective .fxml files
//    private Pane pane1;
//    private Pane pane2;
//    private Pane pane3;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception
    {

//        // Set the window as primary stage
//        window = primaryStage;
//        window.setResizable(false);
//        window.setTitle("Split your bills!");
//        Font.loadFont(getClass().getResourceAsStream("/fonts/Roboto-Light.ttf"), 14);
//        // Load the fxml files and their controllers
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("/fxml/Login-screen.fxml"));
//        pane1 = loader.load();
//        LoginScreen controller1 = loader.getController();
//
//        loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("/fxml/Signup-screen.fxml"));
//        pane2 = loader.load();
//        SignupScreen controller2 = loader.getController();
//
//        loader = new FXMLLoader();
//        loader.setLocation(Main.class.getResource("/fxml/Welcome-screen.fxml"));
//        pane3 = loader.load();
//        WelcomeScreen controller3 = loader.getController();
//
//        // The scenes are based on what has been loaded from the .fxml files
//        scene1 = new Scene(pane1, 487, 169);
//        scene2 = new Scene(pane2);
//        scene3 = new Scene(pane3);
//        // Pass reference the each scenes controller
//        controller1.setScene2(scene2);
//        controller1.setScene3(scene3);
//        controller1.setMain(this);
//        controller2.setScene1(scene1);
//        controller2.setScene3(scene3);
//        controller2.setMain(this);
//        controller3.setScene1(scene1);
//        controller3.setMain(this, localId);
//
//        //Display scene 1 at first
//        window.setScene(scene1);
//        window.show();
        Scene scene = new Scene(new Pane(), 487, 169);

        LoginManager loginManager = new LoginManager(scene, primaryStage);
        loginManager.showLoginScreen();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
//    // used by the controllers to switch the scenes
//    public void setScene(Scene scene) {
//        window.setScene(scene);
//    }
}