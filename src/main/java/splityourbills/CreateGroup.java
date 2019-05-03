package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;

import static splityourbills.SignupScreen.isValid;

public class CreateGroup {
public JFXButton grp_create_creds, add_member;
public JFXTextField members;
String members_list;
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
                TextInputDialog td = new TextInputDialog("Email");

                // setHeaderText
                td.setHeaderText("Enter the member's email");
                // show the text input dialog
                td.showAndWait();
                String member = td.getEditor().getText().trim();
                boolean flag = isValid(member);
                if(flag==true)
                {
                    if(members_list==null)
                    {
                        members_list = member;
                    }
                    else
                    {
                        members_list += ", "+member;
                    }
                    // set the text of the label
                    members.setText(members_list);
                }
                else
                {
                    //TODO: wrong email
                }
            }});

    }
}
