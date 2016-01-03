import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by skallari on 12.12.15.
 */
public class PlanningView {

    private HBox mainBtnLoc;
    public HBox semBtnLoc;
    public VBox semDetail;
    public VBox textLoc;
    private Text intro;
    private Stage stage;
    private ToggleButton button;
    private SplitPane mainScene;
    private AnchorPane buttons;
    public VBox subjectList;
    final ToggleGroup mainGroup = new ToggleGroup();
    final ToggleGroup semGroup = new ToggleGroup();
    final TableView summaryTable = new TableView();
    final TableView subjectTable = new TableView();
    public String subjectMapKey = "subject";
    public String yearMapKey = "year";
    public String eapMapKey = "eap";
    public String typeMapKey = "type";
    public String idMapKey = "id";
    public ObservableList<Map> subjectTableData = FXCollections.observableArrayList();


    public PlanningView() {

        //New stage for new window
        stage = new Stage();

        //Setup default layout
        mainScene = new SplitPane();
        buttons = new AnchorPane();
        subjectList = new VBox();
        textLoc = new VBox();
        mainBtnLoc = new HBox();
        semDetail = new VBox();

        //Set up scene for new window
        Scene scene = new Scene(mainScene, 1024, 768);

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

        buttons.getChildren().add(textLoc);

        mainScene.getItems().addAll(subjectList, buttons);
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
        ToggleButton autumn = getToggleButton("autumn", semGroup);
        autumn.setText("Sügis");
        ToggleButton spring = getToggleButton("spring", semGroup);
        spring.setText("Kevad");

        semBtnLoc.getChildren().addAll(autumn, spring);
        textLoc.getChildren().add(semBtnLoc);
    }

    public void addListenForToggle(ChangeListener<Toggle> toggle) {
        mainGroup.selectedToggleProperty().addListener(toggle);
        semGroup.selectedToggleProperty().addListener(toggle);

    }

    public void removeSubjectList() {
        subjectList.getChildren().remove(subjectTable);
    }

    public void getSubjectList(Toggle selectedToggle) {

        TableColumn<Map, String> subjectColumn = new TableColumn("Nimi");
        subjectColumn.setCellValueFactory(new MapValueFactory(subjectMapKey));
        subjectColumn.setPrefWidth(200);
        TableColumn<Map, String> eapColumn = new TableColumn("EAP");
        eapColumn.setCellValueFactory(new MapValueFactory(eapMapKey));
        TableColumn<Map, String> yearColumn = new TableColumn("Aasta");
        yearColumn.setCellValueFactory(new MapValueFactory(yearMapKey));
        TableColumn<Map, String> typeColumn = new TableColumn("Tüüp");
        typeColumn.setCellValueFactory(new MapValueFactory(typeMapKey));

        addSubjectToPlan();

        subjectTable.setEditable(false);
        subjectTable.getSelectionModel().setCellSelectionEnabled(true);
        subjectTable.getColumns().setAll(subjectColumn, eapColumn, yearColumn, typeColumn);
        subjectTable.setPrefHeight(768);
        subjectList.getChildren().add(subjectTable);
    }

    private void addSubjectToPlan() {
        subjectTable.setRowFactory( tv -> {
           TableRow row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    HashMap rowData = (HashMap) row.getItem();
                    HBox mingiJebla = new HBox();
                    Label label = new Label(rowData.get(subjectMapKey).toString());
                    label.setId(rowData.get(idMapKey).toString());
                    mingiJebla.getChildren().add(label);
                    semDetail.getChildren().add(mingiJebla);
                }
            });
            return row;
        });
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

    //Remove semester specific details
    public void removeSemDetail() { textLoc.getChildren().remove(semDetail); }

    public void getPlanningList() { textLoc.getChildren().add(semDetail); }
}