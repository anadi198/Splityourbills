package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;


public class LoginScreen
{
    public JFXButton sign_in, sign_up;
//    private Scene scene2, scene3;
//    private Main main;
    @FXML
    private JFXPasswordField pwd_text;
    public JFXTextField email_text;

//    public void setMain(Main main){
//        this.main = main;
//    }
//    public void setScene2(Scene scene2){
//        this.scene2 = scene2;
//    }
//    public void setScene3(Scene scene3){this.scene3 = scene3;}
//    @FXML
//    public void switchScene()
//    {
//        main.setScene(scene2);
//    }
//    @FXML
//    public void goAhead(){ main.setScene(scene3);}
//   @FXML
//    public void loginButtonClicked() throws FirebaseException, IOException, JacksonUtilityException, InterruptedException {

//        String tmpdir = System.getProperty("java.io.tmpdir");
//        new File(tmpdir+"/splityourbills").mkdirs();
//        File localIdTemp = null; //localIdTemp.delete()
//        try {
//            //Create two temporary files.
//            localIdTemp = File.createTempFile("localId", ".txt");
//        } catch (IOException ex) {
//            System.err.println("An IOException was caught: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        try {
//            Files.write(Paths.get(tmpdir + "splityourbills/localId.txt"), localId.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }
    public void initManager(final LoginManager loginManager) {
            sign_in.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                 try{
                     String email = email_text.getText().trim();
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
                         //TODO: Reset fields error
                     }
                 }
                 catch (
                         FirebaseException | IOException | JacksonUtilityException e
                 )
                 {
                    //error
                 }
                }});

    }
//    @FXML
//    public void signupButtonClicked()
//    {
//        switchScene();
//    }
}

