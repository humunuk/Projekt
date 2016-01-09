import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by humunuk on 12/20/15.
 */
public class PlanningController {

    private PlanningView planningView;
    private SummaryModel summaryModel;
    private SaveModel saveModel;
    private Plan plan;

    public PlanningController(PlanningView planningView, SummaryModel summaryModel, SaveModel saveModel, Plan plan) {
        this.planningView = planningView;
        this.summaryModel = summaryModel;
        this.saveModel = saveModel;
        this.plan = plan;

        this.planningView.addListenForToggle(new ToggleListener());
        listenForNewSubject();
    }

    public void updatePlanningDetailTable() {
        planningView.removeSemDetail();
        planningView.removeSummaries();
        planningView.textLoc.getChildren().add(planningView.semDetail);
        setPlanDetails(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.planTableData);
        planningView.addPlanTabel(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle());
        int[] summaries = summaryModel.fetchSummaries(plan, planningView.mainGroup.getSelectedToggle(), planningView.semGroup.getSelectedToggle());
        planningView.addSummaries(summaries);
    }

    //Listens for togglegroups and buttons, reflects which one was pushed

    class ToggleListener implements ChangeListener<Toggle> {

        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle deactivated, Toggle activated) {

            if (activated == null) {
                planningView.removeDetailView();
                planningView.removeSummaryPane();
                planningView.removeSubjectList();
                planningView.removeSemDetail();
                planningView.removeTotals();
                return;
            }

            if (observable.getValue().getToggleGroup().equals(planningView.mainGroup)) {
                if (planningView.textLoc.getChildren().contains(planningView.summary)) {
                    planningView.removeSummaryPane();
                }
                if (planningView.textLoc.getChildren().contains(planningView.semBtnLoc)) {
                    planningView.removeDetailView();
                }
                if (planningView.subjectList.getChildren().contains(planningView.subjectTable)) {
                    planningView.removeSubjectList();
                }
                if (planningView.textLoc.getChildren().contains(planningView.semDetail)) {
                    planningView.removeSemDetail();
                }
                if (planningView.textLoc.getChildren().contains(planningView.totals)) {
                    planningView.removeTotalChilds();
                    planningView.removeTotals();
                }
                setSummaryData();
                setTotalsData();
                planningView.getDetailView(planningView.mainGroup.getSelectedToggle());

            } else {
                if (planningView.subjectList.getChildren().contains(planningView.subjectTable)) {
                    planningView.removeSubjectList();
                }
                if (planningView.textLoc.getChildren().contains(planningView.semDetail)) {
                    planningView.removeSemDetail();
                }
                if (planningView.textLoc.getChildren().contains(planningView.totals)) {
                    planningView.removeTotalChilds();
                    planningView.removeTotals();
                }
                //Populate view with data
                setDropDownSubjects(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.subjectTableData);
                planningView.getSubjectList(planningView.semGroup.getSelectedToggle());
                planningView.getPlanningList();
                setPlanDetails(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.planTableData);
                planningView.addPlanTabel(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle());
                int[] summaries = summaryModel.fetchSummaries(plan, planningView.mainGroup.getSelectedToggle(), planningView.semGroup.getSelectedToggle());
                planningView.addSummaries(summaries);
            }
        }
    }

    private void setTotalsData() {
        int[] data = summaryModel.fetchTotalSummaries(plan);
        planningView.addTotals(data);
    }

    private void setSummaryData() {
        ListView<String> firstYearData = summaryModel.fetchAllSummaryDataByYear(plan, 1, planningView.firstSubjects);
        ListView<String> secondYearData = summaryModel.fetchAllSummaryDataByYear(plan, 2, planningView.secondSubjects);
        ListView<String> thirdYearData = summaryModel.fetchAllSummaryDataByYear(plan, 3, planningView.thirdSubjects);
        ListView<String> fourthYearData = summaryModel.fetchAllSummaryDataByYear(plan, 4, planningView.fourthSubjects);

        if (planningView.firstYear.getChildren().contains(firstYearData)) {
            planningView.firstYear.getChildren().remove(firstYearData);
        }

        if (planningView.secondYear.getChildren().contains(secondYearData)) {
            planningView.secondYear.getChildren().remove(secondYearData);
        }

        if (planningView.thirdYear.getChildren().contains(thirdYearData)) {
            planningView.thirdYear.getChildren().remove(thirdYearData);
        }

        if (planningView.fourthYear.getChildren().contains(fourthYearData)) {
            planningView.fourthYear.getChildren().remove(fourthYearData);
        }

        planningView.firstYear.getChildren().add(firstYearData);
        planningView.secondYear.getChildren().add(secondYearData);
        planningView.thirdYear.getChildren().add(thirdYearData);
        planningView.fourthYear.getChildren().add(fourthYearData);
    }

    //Sets plan details to view
    private void setPlanDetails(Toggle semToggle, Toggle yearToggle, ObservableList<Map> planTableData) {
        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        Collection data = summaryModel.fetchPlanDetails(plan, semester, year, planTableData, planningView.subjectMapKey, planningView.eapMapKey, planningView.idMapKey, this);

        planningView.planTable.getItems().setAll(data);

    }

    //Sets all subjects to view
    private void setDropDownSubjects(Toggle semToggle, Toggle yearToggle, ObservableList<Map> subjectTableData) {

        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        planningView.subjectTable.getItems().setAll(summaryModel.fetchSubjectsBySemester(semester, year, planningView.subjectMapKey, planningView.yearMapKey, planningView.eapMapKey, planningView.typeMapKey, planningView.idMapKey, subjectTableData));

    }

    //Adds new subject to plan db tabel, updates the tabel to reflect the change
    private void listenForNewSubject() {
        planningView.subjectTable.setRowFactory(tv -> {
            TableRow row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    // Kui row celli peal klikitakse: * siis lisa antud asi andmebaasi ning viewsse
                    HashMap rowData = (HashMap) row.getItem();
                    saveModel.addSubjectToPlan(plan, planningView.mainGroup.getSelectedToggle(), rowData.get(planningView.idMapKey));
                    updatePlanningDetailTable();
                }
            });
            return row;
        });
    }

}
