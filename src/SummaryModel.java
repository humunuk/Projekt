import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;

import java.sql.*;

/**
 * Created by humunuk on 12/26/15.
 */
public class SummaryModel {

    private Connection conn = null;
    private PreparedStatement prep = null;
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


    public ObservableList<String> fetchSubjectsBySemester(ToggleButton semester, ToggleButton year) {
        ObservableList<String> subjects = FXCollections.observableArrayList();
        try {
            prep = conn.prepareStatement("SELECT subject, eap, year, mandatory, semester FROM subjects ORDER BY (year = ?) DESC, (semester = ?) DESC");
            prep.setInt(1, Integer.parseInt(year.getId()));
            prep.setString(2, semester.getId());
            ResultSet results = prep.executeQuery();
            while (results.next()) {
                subjects.add(results.getString("subject") + " - " + results.getString("year") + " - " + results.getString("semester"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return subjects;
    }

    private void closeConnections() {
        try {
            if (conn != null) {
                conn.close();
            }
            if (prep != null) {
                prep.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
