import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by humunuk on 12/27/15.
 * <p>
 * Handles setting up database architecture for project
 * Table subjects: id integer (auto incrementing)
 *                    code - subject code in curriculum
 *                    curriculum - curriculum where cubject belongs to
 *                    eap - EAP for subject
 *                    semester - semester for subject
 *                    year - recommended year
 *                    mandatory - 1 for mandatory 2 for elective
 */
public class DBSetUp {

    public DBSetUp(String curriculumName, String curriculumPath) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep = null;
        try {
            //Create connection
            connection = DriverManager.getConnection("jdbc:sqlite:project.db");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.executeUpdate("create table if not exists subjects (id integer primary key asc, code string, curriculum string, subject string, eap integer, semester string, year integer, mandatory integer)");
            statement.executeUpdate("create table if not exists plans (id integer primary key asc, name string, subject_id integer, foreign key (subject_id) references subjects(id))");
            try {
                CSVReader reader = new CSVReader(new FileReader(curriculumPath));
                try {
                    prep = connection.prepareStatement("select curriculum from subjects where curriculum = ? limit 1");
                    prep.setString(1, curriculumName);
                    ResultSet result = prep.executeQuery();
                    if (!result.next()) {
                        prep = connection.prepareStatement("INSERT INTO subjects VALUES (?,?,?,?,?,?,?,?)");
                        String [] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            // ID will be incremented automatically if null value is inserted, thats what we want
                            prep.setString(2, nextLine[1]); // subject code
                            prep.setString(3, nextLine[2]); // curriculum
                            prep.setString(4, nextLine[3]); // subject
                            prep.setObject(5, nextLine[4]); // eap
                            prep.setString(6, nextLine[5]); // semester
                            prep.setObject(7, nextLine[6]); // year
                            prep.setObject(8, nextLine[7]); // Mandatory
                            prep.addBatch();
                        }
                        prep.executeBatch();
                        connection.commit();
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (prep != null) {
                    prep.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
