package groupproject545;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 *
 * @author Ethan_2
 */
public class MealPlanGUI extends javax.swing.JPanel {

    Connection conn = null;
    OraclePreparedStatement pst = null;
    OracleResultSet rs = null;

    /**
     * Creates new form MealPlanGUI
     */
    public MealPlanGUI() {
        // Initialize components. Do not attempt to use GUI components (buttons, labels, etc.) until this has been done.
        initComponents();

        // By default, no messages to show.
        messageLabel.setVisible(false);

        // Display this week's plan.
        displayThisWeeksPlan();

        // Place all meal plans in the drop-down menu.
        getMealPlans();

    }

    private void getMealPlans() {
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALPLAN order by TITLE";

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            if (!rs.next()) {  // No rows returned from the query - hide the combo box, label, and jtable.
                mealPlanComboBox.setVisible(false);
                // Hide the JPanel that encapsulates the scheduleTable
                scheduleHoldingPanel.setVisible(false);
                selectPlanLabel.setVisible(false);

                // Let the user know there are not any meal plans.
                messageLabel.setText("You currently do not have any meal plans.");
                messageLabel.setVisible(true);
            } else {
                // Place all the meal plans in the drop down menu.
                while (rs.next()) {
                    String name = rs.getString("TITLE");
                    mealPlanComboBox.addItem(name);
                }

                // -------------------------------------------------------------
                // ----- Now, find out which meal plan is active this week -----
                // (We already know there are meal plans in the database if the code got to this point.)
                // First, get today's date.
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  //Put it in SQL format.
                String sqlDate = sdf.format(date);  // Turn it into a string.

                try {
                    String sqlStatement2 = "select * from MEALPLAN where NEXTOCCURRENCE = '" + sqlDate + "'";

                    pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement2);

                    rs = (OracleResultSet) pst.executeQuery();
                    if (!rs.next()) {  // No meal plan scheduled for this week. Hide the jtable.
                        // Hide the JPanel that encapsulates the scheduleTable
                        scheduleHoldingPanel.setVisible(false);

                        // Let the user know there are not any meal plans.
                        messageLabel.setText("There is not an active meal plan for this week. Click on \"Edit Plan\" to configure your meal plan schedule.");
                        messageLabel.setVisible(true);
                    } else {
                        // Place all meals in the jtable.
                        while (rs.next()) {
                            
                            // There should be only one meal plan active per week, so we can exit after just one iteration.
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(null, e);
                } 
            }
        } catch (Exception e) {
            System.out.println(e);
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

        mealPlanComboBox = new javax.swing.JComboBox<>();
        selectPlanLabel = new javax.swing.JLabel();
        addMealPlanButton = new javax.swing.JButton();
        configureScheduleButton = new javax.swing.JButton();
        editMealPlanButton = new javax.swing.JButton();
        scheduleHoldingPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        messageLabel = new javax.swing.JLabel();

        mealPlanComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        mealPlanComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealPlanComboBoxActionPerformed(evt);
            }
        });

        selectPlanLabel.setText("Select Plan:");

        addMealPlanButton.setText("Add a Meal Plan");

        configureScheduleButton.setText("Configure Schedule");

        editMealPlanButton.setText("Edit Plan");

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
            }
        ));
        jScrollPane1.setViewportView(scheduleTable);

        javax.swing.GroupLayout scheduleHoldingPanelLayout = new javax.swing.GroupLayout(scheduleHoldingPanel);
        scheduleHoldingPanel.setLayout(scheduleHoldingPanelLayout);
        scheduleHoldingPanelLayout.setHorizontalGroup(
            scheduleHoldingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleHoldingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 672, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        scheduleHoldingPanelLayout.setVerticalGroup(
            scheduleHoldingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scheduleHoldingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        messageLabel.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        messageLabel.setText("You currently do not have any meal plans.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(editMealPlanButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addMealPlanButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(configureScheduleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 503, Short.MAX_VALUE)
                        .addComponent(selectPlanLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mealPlanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(scheduleHoldingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(258, 258, 258)
                .addComponent(messageLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mealPlanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectPlanLabel)
                    .addComponent(addMealPlanButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(configureScheduleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editMealPlanButton)
                .addGap(26, 26, 26)
                .addComponent(messageLabel)
                .addGap(18, 18, 18)
                .addComponent(scheduleHoldingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mealPlanComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealPlanComboBoxActionPerformed
        // Show the newly selected meal plan.

    }//GEN-LAST:event_mealPlanComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMealPlanButton;
    private javax.swing.JButton configureScheduleButton;
    private javax.swing.JButton editMealPlanButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> mealPlanComboBox;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JPanel scheduleHoldingPanel;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JLabel selectPlanLabel;
    // End of variables declaration//GEN-END:variables

}