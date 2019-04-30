package groupproject545;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;

/**
 *
 * @author Ethan_2
 */
public class MealPlanGUI extends javax.swing.JPanel {

    Connection conn = null;
    OraclePreparedStatement pst = null;
    OracleResultSet rs = null;
    OracleStatement stmt = null;

    public static DefaultTableModel model = null;  // used for adding/removing rows and editing cells in the schedule table

    public List<String> mealPlans = new ArrayList<String>();
    public List<MealDays> mealDays = new ArrayList<>();
    TableModel mealTableModel = null;

    /**
     * Creates new form MealPlanGUI
     */
    public MealPlanGUI(JFrame frame) {
        // Initialize components. Do not attempt to use GUI components (buttons, labels, etc.) until this has been done.
        initComponents();

        // By default, no messages to show.
        messageLabel.setVisible(false);

        // Display this week's plan.
        displayThisWeeksPlan();

        // Place all meal plans in the drop-down menu.
        getMealPlans();

        // Below is a custom designed close operation for the MealPlanGUI screen.
        // When the meal planner jframe is closed, reopen a new instance of the
        // WelcomeScreen.
        WindowListener exitListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Show the meal plans form.
                JFrame welcomeScreen = new JFrame("Welcome");

                // Maximize the size of the jframe.
                //welcomeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
                welcomeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exits the program after the JFrame is closed by the user.
                welcomeScreen.add(new WelcomeScreenGUI());
                welcomeScreen.pack();
                welcomeScreen.setLocationRelativeTo(null);
                welcomeScreen.setVisible(true);
            }
        };
        frame.addWindowListener(exitListener);  // Add the custom designed listener.

        // Maximize the size of the jframe.
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        model = (DefaultTableModel) scheduleTable.getModel();
    }

    private void getMealPlans() {
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALPLAN order by title";

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            if (!rs.next()) {  // No rows returned from the query - hide the combo box, label, and jtable.
                // Hide the JPanel that encapsulates the scheduleTable
                schedulingPanel.setVisible(false);
                configureScheduleButton.setVisible(false);
                // Let the user know there are not any meal plans.
                messageLabel.setText("You currently do not have any meal plans.");
                messageLabel.setVisible(true);

            } else {
                // Place all the meal plans in the drop down menu.
                do {
                    String name = rs.getString(1);
                    mealPlans.add(name);
                    mealPlanComboBox.addItem(name);
                } while (rs.next());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    private void displayThisWeeksPlan() {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMealPlanButton = new javax.swing.JButton();
        configureScheduleButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        schedulingPanel = new javax.swing.JPanel();
        selectPlanLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        mealPlanComboBox = new javax.swing.JComboBox<>();
        editMealPlanButton = new javax.swing.JButton();
        deleteMealPlanButton = new javax.swing.JButton();

        addMealPlanButton.setText("Add a Meal Plan");
        addMealPlanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMealPlanButtonActionPerformed(evt);
            }
        });

        configureScheduleButton.setText("Configure Schedule");
        configureScheduleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                configureScheduleButtonActionPerformed(evt);
            }
        });

        messageLabel.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        messageLabel.setText("You currently do not have any meal plans.");

        selectPlanLabel.setText("Select Plan:");

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ));
        jScrollPane2.setViewportView(scheduleTable);

        mealPlanComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealPlanComboBoxActionPerformed(evt);
            }
        });

        editMealPlanButton.setText("Edit Plan");
        editMealPlanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMealPlanButtonActionPerformed(evt);
            }
        });

        deleteMealPlanButton.setText("Delete Plan");
        deleteMealPlanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMealPlanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout schedulingPanelLayout = new javax.swing.GroupLayout(schedulingPanel);
        schedulingPanel.setLayout(schedulingPanelLayout);
        schedulingPanelLayout.setHorizontalGroup(
            schedulingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulingPanelLayout.createSequentialGroup()
                .addGap(292, 292, 292)
                .addComponent(selectPlanLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mealPlanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(editMealPlanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(311, 311, 311))
            .addGroup(schedulingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2))
            .addGroup(schedulingPanelLayout.createSequentialGroup()
                .addGap(443, 443, 443)
                .addComponent(deleteMealPlanButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        schedulingPanelLayout.setVerticalGroup(
            schedulingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(schedulingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(schedulingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectPlanLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mealPlanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editMealPlanButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(87, 87, 87)
                .addComponent(deleteMealPlanButton)
                .addGap(81, 81, 81))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(addMealPlanButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(configureScheduleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(118, 118, 118)
                .addComponent(messageLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(schedulingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addMealPlanButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(configureScheduleButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(messageLabel)))
                .addGap(94, 94, 94)
                .addComponent(schedulingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mealPlanComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealPlanComboBoxActionPerformed
        // Show the newly selected meal plan, if there are meals to show.
        if (mealPlans.size() > 0) {
            int selectedIndex = mealPlanComboBox.getSelectedIndex();
            String mealPlanName = mealPlans.get(selectedIndex);
            try {
                mealDays = MealDays.getByMealPlanTitle(mealPlanName);
                displayMeals();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
            }
        }

    }//GEN-LAST:event_mealPlanComboBoxActionPerformed

    private void displayMeals() {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        List<String[]> data = new ArrayList<String[]>();
        // for each day of the week
        for (int i = 0; i < daysOfWeek.length; i++) {
            // sets the working row
            int row = 0;
            for (MealDays mealDay : mealDays) {
                // if day of week matches
                if (daysOfWeek[i].equals(mealDay.getDayOfWeek())) {
                    // if a row needs to be added
                    if (row == data.size()) {
                        data.add(new String[7]); // add row
                    }                    // set row values
                    data.get(row)[i] = mealDay.getMealTitle() + ": " + mealDay.getMealName();
//                    scheduleTable.getModel().setValueAt(data.get(row)[i], row, i);
                    System.out.println(data.get(row)[i]);
                    row++;
                }
            }
        }
        // create a table model with the new values
        mealTableModel = new DefaultTableModel(data.toArray(new String[0][]), daysOfWeek);
        scheduleTable.setModel(mealTableModel);

    }

    private List<String> getMeals() {
        List<String> meals = new ArrayList<String>();
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALS order by name";  // Get all meals, sorting by name.

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                meals.add(rs.getString(1));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return meals;
    }


    private void addMealPlanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMealPlanButtonActionPerformed
        // First, make sure there are meals to add.
        List<String> meals = new ArrayList<String>();
        meals = getMeals();

        if (meals.size() > 0) {
            // Go to screen where user can add a meal plan.
            JFrame frame = new JFrame("Adding a Plan");

            AddMealPlanGUI addMealPlan = new AddMealPlanGUI(meals, null, frame);
            ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the meal plan screen.
        } else if (meals.size() == 0) {  // no meals, so don't allow user to create meal plan
            JOptionPane.showMessageDialog(this,
                    "No meals to choose from. You must add meals before you can create meal plans.",
                    "Cannot Create Meal Plan",
                    JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_addMealPlanButtonActionPerformed

    private void editMealPlanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMealPlanButtonActionPerformed
        String mealPlanTitle = mealPlanComboBox.getSelectedItem().toString();

        // get meals
        List<String> meals = new ArrayList<String>();
        meals = getMeals();

        // Go to screen where user can add a meal plan.
        JFrame frame = new JFrame("Editing Meal Plan");

        AddMealPlanGUI addMealPlan = new AddMealPlanGUI(meals, mealPlanTitle, frame);
        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the meal plan screen.
    }//GEN-LAST:event_editMealPlanButtonActionPerformed

    private void configureScheduleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureScheduleButtonActionPerformed
        // Show the schedule editor screen.
        JFrame frame = new JFrame("Schedule Editor");
        ConfigureScheduleGUI configureScheduleGUI = new ConfigureScheduleGUI(frame, mealPlans);
        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the meal plan screen.
    }//GEN-LAST:event_configureScheduleButtonActionPerformed

    private void deleteMealPlanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMealPlanButtonActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the meal plan?", "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // yes option        
            // Delete the meal plan from the database.
            try {
                String mpTitle = mealPlanComboBox.getSelectedItem().toString();
                String sqlDeleteStmt = "delete from MEALPLAN where title = '" + mpTitle.replace("'", "''") + "'";
                System.out.println("sqlDeleteFrom mealplan for = " + sqlDeleteStmt);
                conn = ConnectDb.setupConnection();
                stmt = (OracleStatement) conn.createStatement();
                stmt.execute(sqlDeleteStmt);

                // Delete from the data structure.
                mealPlans.remove(mpTitle);

                if (mealPlans.size() == 0) {  // no more meal plans to show
                    schedulingPanel.setVisible(false);
                    messageLabel.setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            } finally {
                try {  // Try closing the connection and the statement.
                    conn.close();
                    stmt.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
                }
            }
        }
    }//GEN-LAST:event_deleteMealPlanButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMealPlanButton;
    private javax.swing.JButton configureScheduleButton;
    private javax.swing.JButton deleteMealPlanButton;
    private javax.swing.JButton editMealPlanButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> mealPlanComboBox;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JPanel schedulingPanel;
    private javax.swing.JLabel selectPlanLabel;
    // End of variables declaration//GEN-END:variables

}
