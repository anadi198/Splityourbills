package splityourbills;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import net.thegreshams.firebase4j.error.FirebaseException;
import net.thegreshams.firebase4j.error.JacksonUtilityException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static splityourbills.DB.checkUser;

public class CreateGroup {
public JFXButton grp_create_creds, add_member, cancel;
public JFXTextField members;
String members_list;
ArrayList<String> arrStr = new ArrayList<String>();
@FXML
private JFXTextField group_name;
Executor exec;
    public void initManager(final LoginManager loginManager, UserCred uc, Scene scene) {
        ProgressForm pForm = new ProgressForm();

        exec = Executors.newCachedThreadPool(runnable -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t ;
        });
        members_list=uc.username;
        arrStr.add(uc.username);
        grp_create_creds.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                    Date date= new Date();
                    long time = date.getTime();
                Task<String> create = new Task<String>(){
                    @Override
                    public String call()
                    {
                        try{
                            DB.storeGroup(group_name.getText().trim(), uc, time, arrStr);
                        }
                        catch(IOException | JacksonUtilityException | FirebaseException e)
                        {

                        }
                        return "Error";
                    }
                };
                pForm.activateProgressBar(create);
                create.setOnSucceeded(e2 -> {
                    pForm.getDialogStage().close();
                    loginManager.showMainView(uc);
                });
                pForm.getDialogStage().show();
                exec.execute(create);


            }});
        add_member.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                TextInputDialog td = new TextInputDialog("Username");

                // setHeaderText
                td.setHeaderText("Enter the member's username");
                // show the text input dialog
                td.showAndWait();
                String member = td.getEditor().getText().trim();
                boolean flag = false;
                try
                {
                    flag = checkUser(member);
                }
                catch(FirebaseException | IOException e)
                {
                    //
                }
                if(flag==true)
                {
                    members_list += ", "+member;
                    // set the text of the label
                    arrStr.add(member);
                    members.setText(members_list);
                }
                else
                {
                    AlertHelper.showAlert(Alert.AlertType.ERROR, loginManager.primaryStage(), "User doesn't exist",
                            "Please enter a valid username.");
                }
            }});
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event)
            {
                loginManager.showMainView(uc);
            }});

    }
}
