import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public HBox semBtnLoc;
    public VBox textLoc;
    private Text intro;
    private Stage stage;
    private ToggleButton button;
    final ToggleGroup mainGroup = new ToggleGroup();
    final ToggleGroup semGroup = new ToggleGroup();
    final TableView summaryTable = new TableView();
    public ComboBox<String> curriculumDropDown;
    public ObservableList<String> dropDownSubjects;

    public PlanningView() {

        //New stage for new window
        stage = new Stage();

        //Setup default layout
        textLoc = new VBox();
        mainBtnLoc = new HBox();

        //Set up scene for new window
        Scene scene = new Scene(textLoc, 800, 600);

        // Main buttons to choose year/summary
        //Few hours from sunday -> managed to switch Button to ToggleButton - that is awesome

        ToggleButton firstYear = this.getToggleButton("1", mainGroup);
        ToggleButton secondYear = this.getToggleButton("2", mainGroup);
        ToggleButton thirdYear = this.getToggleButton("3", mainGroup);
        ToggleButton fourthYear = this.getToggleButton("4", mainGroup);
        //In case UTF-8 is not supported, ID is set to Summary for comparsion, but label text is set in estonian
        ToggleButton summary = this.getToggleButton("summary", mainGroup);
        summary.setText("Kokkuvõte");

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

    public void getDetailView(Toggle button) {

        semBtnLoc = new HBox();

        ToggleButton mainGrpBtn = (ToggleButton) mainGroup.getSelectedToggle();

        if (mainGrpBtn.getId().equals("summary")) {
            //Summary View
            getSummary();
        } else {
            // Semester specific buttons and view by year and semester.
            getSemesterSpecificView();
        }
    }

    private void getSemesterSpecificView() {
        ToggleButton autumn = getToggleButton("Sügis", semGroup);
        ToggleButton spring = getToggleButton("Kevad", semGroup);

        semBtnLoc.getChildren().addAll(autumn, spring);
        textLoc.getChildren().add(semBtnLoc);
        autumn.setSelected(true);
    }

    public void addListenForToggle(ChangeListener<Toggle> toggle) {
        mainGroup.selectedToggleProperty().addListener(toggle);
        semGroup.selectedToggleProperty().addListener(toggle);

    }

    public void removeDropDown() {
        textLoc.getChildren().remove(curriculumDropDown);
    }

    public void getDropDown(Toggle selectedToggle) {
        curriculumDropDown = new ComboBox(dropDownSubjects);
        curriculumDropDown.setVisibleRowCount(15);
        textLoc.getChildren().add(curriculumDropDown);
    }

    private void getSummary() {

        TableColumn firstColumn = new TableColumn();
        TableColumn secondColumn = new TableColumn();
        TableColumn firstYearColumn = new TableColumn("1. aasta");
        TableColumn secondYearColumn = new TableColumn("2. aasta");
        TableColumn thirdYearColumn = new TableColumn("3. aasta");
        TableColumn fourthYearColumn = new TableColumn("4. aasta");
        TableColumn summaryColumn = new TableColumn();

        summaryTable.setEditable(false);

        summaryTable.getColumns().clear();

        summaryTable.getColumns().addAll(firstColumn, secondColumn, firstYearColumn, secondYearColumn, thirdYearColumn, fourthYearColumn, summaryColumn);

        textLoc.getChildren().add(summaryTable);
    }

    public void removeSummaryPane() {
        textLoc.getChildren().remove(summaryTable);
    }

    //Remove and switch semester specific buttons
    public void removeDetailView() {
        textLoc.getChildren().remove(semBtnLoc);
    }

}