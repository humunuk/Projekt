import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.sql.*;

/**
 * Created by humunuk on 12/20/15.
 */
public class SaveModel {

    private Connection conn;
    private PreparedStatement prep;
    private ResultSet results;

    public SaveModel() {
        initConnection();
    }

    private void initConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:project.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkForPlanOrAddNew(Plan plan) {
            initConnection();
        try {
            prep = conn.prepareStatement("SELECT name FROM plans WHERE name = ?");
            prep.setString(1, plan.getPlanName());
            results = prep.executeQuery();
            if (!results.next()) {
                prep = conn.prepareStatement("INSERT INTO plans (name) VALUES (?)");
                prep.setString(1, plan.getPlanName());
                if (prep.executeUpdate() > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            closeConnections();
        }
    return false;
    }

    private void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int getPlanId(Plan plan) {
        initConnection();
        try {
            prep = conn.prepareStatement("SELECT id FROM plans where name = ?");
            prep.setString(1, plan.getPlanName());
            results = prep.executeQuery();
            while (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnections();
        }
        return 0;
    }

    // Adds subject to plan, semester is not important, since subject cant be in 2 semesters

    public void addSubjectToPlan(Plan plan, Toggle year, Object subjectId) {
        ToggleButton yearId = (ToggleButton) year;
        int planId = getPlanId(plan);
        //Checks if subject is in plan for that year.
        boolean subjectInPlan = isSubjectInPlan(plan, year, subjectId);

        if (!subjectInPlan) {
            initConnection();
            try {
                prep = conn.prepareStatement("INSERT INTO plan_subjects VALUES (?,?,?,?,?)");
                prep.setInt(2, planId);
                prep.setObject(3, subjectId);
                prep.setObject(4, yearId.getId());
                prep.setObject(5, 0);
                prep.execute();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                closeConnections();
            }
        }
    }

    private boolean isSubjectInPlan(Plan plan, Toggle year, Object subjectId) {

        ToggleButton yearId = (ToggleButton) year;
        int planId = getPlanId(plan);

        initConnection();

        try {
            prep = conn.prepareStatement("SELECT id FROM plan_subjects WHERE year = ? AND subject_id = ? AND plan_id = ?");
            prep.setObject(1, yearId.getId());
            prep.setObject(2, subjectId);
            prep.setObject(3, planId);
            results = prep.executeQuery();
            if (!results.next()) {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnections();
        }
        return true;
    }
}
