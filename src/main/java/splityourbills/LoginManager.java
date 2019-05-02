package splityourbills;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/** Manages control flow for logins */
public class LoginManager {
    private Scene scene;

    public LoginManager(Scene scene) {
        this.scene = scene;
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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Welcome-screen.fxml")
            );
            scene.setRoot((Parent) loader.load());
            WelcomeScreen controller =
                    loader.<WelcomeScreen>getController();
            controller.initUsername(this, uc);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
