import java.util.List;

/**
 * Created by humunuk on 1/4/16.
 */
public class StartUpController {

    private SummaryModel summaryModel;
    private StartUpView startUpView;

    public StartUpController(SummaryModel summaryModel, StartUpView startUpView) {
        this.summaryModel = summaryModel;
        this.startUpView = startUpView;

        this.onNewPlanAction();
        this.onOldPlanAction();
    }

    private void onOldPlanAction() {
        startUpView.chooseOldPlan.setOnAction(event -> {
            List<String> plans = summaryModel.fetchAllPlans();
            if (plans.isEmpty()) {
                startUpView.getWarningNoPlansInDb();
            } else {
                startUpView.getChooseOldPlanDialog(plans);
                if (startUpView.plan != null) {
                    initPlanningView(startUpView.plan);
                }
            }
        });
    }

    private void initPlanningView(Plan plan) {
        PlanningView planningView = new PlanningView();
        SaveModel saveModel = new SaveModel();
        SummaryModel summaryModel = new SummaryModel();
        new PlanningController(planningView, summaryModel, saveModel, plan);
        startUpView.stage.close();
    }

    private void onNewPlanAction() {
        startUpView.chooseNewPlan.setOnAction(event -> {
            new InputNewPlanModal(startUpView.stage);
        });
    }
}
