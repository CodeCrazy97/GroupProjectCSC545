/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject545;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
 * @author Ethan_2
 */
public class WelcomeScreenGUI extends javax.swing.JPanel {

    public Connection conn = null;
    public OraclePreparedStatement pst = null;
    public OracleResultSet rs = null;
    public OracleStatement stmt = null;

    public String mealPlanTitle = "";
    public String nextWeekMealPlanTItle = "";

    //model allows us to add items to the out of stock ingredients jlist
    DefaultListModel model = new DefaultListModel();
    public List<String> outOfStockIngredients = new ArrayList<String>();

    /**
     * Creates new form WelcomeScreenGUI
     */
    public WelcomeScreenGUI() {
        initComponents();

        // Make the jlist be used by model.
        outOfStockIngredientsList.setModel(model);

        // by default, no ingredient is selected, so hide the option to set an ingredient to inStock
        removeFromShoppingListButton.setVisible(false);

        // Find out which meal plan is scheduled for this week.
        getCurrentMealPlan();

        // Get the meals scheduled for this week.
        showMealsForCurrentWeek();

        // Get shopping list for the following week.
        generateShoppingList();

    }

    private void generateShoppingList() {
        conn = ConnectDb.setupConnection();
        try {
            String dateSQLFormatted = getCurrentDateInYYYYMMDDFormat();

            // Below query returns all ingredients that are out of stock for next week's mealplan
            String sqlStatement = "select distinct i.name from ingredients i, recipes r, callsfor cf, servedDuringMeal sdm, meals m, mealDay md, mealplan mp "
                    + "where md.mealPlanTitle = mp.title and m.name = md.mealName and sdm.mealName = m.name and sdm.recipeTitle = r.title "
                    + "and cf.recipeTitle = r.title and cf.ingredientName = i.name and i.inStock = 'N' and mp.nextOccurrence "
                    + "between (select next_day(to_date('" + dateSQLFormatted + "','YYYY-MM-DD'),'SUN') from dual) "
                    + "and (select next_day(to_date('" + dateSQLFormatted + "', 'YYYY-MM-DD'),'SUN') + 6 from dual)";

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery(sqlStatement);
            if (rs.next()) {
                do {
                    outOfStockIngredients.add(rs.getString(1));
                    model.addElement(rs.getString(1));
                } while (rs.next());
            } else {
                jLabel1.setText("Nothing on the shopping list for next week.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    public void showMealsForCurrentWeek() {
        if (mealPlanTitle != null && !mealPlanTitle.equals("")) {
            conn = ConnectDb.setupConnection();
            try {
                String sqlStatement = "select md.dayOfWeek, md.mealTitle, sdm.recipeTitle "
                        + "from servedduringmeal sdm join (select mealTitle, mealname, dayOfWeek "
                        + "from mealday where mealplantitle = '" + mealPlanTitle.replace("'", "''") + "') md "
                        + "on md.mealname = sdm.mealname";

                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                rs = (OracleResultSet) pst.executeQuery(sqlStatement);
                List<String> daysShown = new ArrayList<String>();
                List<String> mealTitlesShown = new ArrayList<String>();
                while (rs.next()) {
                    String dayOfWeek = rs.getString(1);
                    String mealTitle = rs.getString(2);
                    String recipe = rs.getString(3);
                    if (!daysShown.contains(dayOfWeek)) {  // day not already listed
                        todaysMealsTextArea.append("________________\n" + dayOfWeek.toUpperCase());
                        daysShown.add(dayOfWeek);
                    }
                    if (!mealTitlesShown.contains(mealTitle + dayOfWeek)) {  // This meal (lunch, dinner, snack, etc.) on this specific day (Sunday, Monday, etc.) not already shown
                        todaysMealsTextArea.append("\n\n" + mealTitle + "\n");
                        mealTitlesShown.add(mealTitle + dayOfWeek);
                    }

                    // always show recipes
                    todaysMealsTextArea.append(recipe + "\n");

                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
            } finally {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
            }
        } else {
            dailyMealLabel.setText("No Meal Plan Scheduled for This Week");
        }
    }

    // Finds the mealplan scheduled for the current week.
    public void getCurrentMealPlan() {
        String dateSQLFormatted = getCurrentDateInYYYYMMDDFormat();
        conn = ConnectDb.setupConnection();
        try {
            // Below query gets meal plan with next occurrence scheduled this week.
            String sqlStatement = "select * from MEALPLAN where nextOccurrence "
                    + "between (select next_day(to_date('" + dateSQLFormatted + "','YYYY-MM-DD'),'SUN') - 7 from dual) "
                    + "and (select next_day(to_date('" + dateSQLFormatted + "','YYYY-MM-DD'),'SUN') - 1 from dual)";

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery(sqlStatement);
            while (rs.next()) {
                mealPlanTitle = rs.getString(1);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);  // Show the exception message.
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    public String getCurrentDateInYYYYMMDDFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getStartDate() {
        LocalDate today = LocalDate.now();

        // Go backward to get Monday
        LocalDate monday = today;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = today;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.minusDays(1);
        }

        return sunday.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ingredientsButton = new javax.swing.JButton();
        recipesButton = new javax.swing.JButton();
        mealsButton = new javax.swing.JButton();
        mealPlansButton = new javax.swing.JButton();
        dailyMealLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        todaysMealsTextArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        outOfStockIngredientsList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        removeFromShoppingListButton = new javax.swing.JButton();

        ingredientsButton.setText("Ingredients");
        ingredientsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsButtonActionPerformed(evt);
            }
        });

        recipesButton.setText("Recipes");
        recipesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recipesButtonActionPerformed(evt);
            }
        });

        mealsButton.setText("Meals");
        mealsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealsButtonActionPerformed(evt);
            }
        });

        mealPlansButton.setText("Meal Plans");
        mealPlansButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealPlansButtonActionPerformed(evt);
            }
        });

        dailyMealLabel.setText("This Week's Meals");

        todaysMealsTextArea.setEditable(false);
        todaysMealsTextArea.setColumns(20);
        todaysMealsTextArea.setRows(5);
        jScrollPane1.setViewportView(todaysMealsTextArea);

        outOfStockIngredientsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                outOfStockIngredientsListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(outOfStockIngredientsList);

        jLabel1.setText("Next Week's Shopping List");

        removeFromShoppingListButton.setText("Remove From List");
        removeFromShoppingListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromShoppingListButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ingredientsButton)
                                .addGap(17, 17, 17)
                                .addComponent(recipesButton)
                                .addGap(18, 18, 18)
                                .addComponent(mealsButton)
                                .addGap(18, 18, 18)
                                .addComponent(mealPlansButton))
                            .addComponent(dailyMealLabel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2))
                                .addGap(154, 154, 154))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(169, 169, 169)
                                .addComponent(removeFromShoppingListButton)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dailyMealLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(removeFromShoppingListButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ingredientsButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(mealPlansButton)
                        .addComponent(recipesButton)
                        .addComponent(mealsButton)))
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mealPlansButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealPlansButtonActionPerformed

        // Show the meal plans form.
        JFrame frame = new JFrame("My Meal Planner");

        MealPlanGUI mealPlans = new MealPlanGUI(frame);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the welcome menu.
    }//GEN-LAST:event_mealPlansButtonActionPerformed

    private void ingredientsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsButtonActionPerformed

        // Show the meal plans form.
        JFrame frame = new JFrame("My Ingredients");

        // Show the ingredients form.
        IngredientsGUI ingredientsScreen = new IngredientsGUI(frame);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the welcome menu.
    }//GEN-LAST:event_ingredientsButtonActionPerformed

    private void recipesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recipesButtonActionPerformed

        // Show the recipes form.
        JFrame frame = new JFrame("My Recipes");

        RecipesGUI recipes = new RecipesGUI(frame);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the welcome menu.

    }//GEN-LAST:event_recipesButtonActionPerformed

    private void mealsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealsButtonActionPerformed
        // show the meals screen
        JFrame frame = new JFrame("My Meals");

        MealsGUI meals = new MealsGUI(frame);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the welcome menu.
    }//GEN-LAST:event_mealsButtonActionPerformed

    private void removeFromShoppingListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromShoppingListButtonActionPerformed
        // Try to change the ingredient's status.        
        int selectedIngredients[] = outOfStockIngredientsList.getSelectedIndices();
        for (int i = 0; i < selectedIngredients.length; i++) {
            try {
                String sqlUpdateStmt = "update INGREDIENTS set inStock = 'Y' where name = '"
                        + outOfStockIngredientsList.getModel().getElementAt(i).replace("'", "''") + "'";
                outOfStockIngredients.remove(i);

                conn = ConnectDb.setupConnection();
                stmt = (OracleStatement) conn.createStatement();
                stmt.executeUpdate(sqlUpdateStmt);
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

        // remove the selected ingredients from the jlist
        for (int i = 0; i < selectedIngredients.length; i++) {
            model.remove(i);
        }

        if (outOfStockIngredients.size() == 0) {
            jLabel1.setText("Nothing on the shopping list for next week.");
        }

    }//GEN-LAST:event_removeFromShoppingListButtonActionPerformed

    private void outOfStockIngredientsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_outOfStockIngredientsListValueChanged
        if (outOfStockIngredientsList.getSelectedIndices().length > 0) {  // unhide the delete button
            removeFromShoppingListButton.setVisible(true);
        } else {  // hide the delete button
            removeFromShoppingListButton.setVisible(false);
        }
    }//GEN-LAST:event_outOfStockIngredientsListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dailyMealLabel;
    private javax.swing.JButton ingredientsButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton mealPlansButton;
    private javax.swing.JButton mealsButton;
    private javax.swing.JList<String> outOfStockIngredientsList;
    private javax.swing.JButton recipesButton;
    private javax.swing.JButton removeFromShoppingListButton;
    private javax.swing.JTextArea todaysMealsTextArea;
    // End of variables declaration//GEN-END:variables
}
