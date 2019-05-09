package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

import static splityourbills.DB.*;


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
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try{
                    System.out.println("Refreshing...");
                    UserDetails[] ud = getDetails(uc, time);
                    list_summary.getItems().clear();
                    for(int i=0;i<ud.length;i++)
                    {
                        if(ud[i]!=null)
                        {
                            list_summary.getItems().add(new Label(ud[i].finalBalance()));
                        }
                    }
                    GroupDetails[] gd = getGroupDetails(time);
                    list_history.getItems().clear();
                    for(int i =0; i<gd.length;i++)
                    {
                        if(gd[i]!=null)
                        {
                            list_history.getItems().add(new Label(gd[i].Display()));
                        }
                    }
                }
                catch(FirebaseException | IOException | JacksonUtilityException e)
                {

                }

            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        new_expense.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fiveSecondsWonder.stop();
                loginManager.newExpense(uc, time);
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
