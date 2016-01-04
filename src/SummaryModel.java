import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
