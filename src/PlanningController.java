import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

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

    public PlanningController(PlanningView planningView, SummaryModel summaryModel, SaveModel saveModel) {
        this.planningView = planningView;
        this.summaryModel = summaryModel;
        this.saveModel = saveModel;

        this.planningView.addListenForToggle(new ToggleListener());
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
            }
        }
    }

    private void setDropDownSubjects(Toggle semToggle, Toggle yearToggle, ObservableList<Map> subjectTableData) {

        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        planningView.subjectTable.getItems().setAll(summaryModel.fetchSubjectsBySemester(semester, year, planningView.subjectMapKey, planningView.yearMapKey, planningView.eapMapKey, planningView.typeMapKey, planningView.idMapKey, subjectTableData));

    }

}
