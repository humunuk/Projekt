/**
 * Created by skallari on 31.10.15.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //WTF is stage? Stage is something awesome that displays you some awesome stuff that you can display

        VBox buttonPlacement = new VBox();
        Scene scene = new Scene(buttonPlacement, 500, 600);
        buttonPlacement.setAlignment(Pos.CENTER);
        buttonPlacement.setSpacing(10);
        primaryStage.setScene(scene);
        primaryStage.show();

        Label intro = new Label("Planeeri oma õppekava");

        Button chooseNewPlan = new Button("Loo uus plaan");
        chooseNewPlan.setPrefSize(200, 50);

        chooseNewPlan.setOnAction(event -> {
                    HBox createPlan = new HBox();
                    Scene createPlanScene = new Scene(createPlan, 500, 600);
                    primaryStage.setScene(createPlanScene);

                    createPlan.getChildren().add(intro);

                }
        );

        Button chooseOldPlan = new Button("Leia vana plaan");
        chooseOldPlan.setPrefSize(200, 50);
        buttonPlacement.getChildren().addAll(chooseNewPlan, chooseOldPlan);


    }
}
