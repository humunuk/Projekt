import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;

/**
 * Created by skallari on 12.12.15.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        new StartUpView(primaryStage);
        String curriculumName = "Isd-k";
        String curriculumPath = "curriculums/oppekava-isdk.csv";
        new DBSetUp(curriculumName, curriculumPath);
    }

}
