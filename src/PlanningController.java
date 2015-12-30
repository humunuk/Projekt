import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

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
            boolean isMainGroup = observable.getValue().getToggleGroup().equals(planningView.mainGroup);

            if (isMainGroup) {
                if (activated == null) {
                    planningView.removeDetailView();
                    planningView.removeSummaryPane();
                    planningView.removeDropDown();
                } else {
                    if (planningView.textLoc.getChildren().contains(planningView.summaryTable)) {
                        planningView.removeSummaryPane();
                    }
                    if (planningView.textLoc.getChildren().contains(planningView.semBtnLoc)) {
                        planningView.removeDetailView();
                    }
                    if (planningView.textLoc.getChildren().contains(planningView.curriculumDropDown)) {
                        planningView.removeDropDown();
                    }
                    planningView.getDetailView(planningView.mainGroup.getSelectedToggle());
                }
            } else {
                if (activated == null) {
                    planningView.removeDropDown();
                } else {
                    if (planningView.textLoc.getChildren().contains(planningView.curriculumDropDown)) {
                        planningView.removeDropDown();
                    }
                    setDropDownSubjects(planningView.semGroup.getSelectedToggle(), planningView.mainGroup.getSelectedToggle());
                    planningView.getDropDown(planningView.semGroup.getSelectedToggle());
                }
            }
        }
    }

    private void setDropDownSubjects(Toggle semToggle, Toggle yearToggle) {

        ToggleButton semester = (ToggleButton) semToggle;
        ToggleButton year = (ToggleButton) yearToggle;

        planningView.dropDownSubjects = summaryModel.fetchSubjectsBySemester(semester, year);;

    }

}
