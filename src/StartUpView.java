/**
 * Created by skallari on 31.10.15.
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;


public class StartUpView {

    private VBox buttonPlacement;
    private Scene scene;
    public Button chooseNewPlan;
    public Button chooseOldPlan;
    private String title = "Alusta planeerimist";
    public Stage stage;
    public Plan plan;


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

        return chooseNewPlan;

    }

    private Button getOldPlan() {
        Button chooseOldPlan = new Button("Leia vana plaan");
        chooseOldPlan.setPrefSize(200, 50);

        return chooseOldPlan;
    }

    public void getChooseOldPlanDialog(List<String> plans) {

        ChoiceDialog<String> oldPlanDialog = new ChoiceDialog<>(null, plans);
        oldPlanDialog.setTitle("Missugust plaani soovid mudida?");
        oldPlanDialog.setHeaderText("Vali plaan");
        oldPlanDialog.setContentText("Vali plaan: ");

        Optional<String> result = oldPlanDialog.showAndWait();

        if (result.isPresent()) {
            plan = new Plan(result.get());
        }
    }

    public void getWarningNoPlansInDb() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ei ole ühtegi plaani andmebaasis");
        alert.setHeaderText("Ühtegi plaani ei ole andmebaasis, loo ennem mõni");

        alert.showAndWait();
    }
}
