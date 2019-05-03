package splityourbills;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class WelcomeScreen
{
    public JFXButton logoutButton, create_group;
    private Main main;
    private Scene scene1;
    public void setMain(Main main, String localId){this.main = main;
    this.localId = localId;}
//    public void setScene1(Scene scene1){this.scene1 = scene1;}
//    @FXML
//    public void goBack(){main.setScene(scene1);}
    @FXML
    private Label label;
    public String localId=" ";
    public void initUsername(final LoginManager loginManager, UserCred uc) {
        label.setText("Welcome, "+uc.username+"!");
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.logout();
            }
        });
        create_group.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.createGroup(uc);
            }
        });
    }
}

