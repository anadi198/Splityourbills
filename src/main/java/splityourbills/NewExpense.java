package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.util.ArrayList;

import static splityourbills.DB.findUsers;

public class NewExpense
{
    public CheckListView checkList;
    public JFXButton add_expense, back;
    public JFXTextField what_for, how_much;
    public Double amount;
    public String description;
    public void initManager(final LoginManager loginManager, UserCred uc, String time)throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        ArrayList<String> users = new ArrayList<>();
            users = findUsers(time);
            for(int i=0; i<users.size(); i++)
            {
                if(users.get(i).replace("\"","").equals(uc.username)==false)
                {
                    checkList.getItems().add(users.get(i).replace("\"",""));
                }
            }
        add_expense.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                amount = Double.parseDouble(how_much.getText().trim());
                description = what_for.getText().trim();
                ObservableList<String> selectedUsers = checkList.getCheckModel().getCheckedItems();
                double size = selectedUsers.size();
                amount = amount/(size+1);
                for (String each: selectedUsers)
                {
                    System.out.println(each);
                    try
                    {
                        DB.oweUser(uc, each, amount);
                        DB.oweThem(uc, each, amount);
                    }
                    catch(IOException | FirebaseException | JacksonUtilityException e )
                    {

                    }
                }
                amount = amount*(size+1);
                try
                {
                    DB.updateGroup(time, uc, description, amount, selectedUsers);
                }
                catch(IOException | FirebaseException | JacksonUtilityException e )
                {

                }
                loginManager.showGroupScreen(uc, time);
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
            loginManager.showGroupScreen(uc, time);
        }
    });
    }

}
