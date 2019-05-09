package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;

import static splityourbills.DB.findGroups;
class ListViewHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
    }
}
public class WelcomeScreen
{
    public JFXButton logout, create_group;
    @FXML
    private JFXListView<Label> listView;
    @FXML
    private Label label;
    public void initUsername(final LoginManager loginManager, UserCred uc) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        label.setText("Welcome, "+uc.username+"!");
        Group[] groups = findGroups(uc.username);
        int size = groups.length;
        System.out.println("SIZE: "+size);
        for(int i=0;i<size;i++)
        {
            groups[i].name = groups[i].name.replace("\"","");
            listView.getItems().add(new Label((i+1)+ ". " + groups[i].name));
        }
        listView.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                int index = listView.getSelectionModel().getSelectedIndex();
                String time = groups[index].time;
                loginManager.showGroupScreen(uc, time);
            }
        });
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showLoginScreen();
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

