/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject545;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;

/**
 *
 * @author EKUStudent
 */
public class AddMealPlanGUI extends javax.swing.JPanel {

    /**
     * Creates new form AddMealPlanGUI
     */
    // mealPlan is an instance of the MealDays class that will allow us to use the methods contained in that class
    public MealDays mealPlan = null;

    // mealDays will hold all the mealDays that need inserted into the database 
    public List<MealDays> mealDays = new ArrayList<MealDays>();

    // keeps up with the meal plan title being passed to the class (if in edit mode)
    public String mealPlanTitleOld = null;

    // holds all the meals in the database (used to initialize the combo box of meals
    public List<String> meals = new ArrayList<String>();

    public OraclePreparedStatement pst = null;
    public Connection conn = null;
    public OracleResultSet rs = null;
    public OracleStatement stmt = null;

    // EDIT_MODE: determines if we are editing an old meal plan or creating a new meal plan
    public final boolean EDIT_MODE;

    public DefaultListModel listModel = new DefaultListModel();

    public AddMealPlanGUI(List<String> meals, String mealPlanTitle, JFrame frame) {
        initComponents();

        // Maximize the size of the jframe.
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Below is custom close operation.
        WindowListener exitListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Show the meal plans form.
                JFrame frame = new JFrame("Meal Plans");

                MealPlanGUI mealPlanGUI = new MealPlanGUI(frame);
            }
        };
        frame.addWindowListener(exitListener);  // Add the custom designed listener.

        mealsList.setModel(listModel);

        if (mealPlanTitle != null) {  // We are editing an old plan.            
            mealPlanTitleOld = mealPlanTitle;
            mealPlan = new MealDays(mealPlanTitle);
            mealPlanTitleTextField.setText(mealPlanTitle);
            EDIT_MODE = true;
            submitButton.setText("Submit Changes");

            // get all meals and days already in the database for this mealplan and place them into the jlist
            fetchMealDaysForExistingMealPlan(mealPlanTitle);
            putMealDaysIntoList();
        } else {  // Creating a new plan.
            EDIT_MODE = false;
            submitButton.setText("Submit Meal Plan");
        }

        // NOthing selected by default - so hide modify and delete buttons.
        deleteButton.setVisible(false);

        // get the meals and place them in the meals combo box
        this.meals = meals;
        fillMealsComboBox();
    }

    private void putMealDaysIntoList() {
        for (int i = 0; i < mealDays.size(); i++) {
            String mealName = mealDays.get(i).getMealName();
            String dayOfWeek = mealDays.get(i).getDayOfWeek();
            String mealTitle = mealDays.get(i).getMealTitle();
            listModel.addElement(mealTitle + " on " + dayOfWeek + ": " + mealName);
        }
    }

    private void fetchMealDaysForExistingMealPlan(String mealPlanTitle) {
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALDAY where mealPlanTitle = '" + mealPlanTitle + "'";  // Get all meal days
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String dayOfWeek = rs.getString(1);
                String mealTitle = rs.getString(2);
                String mealName = rs.getString(3);
                MealDays md = new MealDays(mealTitle, mealName, dayOfWeek);
                mealDays.add(md);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    private void fillMealsComboBox() {
        mealNamesComboBox.removeAllItems();
        for (int i = 0; i < meals.size(); i++) {
            mealNamesComboBox.addItem(meals.get(i));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        submitButton = new javax.swing.JButton();
        daysOfWeekComboBox = new javax.swing.JComboBox<>();
        mealTitleTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        mealNamesComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        mealsList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        mealPlanTitleTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        addMealButton = new javax.swing.JButton();

        submitButton.setText("Submit Meal Plan");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        daysOfWeekComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" }));

        mealTitleTextField.setText("Breakfast, brunch, lunch, etc.");
        mealTitleTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                mealTitleTextFieldFocusLost(evt);
            }
        });
        mealTitleTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mealTitleTextFieldMouseClicked(evt);
            }
        });

        jLabel1.setText("Served during:");

        mealNamesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        mealsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                mealsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(mealsList);

        jLabel2.setText("Meal:");

        jLabel3.setText("My Meals in this Plan");

        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("Meal Plan Title:");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        addMealButton.setText("Add Meal");
        addMealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMealButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mealNamesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(mealPlanTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(daysOfWeekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addMealButton)
                            .addComponent(mealTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(192, 192, 192))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton)
                        .addGap(399, 399, 399))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(deleteButton)
                        .addGap(191, 191, 191))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(mealPlanTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(daysOfWeekComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(mealNamesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mealTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(addMealButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(deleteButton)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(submitButton))
                .addGap(58, 58, 58))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void mealsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_mealsListValueChanged
        // Hide modify button if only item is selected.
        if (mealsList.getSelectedIndices().length == 1) {  // Allow deletion of one or more meals.
            deleteButton.setVisible(true);
        } else {  // Zero or more than one items selected
            deleteButton.setVisible(false);
        }
    }//GEN-LAST:event_mealsListValueChanged

    private boolean stringNotEmpty(String mealPlanName) {
        if (mealPlanName.equals("") || mealPlanName == null) {
            return false;
        }
        return true;
    }

    private boolean mealPlanNameDoesNotExist(String mealPlanTitle, String oldMealPlanTitle) {
        // If editing a current meal plan, then don't check that that meal plan title already exists (obviously, it does exist - otherwise we wouldn't be editing it)
        // But, if adding a new meal plan, then make sure that the name doesn't exist.
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALPLAN where title = '" + mealPlanTitle + "'";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            if (rs.next()) {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
                if (EDIT_MODE && oldMealPlanTitle.equals(mealPlanTitle)) {  // checked out ok - meal plan title unchanged
                    return true;
                } else {  // There are results from the query (otherwise we wouldn't be in this if statement), therefore the meal plan title does exist in the db
                    return false;
                }
            } else {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
                return true;
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        JOptionPane.showMessageDialog(this,
                "There was a problem checking if the meal plan title exists or not.",
                "Error!",
                JOptionPane.ERROR_MESSAGE);

        return false;
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed

        //  Go back to the meal plan screen without submitting changes to the database.
        JFrame frame = new JFrame("My Meal Plans");

        MealPlanGUI mealPlanGUI = new MealPlanGUI(frame);

        // Close the frame, don't submit changes.
        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addMealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMealButtonActionPerformed
        // make sure the meal name is not empty
        if (stringNotEmpty(mealTitleTextField.getText())) {
            // now make sure that this specific meal name, with this specific meal plan, on this specific day does not already exist in the list
            String mealName = mealNamesComboBox.getSelectedItem().toString();
            String mealTitle = mealTitleTextField.getText();
            String dayOfWeek = daysOfWeekComboBox.getSelectedItem().toString();
            MealDays md = new MealDays(mealTitle, mealName, dayOfWeek);
            if (!mealDayDoesNotExist(md)) {
                mealDays.add(md);  // append to the list
                listModel.addElement(mealTitle + " on " + dayOfWeek + ": " + mealName);
            } else {
                JOptionPane.showMessageDialog(this,
                        "That meal-day for this meal plan already has a scheduled meal!",
                        "Meal Not Added",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Must be served at a specific meal (breakfast, lunch, snack, etc.)",
                    "Meal Not Added",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_addMealButtonActionPerformed

    private boolean mealDayDoesNotExist(MealDays md) {
        for (MealDays mealDay : mealDays) {
            if (mealDay.getDayOfWeek().equals(md.getDayOfWeek()) && mealDay.getMealTitle().equals(md.getMealTitle())) {
                // this meal already exists in the list
                return true;
            }
        }
        // passed all the checks - meal does not exist
        return false;
    }


    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        String mealPlanTitle = mealPlanTitleTextField.getText();
        if (mealPlanNameDoesNotExist(mealPlanTitle, mealPlanTitleOld) && stringNotEmpty(mealPlanTitleTextField.getText())) {
            if (EDIT_MODE) {
                // First, delete everything for this meal plan from the mealday table, and reinsert anything added during editing or already present
                try {
                    String sqlDeleteStmt = "delete from MEALDAY where mealPlanTitle = '" + mealPlanTitleOld + "'";
                    conn = ConnectDb.setupConnection();
                    pst = (OraclePreparedStatement) conn.prepareStatement(sqlDeleteStmt);
                    pst.execute(sqlDeleteStmt);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);  // Show the exception message.
                } finally {
                    try {  // Try closing the connection and the statement.
                        conn.close();
                        pst.close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
                    }
                }

                // update the mealplan table
                try {
                    String sqlUpdateStmt = "update MEALPLAN set title = '" + mealPlanTitle + "' where title = '" + mealPlanTitleOld + "'";
                    conn = ConnectDb.setupConnection();
                    pst = (OraclePreparedStatement) conn.prepareStatement(sqlUpdateStmt);
                    pst.executeUpdate();
                    System.out.println("finished update of meal plan");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);  // Show the exception message.
                } finally {
                    try {  // Try closing the connection and the statement.
                        conn.close();
                        pst.close();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
                    }
                }

                // submit new meal days into the mealday table
                insertIntoMealDay(mealPlanTitle);

                // done editing - now set the old meal plan title to the one just submitted (do this because user will remain on this screen and can edit it again)
                mealPlanTitleOld = mealPlanTitle;
            } else {
                // first, insert into the MEALPLAN table
                String title = mealPlanTitleTextField.getText();
                String nextOccurrence = "1900-01-01";  // default next occurrence date in the past
                insertIntoMealPlan(title, nextOccurrence);

                // now insert into the MEALDAY table
                insertIntoMealDay(title);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Check that you have entered a meal plan title and that it does not already exist.",
                    "Meal Plan Not Added",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void insertIntoMealDay(String mealPlanTitle) {
        conn = ConnectDb.setupConnection();
        try {
            stmt = (OracleStatement) conn.createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
        }
        for (int i = 0; i < mealDays.size(); i++) {
            try {
                String sqlInsertStmt = "insert into MEALDAY values ('" + mealDays.get(i).getDayOfWeek() + "', '" + mealDays.get(i).getMealTitle() + "', '" + mealDays.get(i).getMealName() + "', '" + mealPlanTitle + "')";

                stmt.execute(sqlInsertStmt);
                if (i == mealDays.size() - 1) {  // clean up the textfields and show success if we're not editing (if editing, leave textfields alone)
                    if (!EDIT_MODE) {
                        mealPlanTitleTextField.setText("");
                        // remove all the meals added for this meal plan
                        listModel.removeAllElements();
                        mealDays.clear();
                    }
                    mealTitleTextField.setText("Breakfast, brunch, lunch, etc.");

                    // indicate success
                    JOptionPane.showMessageDialog(this,
                            "The new meal plan has been successfully added/changed!",
                            "Success!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            }
        }
        try {  // Try closing the connection and the statement.
            conn.close();
            stmt.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
        }
    }

    private void insertIntoMealPlan(String title, String nextOccurrence) {
        try {
            String sqlInsertStmt = "insert into MEALPLAN values ('" + title + "', to_date('" + nextOccurrence + "', 'YYYY-MM-DD'))";
            conn = ConnectDb.setupConnection();
            stmt = (OracleStatement) conn.createStatement();
            stmt.executeUpdate(sqlInsertStmt);
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

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int selectedIndex = mealsList.getSelectedIndex();
        listModel.remove(selectedIndex);
        mealDays.remove(selectedIndex);
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void mealTitleTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mealTitleTextFieldMouseClicked
        // Set the text to null if the user clicks into the text field and nothing has been entered yet.
        if (mealTitleTextField.getText().equals("Breakfast, brunch, lunch, etc.")) {
            mealTitleTextField.setText("");
        }
    }//GEN-LAST:event_mealTitleTextFieldMouseClicked

    private void mealTitleTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mealTitleTextFieldFocusLost
       if (mealTitleTextField.getText().equals("")) {  // reset to default text
           mealTitleTextField.setText("Breakfast, brunch, lunch, etc.");
       }
    }//GEN-LAST:event_mealTitleTextFieldFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMealButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox<String> daysOfWeekComboBox;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> mealNamesComboBox;
    private javax.swing.JTextField mealPlanTitleTextField;
    private javax.swing.JTextField mealTitleTextField;
    private javax.swing.JList<String> mealsList;
    private javax.swing.JButton submitButton;
    // End of variables declaration//GEN-END:variables
}
