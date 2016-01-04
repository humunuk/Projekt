import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by humunuk on 1/3/16.
 */
public class InputNewPlanModal {

    private VBox modalBox;
    public Label intro;
    private TextField planNameField;
    private HBox btnPlacement;
    private Button okBtn;
    private Button cancelBtn;
    public Stage modal;
    public Stage parent;

    InputNewPlanModal(Stage stage) {
        setupStage(stage);
    }

    private void setupStage(Stage stage) {
        parent = stage;
        modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.initOwner(stage);
        modal.setTitle("Anna uuele plaanile nimi");
        modal.setResizable(false);

        modalBox = new VBox(15);

        addIntro();
        modalBox.getChildren().add(intro);

        addPlanNameField();
        modalBox.getChildren().add(planNameField);

        addButtons();
        modalBox.getChildren().add(btnPlacement);

        Scene dialogScene = new Scene(modalBox, 400, 200);
        modal.setScene(dialogScene);
        modal.show();
    }

    private void addButtons() {
        okBtn = new Button("Loo");
        okBtn.setPadding(new Insets(15));
        okBtn.setDefaultButton(true);
        okBtn.setOnAction(event -> {
            if (planNameField.getText() != null && !planNameField.getText().trim().isEmpty()) {
                new InputNewPlanModalController(planNameField.getText(), this);
            } else {
                intro.setText("Tühjad väljad ei ole lubatud");
                intro.setFont(new Font(15));
                intro.setTextFill(Color.RED);
            }
        });
        cancelBtn = new Button("Tühista");
        cancelBtn.setPadding(new Insets(15));
        cancelBtn.setCancelButton(true);
        cancelBtn.setOnAction(event -> {
            modal.close();
        });

        btnPlacement = new HBox();
        btnPlacement.getChildren().addAll(okBtn, cancelBtn);
    }

    private void addPlanNameField() {
        planNameField = new TextField();
        planNameField.setPadding(new Insets(10));
    }

    private void addIntro() {
        intro = new Label("Mis nimeks uuele plaanile?");
        intro.setFont(new Font(20));
        intro.setPadding(new Insets(20));
    }
}
