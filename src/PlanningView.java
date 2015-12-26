import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by skallari on 12.12.15.
 */
public class PlanningView {

    private HBox mainBtnLoc;
    private HBox semBtnLoc;
    private VBox textLoc;
    private Text intro;
    private Stage stage;
    private ToggleButton button;
    final ToggleGroup mainGroup = new ToggleGroup();
    final ToggleGroup semGroup = new ToggleGroup();

    public PlanningView() {

        stage = new Stage();

        textLoc = new VBox();
        mainBtnLoc = new HBox();


        Scene scene = new Scene(textLoc, 500, 600);

        // Main buttons to choose year/summary
        //Few hours from sunday -> managed to switch Button to ToggleButton - that is awesome

        ToggleButton firstYear = this.getToggleButton("1", mainGroup);
        ToggleButton secondYear = this.getToggleButton("2", mainGroup);
        ToggleButton thirdYear = this.getToggleButton("3", mainGroup);
        ToggleButton fourthYear = this.getToggleButton("4", mainGroup);
        ToggleButton summary = this.getToggleButton("Kokkuvõte", mainGroup);

        // Get semester specific buttons for year, ignore for summary.
        mainGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle deactivated, Toggle activated) {
                if (activated == null) {
                    removeDetailView();
                } else {
                    if (textLoc.getChildren().contains(semBtnLoc)) {
                        removeDetailView();
                    }
                    showDetailView(mainGroup.getSelectedToggle());
                }
            }
        });

        mainBtnLoc.getChildren().addAll(firstYear, secondYear, thirdYear, fourthYear, summary);
        mainBtnLoc.setPadding(new Insets(10));

        intro = getIntro();

        textLoc.getChildren().addAll(intro, mainBtnLoc);
        textLoc.setPadding(new Insets(10));
        //Adds css stylesheet to layout
        textLoc.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        stage.setScene(scene);
        stage.show();

    }

    private Text getIntro() {

        intro = new Text("Vali aasta ja semester");
        intro.setFont(new Font(15));

        return intro;

    }

    private ToggleButton getToggleButton(String name, ToggleGroup group) {

        button = new ToggleButton(name);
        button.setId(name);
        button.setPrefSize(110, 20);
        button.setToggleGroup(group);

        return button;
    }

    private void showDetailView(Toggle button) {

        semBtnLoc = new HBox();

        // Semester specific buttons.

        ToggleButton autumn = getToggleButton("Sügis", semGroup);
        ToggleButton spring = getToggleButton("Kevad", semGroup);

        semBtnLoc.getChildren().addAll(autumn, spring);

        textLoc.getChildren().add(semBtnLoc);

    }

    //Remove and switch semester specific buttons
    private void removeDetailView() {
        this.textLoc.getChildren().remove(semBtnLoc);
    }
}
