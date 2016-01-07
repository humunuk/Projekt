import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by humunuk on 12/20/15.
 */
public class PlanningController {
    //Saab infot StartUpViewst ja koostab selle põhjal PlanningView
    //Saab infot PlanningViewst ja saadab DetailViewsse
    //Tegelt vist peaks saatma controlleritesse?!
    //Info mis saab: mis nupule vajutati (1, 2, 3, 4 või Kokkuvõte), sama info saadab edasi

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

    class ToggleListener implements ChangeListener<Toggle> {

        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle deactivated, Toggle activated) {

            if (activated == null) {
                planningView.removeDetailView();
                planningView.removeSummaryPane();
                planningView.removeSubjectList();
                planningView.removeSemDetail();
                return;
            }

            if (observable.getValue().getToggleGroup().equals(planningView.mainGroup)) {
                if (planningView.textLoc.getChildren().contains(planningView.summaryTable)) {
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
                planningView.getDetailView(planningView.mainGroup.getSelectedToggle());
            } else {
                if (planningView.subjectList.getChildren().contains(planningView.subjectTable)) {
                    planningView.removeSubjectList();
                }
                if (planningView.textLoc.getChildren().contains(planningView.semDetail)) {
                    planningView.removeSemDetail();
                }
                setDropDownSubjects(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.subjectTableData);
                planningView.getSubjectList(planningView.semGroup.getSelectedToggle());
                planningView.getPlanningList();
                setPlanDetails(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.planTableData);
                planningView.addPlanTabel(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle());
            }
        }
    }

    private void setPlanDetails(Toggle semToggle, Toggle yearToggle, ObservableList<Map> planTableData) {
        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        Collection data = summaryModel.fetchPlanDetails(plan, semester, year, planTableData, planningView.subjectMapKey, planningView.eapMapKey, planningView.idMapKey);

        planningView.planTable.getItems().setAll(data);

    }

    private void setDropDownSubjects(Toggle semToggle, Toggle yearToggle, ObservableList<Map> subjectTableData) {

        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        planningView.subjectTable.getItems().setAll(summaryModel.fetchSubjectsBySemester(semester, year, planningView.subjectMapKey, planningView.yearMapKey, planningView.eapMapKey, planningView.typeMapKey, planningView.idMapKey, subjectTableData));

    }

    private void listenForNewSubject() {
        planningView.subjectTable.setRowFactory(tv -> {
            TableRow row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
// Kui row celli peal klikitakse: * siis lisa antud asi andmebaasi ning viewsse
                    HashMap rowData = (HashMap) row.getItem();
                    saveModel.addSubjectToPlan(plan, planningView.mainGroup.getSelectedToggle(), rowData.get(planningView.idMapKey));
                    planningView.removeSemDetail();
                    planningView.textLoc.getChildren().add(planningView.semDetail);
                    setPlanDetails(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle(), planningView.planTableData);
                    planningView.addPlanTabel(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle());
                }
            });
            return row;
        });
    }

}
