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
import net.thegreshams.firebase4j.model.FirebaseResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.regex.Pattern;


public class SignupScreen {
    public JFXButton register;
    public JFXTextField user_id, email_text;
    @FXML
    private JFXPasswordField pwd_text, pwd_text_repeat;
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    @FXML
    public void regButtonClicked(final LoginManager loginManager) throws FirebaseException, IOException, JacksonUtilityException
    {
        String password, password_repeat, id, email;
        id = user_id.getText().trim();
        email = email_text.getText().trim();
        password = pwd_text.getText();
        password_repeat = pwd_text_repeat.getText();
        boolean flag = isValid(email);
        if(password.isEmpty() || email.isEmpty() || password_repeat.isEmpty() || id.isEmpty())
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Incomplete form!",
                    "Please fill all the fields.");
        }

        else if(flag==false)
        {
            AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Invalid email!",
                    "The email address you entered isn't valid.");
        }
        else {
            if (password.equals(password_repeat))
            {
                FirebaseResponse response = Signup.register(email, password);
                if (response.getSuccess()==true)
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getRawBody());
                    JsonNode idNode = rootNode.path("localId");
                    String localId = idNode.toString();
                    DB.storeUser(localId,id,email);
                    UserCred uc = new UserCred(localId, id, email);
                    loginManager.authenticated(uc);
                }
                else
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Error",
                            "Signup failed.");
                }

            }
            else
            {
                AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Passwords don't match",
                        "Please make sure the passwords match.");
            }
        }
    }
    public void initManager(final LoginManager loginManager) {
        register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    regButtonClicked(loginManager);
                }
                catch (
                        FirebaseException | IOException | JacksonUtilityException e
                )
                {

                }
            }});

    }
}