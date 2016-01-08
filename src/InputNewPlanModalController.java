import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by humunuk on 1/3/16.
 *
 * Handles new plan creation modal
 */
public class InputNewPlanModalController {

    private SaveModel saveModel;
    private SummaryModel summaryModel;
    private Plan plan;
    private InputNewPlanModal modalView;

    public InputNewPlanModalController(String name, InputNewPlanModal newPlanModal) {
        saveModel = new SaveModel();
        summaryModel = new SummaryModel();
        this.modalView = newPlanModal;
        plan = new Plan(name);

        Boolean newPlanCreated = validatePlan();

        if (newPlanCreated) {
            initPlanningView(plan);
        } else {
            modalView.intro.setText("Sellise nimega plaan on juba olemas");
            modalView.intro.setFont(new Font(15));
            modalView.intro.setTextFill(Color.RED);
        }
    }

    private void initPlanningView(Plan plan) {

        PlanningView planningView = new PlanningView();
        SaveModel saveModel = new SaveModel();
        SummaryModel summaryModel = new SummaryModel();
        new PlanningController(planningView, summaryModel, saveModel, plan);
        modalView.modal.close();
        modalView.parent.close();
    }

    private Boolean validatePlan() {

        return saveModel.checkForPlanOrAddNew(plan);
    }

}
