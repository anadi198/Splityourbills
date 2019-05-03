package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class CreateGroup {
public JFXButton grp_create_creds;
@FXML
private JFXTextArea email_string;
@FXML
private JFXTextField group_name;
    public void initManager(final LoginManager loginManager, UserCred uc) {
        grp_create_creds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            String emails = email_string.getText().trim();
                ArrayList<String> arrStr = new ArrayList<String>();
                for(int i=0; i<emails.length(); ++i)
                {

                }
            }});

    }
}
