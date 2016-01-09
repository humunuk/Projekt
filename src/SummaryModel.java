import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.lang.reflect.*;
import java.lang.reflect.Array;
import java.sql.*;
import java.util.*;

/**
 * Created by humunuk on 12/26/15.
 */
public class SummaryModel {

    private Connection conn;
    private PreparedStatement prep;
    private ResultSet results;
    private int[] summaries = new int[3];
    private int[] totals = new int[3];

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
            closeConnection();
        }
        return 0;
    }


    public ObservableList<Map> fetchSubjectsBySemester(ToggleButton semester, ToggleButton year, String subjectMapKey, String yearMapKey, String eapMapKey, String typeMapKey, String idMapKey, ObservableList<Map> subjectTableData) {
            initConnection();
        //Clear old data when another button is pushed
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

        initConnection();
        List<String> oldPlans = new ArrayList<>();
        try {
            prep = conn.prepareStatement("SELECT name FROM plans;");
            ResultSet results = prep.executeQuery();
            while (results.next()) {
                oldPlans.add(results.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        return oldPlans;
    }

    public Collection fetchPlanDetails(Plan plan, ToggleButton semester, ToggleButton year, ObservableList<Map> planTableData, String subjectMapKey, String eapMapKey, String idMapKey, PlanningController planningController) {

        int planId = getPlanId(plan);

        initConnection();

        planTableData.clear();

        try {
            prep = conn.prepareStatement("SELECT plan_subjects.vota, plan_subjects.id as pid, subjects.id as subId, subjects.subject, subjects.eap FROM plan_subjects JOIN subjects ON subjects.id = plan_subjects.subject_id WHERE subjects.semester = ? AND plan_subjects.year = ? AND plan_id = ?");
            prep.setObject(1, semester.getId());
            prep.setObject(2, year.getId());
            prep.setObject(3, planId);
            results = prep.executeQuery();

            while (results.next()) {
                String subId = results.getString("subId");
                Button deleteBtn = new Button("Eemalda");
                int votaRs = results.getInt("vota");
                deleteBtn.setId(results.getString("subId"));
                deleteBtn.setOnAction(event -> {
                    removeSubjectFromPlan(year, deleteBtn.getId(), plan);
                    planningController.updatePlanningDetailTable();
                });
                CheckBox votaBtn = new CheckBox();
                votaBtn.setId(results.getString("pid"));
                votaBtn.setOnAction(event -> {
                    if (votaBtn.isSelected()) {
                        int vota = 1;
                        updateVota(vota, year, votaBtn.getId());
                        planningController.updatePlanningDetailTable();
                    } else {
                        int vota = 0;
                        updateVota(vota, year, votaBtn.getId());
                        planningController.updatePlanningDetailTable();
                    }
                });
                if (votaRs == 1) {
                    votaBtn.setSelected(true);
                } else {
                    votaBtn.setSelected(false);
                }
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

    private void updateVota(int vota, ToggleButton year, String id) {
        initConnection();
        String yearId = year.getId();
        try {
            prep = conn.prepareStatement("UPDATE plan_subjects SET vota = ? WHERE id = ? AND year = ?");
            prep.setInt(1, vota);
            prep.setObject(2, id);
            prep.setObject(3, yearId);
            prep.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void removeSubjectFromPlan(ToggleButton year, String subId, Plan plan) {

        int planId = getPlanId(plan);

        initConnection();
        String yearId = year.getId();
        try {
            prep = conn.prepareStatement("DELETE FROM plan_subjects WHERE year = ? AND subject_id = ? AND plan_id = ?");
            prep.setObject(1, yearId);
            prep.setObject(2, subId);
            prep.setObject(3, planId);
            prep.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public int[] fetchSummaries(Plan plan, Toggle year, Toggle semester) {

        ToggleButton yearBtn = (ToggleButton) year;
        ToggleButton semBtn = (ToggleButton) semester;

        int planId = getPlanId(plan);
        int mandatoryTotal = 0;
        int electiveTotal = 0;
        int votaTotal = 0;

        initConnection();

        try {
            prep = conn.prepareStatement("SELECT subjects.eap, subjects.mandatory, plan_subjects.vota  FROM  plan_subjects  JOIN subjects ON subjects.id = plan_subjects.subject_id WHERE plan_subjects.year = ? AND plan_id = ? AND subjects.semester = ?");
            prep.setObject(1, yearBtn.getId());
            prep.setObject(2, planId);
            prep.setObject(3, semBtn.getId());
            results = prep.executeQuery();
            while (results.next()) {
                if (results.getInt("mandatory") == 1 && results.getInt("vota") == 0) {
                    mandatoryTotal += results.getInt("eap");
                } else if (results.getInt("mandatory") == 2 && results.getInt("vota") == 0) {
                    electiveTotal += results.getInt("eap");
                } else if (results.getInt("vota") == 1) {
                    votaTotal += results.getInt("eap");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        summaries[0] = mandatoryTotal;
        summaries[1] = electiveTotal;
        summaries[2] = votaTotal;

        return summaries;
    }

    public ListView<String> fetchAllSummaryDataByYear(Plan plan, int i, ListView<String> subjects) {

        subjects.getItems().clear();
        int planId = getPlanId(plan);

        initConnection();

        try {
            prep = conn.prepareStatement("SELECT subjects.eap, subjects.subject, subjects.mandatory, subjects.semester, plan_subjects.year FROM subjects JOIN plan_subjects ON plan_subjects.subject_id = subjects.id WHERE plan_subjects.year = ? AND plan_id = ? ORDER BY plan_subjects.year ASC, semester ASC, mandatory ASC");
            prep.setInt(1, i);
            prep.setInt(2, planId);
            results = prep.executeQuery();
            while (results.next()) {
                subjects.getItems().add(results.getString("subject"));
            }
            getTotals(i, subjects, planId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
        return subjects;
    }

    private void getTotals(int i, ListView<String> subjects, int planId) {

        initConnection();

        int mandatoryTotal = 0;
        int electiveTotal = 0;
        int votaTotal = 0;
        int totalTotal = 0;

        try {
            prep = conn.prepareStatement("SELECT subjects.eap, subjects.mandatory, plan_subjects.vota  FROM  plan_subjects  JOIN subjects ON subjects.id = plan_subjects.subject_id WHERE plan_subjects.year = ? AND plan_id = ?");
            prep.setInt(1, i);
            prep.setInt(2, planId);
            results = prep.executeQuery();
            while (results.next()) {
                if (results.getInt("mandatory") == 1 && results.getInt("vota") == 0) {
                    mandatoryTotal += results.getInt("eap");
                } else if (results.getInt("mandatory") == 2 && results.getInt("vota") == 0) {
                    electiveTotal += results.getInt("eap");
                } else if (results.getInt("vota") == 1) {
                    votaTotal += results.getInt("eap");
                }
                totalTotal += results.getInt("eap");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
        String mandatory = "Kohustuslikud: "+mandatoryTotal;
        String elective = "Valikained: "+electiveTotal;
        String vota = "VÕTA: "+votaTotal;
        String total = "Kokku: "+totalTotal;

        subjects.getItems().add("");
        subjects.getItems().add(mandatory);
        subjects.getItems().add(elective);
        subjects.getItems().add(vota);
        subjects.getItems().add(total);

    }

    public int[] fetchTotalSummaries(Plan plan) {

        int planId = getPlanId(plan);
        int mandatoryTotal = 0;
        int electiveTotal = 0;
        int votaTotal = 0;

        initConnection();

        try {
            prep = conn.prepareStatement("SELECT subjects.eap, subjects.mandatory, plan_subjects.vota  FROM  plan_subjects  JOIN subjects ON subjects.id = plan_subjects.subject_id WHERE plan_id = ?");
            prep.setObject(1, planId);
            results = prep.executeQuery();
            while (results.next()) {
                if (results.getInt("mandatory") == 1 && results.getInt("vota") == 0) {
                    mandatoryTotal += results.getInt("eap");
                } else if (results.getInt("mandatory") == 2 && results.getInt("vota") == 0) {
                    electiveTotal += results.getInt("eap");
                } else if (results.getInt("vota") == 1) {
                    votaTotal += results.getInt("eap");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        totals[0] = mandatoryTotal;
        totals[1] = electiveTotal;
        totals[2] = votaTotal;

        return totals;
    }
}
