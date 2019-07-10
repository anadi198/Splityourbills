package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.controlsfx.control.CheckListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static splityourbills.DB.findUsers;

public class NewExpense
{
    private Executor exec, exec1 ;
    public CheckListView checkList;
    public JFXButton add_expense, back;
    public JFXTextField what_for, how_much;
    public Double amount;
    public String description;
    public void initManager(final LoginManager loginManager, UserCred uc, String time, String groupname)throws FirebaseException, JsonParseException, JsonMappingException, IOException, JacksonUtilityException
    {
        ProgressForm pForm = new ProgressForm();
        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        exec1 = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
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
                if(how_much.getText().isEmpty() || description.isEmpty())
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Empty fields",
                            "Please fill all the fields properly.");
                }
                else if(amount.isNaN())
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Not a valid number",
                            "Please enter a valid amount.");
                }
                else
                {

                    ObservableList<String> selectedUsers = checkList.getCheckModel().getCheckedItems();
                    double size = selectedUsers.size();
                    if(size==0)
                    {
                        AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "No users selected",
                                "You must choose someone from the list.");
                    }
                    else
                    {

                        amount = amount/(size+1);
                        System.out.println(amount + "AMOUNT AFTER DIVIDE");
                        for (String each: selectedUsers)
                        {
                            //System.out.println(each);
                            Task<String> owe_user = new Task<String>(){
                                @Override
                                public String call()
                                {
                                    try{
                                        DB.oweUser(uc, each, amount);
                                        DB.oweThem(uc, each, amount);
                                        //System.out.println("ONCE");
                                    }
                                    catch(IOException | JacksonUtilityException | FirebaseException e)
                                    {

                                    }
                                    return "Error";
                                }
                            };
                            // binds progress of progress bars to progress of task:
                            pForm.activateProgressBar(owe_user);
                            owe_user.setOnSucceeded(e2 -> {
                                pForm.getDialogStage().close();
                            });
                            pForm.getDialogStage().show();
                            exec.execute(owe_user);
                        }
                        Task<String> update = new Task<String>(){
                            @Override
                            public String call()
                            {
                                try{
                                    DB.updateGroup(time, uc, description, amount, selectedUsers);
                                }
                                catch(IOException | JacksonUtilityException | FirebaseException e)
                                {

                                }
                                return "Error";
                            }
                        };
                        exec1.execute(update);
                        loginManager.showGroupScreen(uc, time, groupname);
                    }
                }
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
            loginManager.showGroupScreen(uc, time, groupname);
        }
    });
    }

}
