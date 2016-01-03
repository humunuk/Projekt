/**
 * Created by skallari on 31.10.15.
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class StartUpView {

    private VBox buttonPlacement;
    private Scene scene;
    private Button chooseNewPlan;
    private Button chooseOldPlan;
    private String title = "Alusta planeerimist";
    private Stage stage;


    public StartUpView(Stage primaryStage) {

        buttonPlacement = new VBox();
        scene = new Scene(buttonPlacement, 500, 600);
        buttonPlacement.setAlignment(Pos.CENTER);
        buttonPlacement.setSpacing(10);

        chooseNewPlan = getNewPlan();
        chooseOldPlan = getOldPlan();

        buttonPlacement.getChildren().addAll(chooseNewPlan, chooseOldPlan);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();

        stage = primaryStage;

    }

    private Button getNewPlan() {

        Button chooseNewPlan = new Button("Loo uus plaan");
        chooseNewPlan.setPrefSize(200, 50);

        chooseNewPlan.setOnAction(event -> {
                    //Add a pop-up to ask for a plan name, do not allow duplicate plan names

                    new InputNewPlanModal(stage);
//                        PlanningView planningView = new PlanningView();
//                        SaveModel saveModel = new SaveModel();
//                        SummaryModel summaryModel = new SummaryModel();
//                        new PlanningController(planningView, summaryModel, saveModel);
//                        stage.close();
                }
        );

        return chooseNewPlan;

    }

    private Button getOldPlan() {
        Button chooseOldPlan = new Button("Leia vana plaan");
        chooseOldPlan.setPrefSize(200, 50);

        chooseOldPlan.setOnAction(event -> {
            //Pop to display list of plan names, when chosen planning controller gets initialized with plan name
        });

        return chooseOldPlan;
    }

}
