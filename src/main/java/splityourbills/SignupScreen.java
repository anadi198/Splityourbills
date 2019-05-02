package splityourbills;

import com.google.appengine.api.users.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import net.thegreshams.firebase4j.model.FirebaseResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.regex.Pattern;


public class SignupScreen {
//    private Scene scene1, scene3;
//    private Main main;

//    public void setMain(Main main){this.main = main;}
//    public void setScene1(Scene scene1){this.scene1 = scene1;}
//    public void setScene3(Scene scene3){this.scene3 = scene3;}
    // this method is called by clicking the button
//    @FXML
//    public void goBack(){main.setScene(scene1);}
//    @FXML
//    public void goAhead(){main.setScene(scene3);}
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
        //TODO: add checks to make sure no field is empty

        if(flag==false)
        {
            //prompt or alert
        }
        else {
            if (password.equals(password_repeat))
            {
                FirebaseResponse response = Signup.register(email, password);
                if (response.getSuccess()==true)
                {
                    System.out.println("SIGN UP WORKED");
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(response.getRawBody());
                    JsonNode idNode = rootNode.path("localId");
                    String localId = idNode.toString();
                    DB.storeUser(localId,id,email);
//                    String tmpdir = System.getProperty("java.io.tmpdir");
//                    new File(tmpdir+"/splityourbills").mkdirs();
//                    File localIdTemp = null; //localIdTemp.delete()
//                    try {
//                        //Create two temporary files.
//                        localIdTemp = File.createTempFile("localId", ".txt");
//                    } catch (IOException ex) {
//                        System.err.println("An IOException was caught: " + ex.getMessage());
//                        ex.printStackTrace();
//                    }
//                    try {
//                        Files.write(Paths.get(tmpdir + "splityourbills/localId.txt"), localId.getBytes());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    UserCred uc = new UserCred(localId, id);
                    loginManager.authenticated(uc);
                }
                else
                {
                    //failed to sign in
                }

            }
            else
            {
                //prompt or alert
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
                    //error
                }
            }});

    }
}