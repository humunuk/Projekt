import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
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

    public PlanningController(PlanningView planningView) {
        this.planningView = planningView;
    }

}
