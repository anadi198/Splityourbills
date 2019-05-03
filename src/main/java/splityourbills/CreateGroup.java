package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.util.ArrayList;

public class CreateGroup {
public JFXButton grp_create_creds, add_member;;
@FXML
private JFXTextField group_name;
    public void initManager(final LoginManager loginManager, UserCred uc, Scene scene) {
        grp_create_creds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<String> arrStr = new ArrayList<String>();

            }});
        add_member.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<String> arrStr = new ArrayList<String>();

            }});

    }
}
