import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by skallari on 12.12.15.
 */
public class PlanningWindow {

    private HBox mainBtnLoc;
    private HBox semBtnLoc;
    private VBox textLoc;
    private Text intro;
    private Stage stage;
    private Button button;
    private boolean clicked = true;
    private String activate = "-fx-base: #000000";
    private String deactivate = "-fx-base: #ffffff";

    public PlanningWindow() {

        stage = new Stage();

        textLoc = new VBox();
        mainBtnLoc = new HBox();


        Scene scene = new Scene(textLoc, 500, 600);

        // Main buttons to choose year/summary

        Button firstYear = this.getButton("1");
        this.setStyle(firstYear);
        Button secondYear = this.getButton("2");
        this.setStyle(secondYear);
        Button thirdYear = this.getButton("3");
        this.setStyle(thirdYear);
        Button fourthYear = this.getButton("4");
        this.setStyle(fourthYear);
        Button summary = this.getButton("Kokkuvõte");
        this.setStyle(summary);

        mainBtnLoc.getChildren().addAll(firstYear, secondYear, thirdYear, fourthYear, summary);
        mainBtnLoc.setPadding(new Insets(10));

        intro = this.getIntro();

        textLoc.getChildren().addAll(intro, mainBtnLoc);
        textLoc.setPadding(new Insets(10));

        stage.setScene(scene);
        stage.show();

    }

    private Text getIntro() {

        intro  = new Text("Vali aasta ja semester");
        intro.setFont(new Font(15));

        return intro;

    }

    private Button getButton(String name) {

        button = new Button(name);
        button.setPrefSize(110, 20);

        return button;
    }

    private void setStyle(Button button) {
        button.setOnAction(event -> {
            toggleSelected(button);
            showExtraBtns();
        });
    }

    private void toggleSelected(Button button) {
        if (button.getStyle().equals(deactivate)) {
            button.setStyle(activate);
        } else {
            button.setStyle(deactivate);
        }
    }

    private void showExtraBtns() {

        semBtnLoc = new HBox();

        // Semester specific buttons.

        Button autumn = this.getButton("Sügis");
        Button spring = this.getButton("Kevad");

        semBtnLoc.getChildren().addAll(autumn, spring);

    }
}
