package splityourbills;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Manages control flow for logins */
public class LoginManager {
    private Scene scene;
    private Stage primaryStage;
    public LoginManager(Scene scene, Stage primaryStage) {
        this.scene = scene;
        this.primaryStage = primaryStage;
    }
    public Stage primaryStage()
    {
        return primaryStage;
    }
    public void close()
    {
        primaryStage.close();
    }
    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(UserCred uc) {
        showMainView(uc);
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
    }
    public void showGroupScreen(UserCred uc, String time) {
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Group-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            GroupScreen controller =
                    loader.<GroupScreen>getController();
            controller.initManager(this, uc, time);
        } catch (IOException| FirebaseException | JacksonUtilityException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void newExpense(UserCred uc, String time)
    {

        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/new-expense.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            NewExpense controller = loader.<NewExpense>getController();
            controller.initManager(this, uc, time);
        } catch (IOException | FirebaseException | JacksonUtilityException | NullPointerException e) {
            Logger.getLogger(CreateGroup.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void createGroup(UserCred uc)
    {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/create-group.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(false);
            CreateGroup controller =
                    loader.<CreateGroup>getController();
            controller.initManager(this, uc, scene);
        } catch (IOException ex) {
            Logger.getLogger(CreateGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showLoginScreen() {
        try {
            primaryStage.setWidth(487);
            primaryStage.setHeight(200);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Login-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(false);
            LoginScreen controller =
                    loader.<LoginScreen>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showSignupScreen() {
        try {
            primaryStage.setWidth(487);
            primaryStage.setHeight(300);
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Signup-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            SignupScreen controller =
                    loader.<SignupScreen>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showMainView(UserCred uc) {
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth()-primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Welcome-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            WelcomeScreen controller =
                    loader.<WelcomeScreen>getController();
            controller.initUsername(this, uc);
        } catch (IOException | FirebaseException | JacksonUtilityException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
