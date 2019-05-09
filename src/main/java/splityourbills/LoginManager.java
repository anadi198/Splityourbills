package splityourbills;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Group-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(480);
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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/new-expense.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(480);
            NewExpense controller = loader.<NewExpense>getController();
            controller.initManager(this, uc, time);
        } catch (IOException | FirebaseException | JacksonUtilityException | NullPointerException e) {
            Logger.getLogger(CreateGroup.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    public void createGroup(UserCred uc)
    {
        primaryStage.setWidth(640);
        primaryStage.setHeight(480);
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/create-group.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(480);
            CreateGroup controller =
                    loader.<CreateGroup>getController();
            controller.initManager(this, uc, scene);
        } catch (IOException ex) {
            Logger.getLogger(CreateGroup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Login-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            LoginScreen controller =
                    loader.<LoginScreen>getController();
            controller.initManager(this);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void showSignupScreen() {
        try {
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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Welcome-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            primaryStage.setResizable(true);
            primaryStage.setMinHeight(480);
            WelcomeScreen controller =
                    loader.<WelcomeScreen>getController();
            controller.initUsername(this, uc);
        } catch (IOException | FirebaseException | JacksonUtilityException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
