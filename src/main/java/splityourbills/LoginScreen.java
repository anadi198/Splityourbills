package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;

import static splityourbills.SignupScreen.isValid;


public class LoginScreen
{
    public JFXButton sign_in, sign_up;
//    private Scene scene2, scene3;
//    private Main main;
    @FXML
    private JFXPasswordField pwd_text;
    public JFXTextField email_text;
    public void initManager(final LoginManager loginManager) {

            sign_in.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                 try{
                     String email = email_text.getText().trim();
                     boolean flag = isValid(email);
                     if(flag==false)
                     {
                         AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Invalid email",
                                 "Please enter a valid email address.");
                     }
                     else
                     {
                         String password = pwd_text.getText();
                         String localId = Signin.signIn(email, password);
                         if(localId!="Error")
                         {
                             String username;
                             username = DB.getUsername(localId);
                             UserCred uc = new UserCred(localId, username, email);
                             loginManager.authenticated(uc);
                         }
                         else
                         {
                             AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Error",
                                     "Signin failed. Please retry.");
                             pwd_text.clear();
                         }

                     }
                 }
                 catch (
                         FirebaseException | IOException | JacksonUtilityException e
                 )
                 {
                    //error
                 }
                }});
        sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.showSignupScreen();
            }});
    }
}

