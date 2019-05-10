package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static splityourbills.DB.*;


public class GroupScreen {
    private Executor exec, exec1;
    public MenuItem menu_close, add_user, delete_user;
    public JFXListView list_summary, list_history;
    public JFXButton new_expense, back;
    public Label label;
    ProgressForm pForm = new ProgressForm();

    public void initManager(final LoginManager loginManager, UserCred uc, String time, String groupname)throws FirebaseException, IOException, JacksonUtilityException
    {
        label.setText(groupname);
        list_history.setExpanded(true);
        list_history.setDepth(1);
        list_summary.setExpanded(true);
        list_summary.setDepth(1);

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
        Timeline quarterSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(0.25), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task<UserDetails[]> get_details = new Task<UserDetails[]>() {
                    @Override
                    public UserDetails[] call() throws Exception {
                        try{
                            return getDetails(uc, time);
                        }
                        catch(IOException | FirebaseException e1)
                        {}
                        return new UserDetails[0];
                    }
                };
                get_details.setOnSucceeded(e -> {
                    UserDetails[] ud = get_details.getValue();
                    list_summary.getItems().clear();
                    for(int i=0;i<ud.length;i++)
                    {
                        if(ud[i]!=null)
                        {
                            list_summary.getItems().add(new Label(ud[i].finalBalance()));
                        }
                    }
                });
                exec.execute(get_details);
                //
                Task<GroupDetails[]> get_gdetails = new Task<GroupDetails[]>() {
                    @Override
                    public GroupDetails[] call() throws Exception {
                        try{
                            return getGroupDetails(time);
                        }
                        catch(IOException | FirebaseException e1)
                        {}
                        return new GroupDetails[0];
                    }
                };
                get_gdetails.setOnSucceeded(e -> {
                    GroupDetails[] gd = get_gdetails.getValue();
                    list_history.getItems().clear();
                    for(int i =0; i<gd.length;i++)
                    {
                        if(gd[i]!=null)
                        {
                            list_history.getItems().add(new Label(gd[i].Display()));
                        }
                    }
                });
                exec1.execute(get_gdetails);
            }
        }));
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task<UserDetails[]> get_details = new Task<UserDetails[]>() {
                    @Override
                    public UserDetails[] call() throws Exception {
                        try{
                            return getDetails(uc, time);
                        }
                        catch(IOException | FirebaseException e1)
                        {}
                        return new UserDetails[0];
                    }
                };
                get_details.setOnSucceeded(e -> {
                    UserDetails[] ud = get_details.getValue();
                    list_summary.getItems().clear();
                    for(int i=0;i<ud.length;i++)
                    {
                        if(ud[i]!=null)
                        {
                            list_summary.getItems().add(new Label(ud[i].finalBalance()));
                        }
                    }
                });
                exec.execute(get_details);
                //
                Task<GroupDetails[]> get_gdetails = new Task<GroupDetails[]>() {
                    @Override
                    public GroupDetails[] call() throws Exception {
                        try{
                            return getGroupDetails(time);
                        }
                        catch(IOException | FirebaseException e1)
                        {}
                        return new GroupDetails[0];
                    }
                };
                get_gdetails.setOnSucceeded(e -> {
                    GroupDetails[] gd = get_gdetails.getValue();
                    list_history.getItems().clear();
                    for(int i =0; i<gd.length;i++)
                    {
                        if(gd[i]!=null)
                        {
                            list_history.getItems().add(new Label(gd[i].Display()));
                        }
                    }
                });
                exec1.execute(get_gdetails);
            }
        }));
        quarterSecondsWonder.setCycleCount(2);
        quarterSecondsWonder.play();
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        new_expense.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fiveSecondsWonder.stop();
                loginManager.newExpense(uc, time, groupname);
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                fiveSecondsWonder.stop();
                loginManager.showMainView(uc);
            }
        });
        menu_close.setOnAction(e -> loginManager.close());
        add_user.setOnAction(f->{
            TextInputDialog td = new TextInputDialog("Username");

            // setHeaderText
            td.setHeaderText("Enter the member's username");
            // show the text input dialog
            td.showAndWait();
            ArrayList<String> arrStr = new ArrayList<>();
            arrStr.add(td.getEditor().getText().trim());
            boolean flag = false;
            try
            {
                flag = checkUser(arrStr.get(0));

                if(flag==true)
                {
                    DB.addUser(time,arrStr);
                }

                else
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "Invalid Username!",
                            "The username doesn't exist.");
                }
            }
            catch(FirebaseException | JacksonUtilityException | IOException e)
            {
                //
            }
        });
    }
}
