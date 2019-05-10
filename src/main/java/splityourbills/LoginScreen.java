package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static splityourbills.SignupScreen.isValid;


public class LoginScreen
{
    public JFXButton sign_in, sign_up;
    private Executor exec, exec1 ;
    @FXML
    private JFXPasswordField pwd_text;
    public JFXTextField email_text;
    public void initManager(final LoginManager loginManager) {
        ProgressForm pForm = new ProgressForm();
        /**
         * Find a non-GUI background thread
         */
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        exec1 = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        sign_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                     /**
                      * Assign a task that returns a String and has a call method in which we call our database operation
                      */
                     Task<String> signin = new Task<String>(){
                         @Override
                         public String call()
                         {
                             try{
                                 return Signin.signIn(email, password);
                             }
                             catch(IOException | JacksonUtilityException | FirebaseException e)
                             {

                             }
                             return "Error";
                         }
                     };
                     /**
                      * This code is executed upon successful execution of call() and can be used to handle the GUI events finally.
                      */
                     signin.setOnSucceeded(e -> {
                         /**
                          * Get the value from return of Task's call() method.
                          */
                         String localID = signin.getValue();
                         if (localID != "Error") {
                                 Task<String> get_username = new Task<String>(){
                                     @Override
                                     public String call()
                                     {
                                         try{
                                             return DB.getUsername(localID);
                                         }
                                         catch(IOException | FirebaseException e)
                                         {

                                         }
                                         return "Error";
                                     }
                                 };
                             // binds progress of progress bars to progress of task:
                             pForm.activateProgressBar(get_username);
                             get_username.setOnSucceeded(e2 -> {
                                 pForm.getDialogStage().close();
                                 String username = get_username.getValue();
                                 UserCred uc = new UserCred(localID, username, email);
                                 loginManager.authenticated(uc);
                             });
                             pForm.getDialogStage().show();
                             exec1.execute(get_username);
                         }
                         else
                         {
                             AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Error",
                                     "Signin failed. Please retry.");
                             pwd_text.clear();
                         }
                     });
                     exec.execute(signin);

                 }
            }});
        sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.showSignupScreen();
            }});
    }
}

