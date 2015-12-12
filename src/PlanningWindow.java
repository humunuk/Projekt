import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by skallari on 12.12.15.
 */
public class PlanningWindow {

    private HBox buttonPlacement;
    private VBox textPlacement;
    private Text intro;
    private Stage stage;

    public PlanningWindow() {

        stage = new Stage();
        textPlacement = new VBox();
        buttonPlacement = new HBox();
        Scene scene = new Scene(textPlacement, 500, 600);
        intro = this.getIntro();
        textPlacement.getChildren().addAll(intro, buttonPlacement);

        stage.setScene(scene);
        stage.show();

    }

    private Text getIntro() {

        intro  = new Text("Vali aasta ja semester");
        intro.setFont(new Font(15));

        return intro;

    }
}
