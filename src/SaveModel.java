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
        if (conn == null) {
            initConnection();
        }
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
}
