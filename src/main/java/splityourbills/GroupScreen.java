package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;

import static splityourbills.DB.getDetails;
import static splityourbills.DB.getGroupDetails;

public class GroupScreen {
    public MenuItem menu_close, add_user, delete_user;
    public JFXListView list_summary, list_history;
    public JFXButton new_expense, back;
    public void initManager(final LoginManager loginManager, UserCred uc, String time)throws FirebaseException, IOException, JacksonUtilityException
    {
        UserDetails[] ud = getDetails(uc, time);
        for(int i=0;i<ud.length;i++)
        {
            if(ud[i]!=null)
            {
                list_summary.getItems().add(new Label(ud[i].finalBalance()));
            }
        }
        GroupDetails[] gd = getGroupDetails(time);
        for(int i =0; i<gd.length;i++)
        {
            if(gd[i]!=null)
            {
                list_history.getItems().add(new Label(gd[i].Display()));
            }
        }
        new_expense.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loginManager.newExpense(uc, time);
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showMainView(uc);
            }
        });
        menu_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.close();
            }
        });
    }
}
