import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.sql.*;
import java.util.*;

/**
 * Created by humunuk on 12/26/15.
 */
public class SummaryModel {

    private Connection conn;
    private PreparedStatement prep;
    private ResultSet results;

    public SummaryModel() {
        initConnection();
    }

    private void initConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:project.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ObservableList<Map> fetchSubjectsBySemester(ToggleButton semester, ToggleButton year, String subjectMapKey, String yearMapKey, String eapMapKey, String typeMapKey, String idMapKey, ObservableList<Map> subjectTableData) {
            initConnection();
        //Clear old data when new button is pushed
        subjectTableData.clear();
        try {
            prep = conn.prepareStatement("SELECT id, subject, eap, year, mandatory FROM subjects  where semester = ? ORDER BY (year = ?) DESC");
            prep.setString(1, semester.getId());
            prep.setInt(2, Integer.parseInt(year.getId()));
            ResultSet results = prep.executeQuery();
            while (results.next()) {
                Map<String, String> tableRow = new HashMap<>();
                tableRow.put(subjectMapKey, results.getString("subject"));
                tableRow.put(eapMapKey, results.getString("eap"));
                tableRow.put(yearMapKey, results.getString("year"));
                if (results.getInt("mandatory") == 1) {
                    tableRow.put(typeMapKey, "Kohustuslik");
                } else {
                    tableRow.put(typeMapKey, "Valikaine");
                }
                tableRow.put(idMapKey, results.getString("id"));
                subjectTableData.add(tableRow);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
        return subjectTableData;
    }

    private void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public List<String> fetchAllPlans() {

        List<String> oldPlans = new ArrayList<>();
        try {
            prep = conn.prepareStatement("SELECT name FROM plans;");
            ResultSet results = prep.executeQuery();
            while (results.next()) {
                oldPlans.add(results.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return oldPlans;
    }

    public Collection fetchPlanDetails(Plan plan, ToggleButton semester, ToggleButton year, ObservableList<Map> planTableData, String subjectMapKey, String eapMapKey, String idMapKey) {

        initConnection();
        planTableData.clear();

        try {
            prep = conn.prepareStatement("SELECT plan_subjects.id as pId, subjects.id as subId, subjects.subject, subjects.eap FROM plan_subjects JOIN subjects ON subjects.id = plan_subjects.subject_id WHERE subjects.semester = ? AND plan_subjects.year = ? GROUP BY subjects.id");
            prep.setObject(1, semester.getId());
            prep.setObject(2, year.getId());
            results = prep.executeQuery();

            while (results.next()) {
                String subId = results.getString("subId");
                Button deleteBtn = new Button("Eemalda");
                deleteBtn.setId(results.getString("pId"));
                deleteBtn.setOnAction(event -> {
                    removeSubjectFromPlan(year, subId);
                    fetchPlanDetails(plan, semester, year, planTableData, subjectMapKey, eapMapKey, idMapKey);
                });
                CheckBox votaBtn = new CheckBox();
                votaBtn.setId(results.getString("pid"));
                Map tableRow = new HashMap<>();
                tableRow.put(subjectMapKey, results.getString("subject"));
                tableRow.put(eapMapKey, results.getString("eap"));
                tableRow.put(idMapKey, subId);
                tableRow.put("delete", deleteBtn);
                tableRow.put("checkbox", votaBtn);
                planTableData.add(tableRow);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        return planTableData;
    }

    private void removeSubjectFromPlan(ToggleButton year, String subId) {
            initConnection();
        String yearId = year.getId();
        try {
            prep = conn.prepareStatement("DELETE FROM plan_subjects WHERE year = ? AND subject_id = ?");
            prep.setObject(1, yearId);
            prep.setObject(2, subId);
            prep.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }
}
