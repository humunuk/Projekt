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

import java.lang.reflect.Array;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by skallari on 12.12.15.
 */
public class PlanningView {

    private HBox mainBtnLoc;
    public HBox semBtnLoc;
    public VBox semDetail;
    public VBox textLoc;
    public HBox summary;
    public VBox totals;
    private Text intro;
    private Stage stage;
    private ToggleButton button;
    public SplitPane mainScene;
    private AnchorPane buttons;
    public VBox subjectList;
    final ToggleGroup mainGroup = new ToggleGroup();
    final ToggleGroup semGroup = new ToggleGroup();
    final TableView summaryTable = new TableView();
    final TableView subjectTable = new TableView();
    final TableView planTable = new TableView();
    public String subjectMapKey = "subject";
    public String yearMapKey = "year";
    public String eapMapKey = "eap";
    public String typeMapKey = "type";
    public String idMapKey = "id";
    public String cbMapKey = "checkbox";
    public String deleteMapKey = "delete";
    public ObservableList<Map> subjectTableData = FXCollections.observableArrayList();
    public ObservableList<Map> planTableData = FXCollections.observableArrayList();
    public ListView<String> firstSubjects = new ListView<>();
    public ListView<String> secondSubjects = new ListView<>();
    public ListView<String> thirdSubjects = new ListView<>();
    public ListView<String> fourthSubjects = new ListView<>();
    public Label mandatorySum = new Label();
    public Label electiveSum = new Label();
    public Label votaSum = new Label();
    public Label sumLabel = new Label();
    public Label mandatoryTotalSum = new Label();
    public Label electiveTotalSum = new Label();
    public Label votaTotalSum = new Label();
    public Label sumTotalLabel = new Label();
    public VBox firstYear = new VBox();
    public VBox secondYear = new VBox();
    public VBox thirdYear = new VBox();
    public VBox fourthYear = new VBox();


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
        summary = new HBox();
        totals = new VBox();

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
            addSummaryPane();
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

        subjectTable.setEditable(false);
        subjectTable.getSelectionModel().setCellSelectionEnabled(true);
        subjectTable.getColumns().setAll(subjectColumn, eapColumn, yearColumn, typeColumn);
        subjectTable.setPrefHeight(768);
        subjectList.getChildren().add(subjectTable);
    }

    private void addSummaryPane() {

        if (summary.getChildren().contains(firstYear)) {
            summary.getChildren().removeAll(firstYear, secondYear, thirdYear, fourthYear);
        }

        summary.getChildren().addAll(firstYear, secondYear, thirdYear, fourthYear);


        textLoc.getChildren().add(summary);

        totals.getChildren().addAll(mandatoryTotalSum, electiveTotalSum, votaTotalSum, sumTotalLabel);

        textLoc.getChildren().add(totals);
    }

    public void addPlanTabel(Toggle semester, Toggle year) {

        TableColumn nameColumn = new TableColumn("Nimi");
        nameColumn.setCellValueFactory(new MapValueFactory(subjectMapKey));
        nameColumn.setPrefWidth(300);
        TableColumn eapColumn = new TableColumn("EAP");
        eapColumn.setCellValueFactory(new MapValueFactory(eapMapKey));
        TableColumn votaColumn = new TableColumn("VÕTA?");
        votaColumn.setCellValueFactory(new MapValueFactory(cbMapKey));
        votaColumn.setSortable(false);
        TableColumn removeColumn = new TableColumn();
        removeColumn.setCellValueFactory(new MapValueFactory(deleteMapKey));
        removeColumn.setSortable(false);

        planTable.setEditable(false);
        planTable.getColumns().clear();
        planTable.getColumns().addAll(nameColumn, eapColumn, votaColumn, removeColumn);

        semDetail.getChildren().add(planTable);

    }

    public void addSummaries(int[] summaries) {

        if (semDetail.getChildren().contains(mandatorySum)) {
            removeSummaries();
        }
        mandatorySum.setText("Kohustuslikud: "+summaries[0]);
        electiveSum.setText("Valikained: "+summaries[1]);
        votaSum.setText("VÕTA: "+summaries[2]);
        sumLabel.setText("Kokku: "+ IntStream.of(summaries).sum());

        semDetail.getChildren().addAll(mandatorySum, electiveSum, votaSum, sumLabel);

    }

    public void addTotals(int[] summaries) {

        if (textLoc.getChildren().contains(totals)) {
            removeTotals();
        }

        if (totals.getChildren().contains(mandatoryTotalSum)) {
            removeTotalChilds();
        }
        mandatoryTotalSum.setText("Kohustuslikud: "+summaries[0]);
        electiveTotalSum.setText("Valikained: "+summaries[1]);
        votaTotalSum.setText("VÕTA: "+summaries[2]);
        sumTotalLabel.setText("Kokku: "+ IntStream.of(summaries).sum());

    }

    public void removeTotals() {
        textLoc.getChildren().remove(totals);
    }

    public void removeTotalChilds() {
        totals.getChildren().removeAll(mandatoryTotalSum, electiveTotalSum, votaTotalSum, sumTotalLabel);
    }

    public void removeSummaries() {
        semDetail.getChildren().removeAll(mandatorySum, electiveSum, votaSum, sumLabel);
    }

    public void removeSummaryPane() {
        textLoc.getChildren().remove(summary);
    }

    //Remove and switch semester specific buttons
    public void removeDetailView() {
        textLoc.getChildren().remove(semBtnLoc);
    }

    //Remove semester specific details
    public void removeSemDetail() {
        textLoc.getChildren().remove(semDetail);
        semDetail.getChildren().remove(planTable);
    }

    public void getPlanningList() {
        textLoc.getChildren().add(semDetail);
    }
}