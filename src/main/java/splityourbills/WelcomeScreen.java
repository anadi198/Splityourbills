package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.concurrent.Task;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static splityourbills.DB.findGroups;
class ListViewHandler implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent event) {
    }
}
public class WelcomeScreen
{
    private Executor exec;
    public JFXButton logout, create_group;
    @FXML
    private JFXListView<Label> listView;
    @FXML
    private Label label;
    ProgressForm pForm = new ProgressForm();
    public void initUsername(final LoginManager loginManager, UserCred uc) throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        label.setText("Welcome, "+uc.username+"!");
        Task<Group[]> find_groups = new Task<Group[]>(){
            @Override
            public Group[] call() throws Exception {
                try
                {
                    return findGroups(uc.username);
                }
                catch(IOException | FirebaseException | JacksonUtilityException e){}
                return new Group[0];
            }
        };
        pForm.activateProgressBar(find_groups);
        find_groups.setOnSucceeded(e1 -> {
            Group[] groups = find_groups.getValue();
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
            pForm.getDialogStage().close();
        });
        pForm.getDialogStage().show();
        exec.execute(find_groups);
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

