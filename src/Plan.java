import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by humunuk on 1/3/16.
 */
public class Plan {

    private final StringProperty planName;

    public Plan(String name) {
        this.planName = new SimpleStringProperty(name);
    }

    public String getPlanName() {
        return planName.get();
    }

    public void setPlanName(String name) {
        this.planName.set(name);
    }

    public StringProperty planNameProperty() {
        return planName;
    }
}
