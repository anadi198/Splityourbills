package splityourbills;

import com.jfoenix.controls.JFXProgressBar;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.application.Application.launch;

public class ProgressForm {
    private final Stage dialogStage;
    private final JFXProgressBar pb = new JFXProgressBar();

    public ProgressForm() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.setWidth(200);
        pb.setPrefWidth(200);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        dialogStage.setX((primScreenBounds.getWidth()-dialogStage.getWidth()) / 2);
        dialogStage.setY((primScreenBounds.getHeight()) / 2);

        // PROGRESS BAR
        pb.setProgress(-1F);

        final HBox hb = new HBox();
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(pb);

        Scene scene = new Scene(hb);
        dialogStage.setScene(scene);
    }

    public void activateProgressBar(final Task<?> task)  {
        pb.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

