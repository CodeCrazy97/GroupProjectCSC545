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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 *
 * @author Ethan_2
 */
public class MealsGUI extends javax.swing.JPanel {

    static Connection conn = null;
    static OraclePreparedStatement pst = null;
    static OracleResultSet rs = null;
    static Statement stmt = null;

    public List<String> newUsedRecipes = new ArrayList<String>();    // will be used for deleting items from SERVEDDURINGMEAL
    public List<String> newUnUsedRecipes = new ArrayList<String>();  // will be used for deleting items from SERVEDDURINGMEAL

    public List<String> usedRecipes = new ArrayList<String>();
    public List<String> unUsedRecipes = new ArrayList<String>();
    public List<String> myMeals = new ArrayList<String>();

    /**
     * Creates new form MealsGUI
     */
    public MealsGUI(JFrame frame) {
        initComponents();

        WindowListener exitListener = new WindowAdapter() {  // Create custom closing event.
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

        frame.add(this);  // Pass the current instance of the MealsGUI class to the frame.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Fetch meals from the database. Populate the combo boxes.
        getMeals();
        if (myMeals.size() > 0) {
            putMealsInComboBox();
        } else {  // Hide almost everything.
            deleteMealButton.setVisible(false);
            mealsComboBox.setVisible(false);
            myMealsLabel.setVisible(false);
            usedRecipesLabel.setVisible(false);
            usedRecipesComboBox.setVisible(false);
            editMealButton.setVisible(false);
        }
        addRecipeButton.setVisible(false);
        removeRecipeButton.setVisible(false);
        mealNameTextField.setVisible(false);
        mealNameLabel.setVisible(false);
        unusedRecipesLabel.setVisible(false);
        unusedRecipesComboBox.setVisible(false);
    }

    // Gets all meals in the database.
    private void getMeals() {
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from MEALS order by name";  // Get all ingredients, sorting by name.

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            myMeals.clear();   // Clear meals from any previous use.
            while (rs.next()) {
                String mealName = rs.getString(1);
                myMeals.add(mealName);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    // Gets all recipes that are used in meal.
    private void getUsedRecipes(String meal) {
        usedRecipes.clear();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from SERVEDDURINGMEAL where mealName = '" + meal + "' order by recipeTitle";  // Get all recipes served in this meal.

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String recipeTitle = rs.getString(1);
                System.out.println("new used recipe for " + meal + " : " + recipeTitle);
                usedRecipes.add(recipeTitle);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
    }

    // Gets all recipes not used in meal.
    private void getUnusedRecipes(String meal) {
        unUsedRecipes.clear();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from RECIPES where title <> all(select "
                    + "recipeTitle from SERVEDDURINGMEAL where mealName = '" + meal + "')";  // Get all recipes not served in this meal.

            System.out.println("sql: " + sqlStatement);
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String recipeTitle = rs.getString(1);
                System.out.println("recipetitle = " + recipeTitle);
                unUsedRecipes.add(recipeTitle);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(pst);
            ConnectDb.close(rs);
            ConnectDb.close(conn);
        }
    }

    private void putMealsInComboBox() {
        if (myMeals.size() > 0) {
            deleteMealButton.setVisible(true);
            mealsComboBox.setVisible(true);
            myMealsLabel.setVisible(true);
            usedRecipesLabel.setVisible(true);
            usedRecipesComboBox.setVisible(true);
            editMealButton.setVisible(true);

            System.out.println("about to remove all items from the meals cbox");
            mealsComboBox.removeAllItems();
            for (int i = 0; i < myMeals.size(); i++) {
                mealsComboBox.addItem(myMeals.get(i));
            }
        } else {
            deleteMealButton.setVisible(false);
            mealsComboBox.setVisible(false);
            myMealsLabel.setVisible(false);
            usedRecipesLabel.setVisible(false);
            usedRecipesComboBox.setVisible(false);
            editMealButton.setVisible(false);
        }
    }

    private void putUnUsedRecipesInComboBox() {
        if (unUsedRecipes.size() > 0) {
            unusedRecipesComboBox.setVisible(true);
            unusedRecipesLabel.setVisible(true);
            addRecipeButton.setVisible(true);

            unusedRecipesComboBox.removeAllItems();
            for (int i = 0; i < unUsedRecipes.size(); i++) {
                unusedRecipesComboBox.addItem(unUsedRecipes.get(i));
            }
        } else {
            unusedRecipesComboBox.setVisible(false);
            unusedRecipesLabel.setVisible(false);
            addRecipeButton.setVisible(false);
        }
    }

    private void putUsedRecipesInComboBox() {
        if (usedRecipes.size() > 0) {
            usedRecipesComboBox.setVisible(true);
            usedRecipesLabel.setVisible(true);
            if (editMealButton.getText().equals("Submit Changes")) {  // in edit mode  
                addRecipeButton.setVisible(true);
            }

            System.out.println("about to remove items. COunt = " + usedRecipesComboBox.getItemCount());
            usedRecipesComboBox.removeAllItems();
            System.out.println("removed allitem. COunt = " + usedRecipesComboBox.getItemCount());
            System.out.println("number of recipes in data structure: " + usedRecipes.size());
            for (int i = 0; i < usedRecipes.size(); i++) {
                usedRecipesComboBox.addItem(usedRecipes.get(i));
            }
        } else {
            usedRecipesComboBox.setVisible(false);
            usedRecipesLabel.setVisible(false);
            addRecipeButton.setVisible(false);
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

        addMealButton = new javax.swing.JButton();
        editMealButton = new javax.swing.JButton();
        deleteMealButton = new javax.swing.JButton();
        unusedRecipesComboBox = new javax.swing.JComboBox<>();
        usedRecipesComboBox = new javax.swing.JComboBox<>();
        unusedRecipesLabel = new javax.swing.JLabel();
        usedRecipesLabel = new javax.swing.JLabel();
        mealNameLabel = new javax.swing.JLabel();
        mealNameTextField = new javax.swing.JTextField();
        addRecipeButton = new javax.swing.JButton();
        removeRecipeButton = new javax.swing.JButton();
        mealsComboBox = new javax.swing.JComboBox<>();
        myMealsLabel = new javax.swing.JLabel();

        addMealButton.setText("Add a Meal");
        addMealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMealButtonActionPerformed(evt);
            }
        });

        editMealButton.setText("Edit Meal");
        editMealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMealButtonActionPerformed(evt);
            }
        });

        deleteMealButton.setText("Delete Meal");
        deleteMealButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMealButtonActionPerformed(evt);
            }
        });

        unusedRecipesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unusedRecipesComboBoxActionPerformed(evt);
            }
        });

        usedRecipesComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        usedRecipesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usedRecipesComboBoxActionPerformed(evt);
            }
        });

        unusedRecipesLabel.setText("Available recipes:");

        usedRecipesLabel.setText("Recipes used in this meal:");

        mealNameLabel.setText("Meal Name:");

        addRecipeButton.setText("Add Recipe");
        addRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRecipeButtonActionPerformed(evt);
            }
        });

        removeRecipeButton.setText("Remove Recipe");
        removeRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeRecipeButtonActionPerformed(evt);
            }
        });

        mealsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealsComboBoxActionPerformed(evt);
            }
        });

        myMealsLabel.setText("My Meals:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(addMealButton)
                        .addGap(26, 26, 26)
                        .addComponent(editMealButton)
                        .addGap(34, 34, 34)
                        .addComponent(deleteMealButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(mealNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mealNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(myMealsLabel)
                        .addGap(18, 18, 18)
                        .addComponent(mealsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(unusedRecipesLabel)
                            .addComponent(usedRecipesLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unusedRecipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usedRecipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeRecipeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addRecipeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mealsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myMealsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mealNameLabel)
                    .addComponent(mealNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(usedRecipesLabel)
                            .addComponent(usedRecipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unusedRecipesLabel)
                            .addComponent(unusedRecipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addRecipeButton)))
                    .addComponent(removeRecipeButton))
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addMealButton)
                    .addComponent(editMealButton)
                    .addComponent(deleteMealButton))
                .addGap(78, 78, 78))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mealsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealsComboBoxActionPerformed
        if (mealsComboBox.getItemCount() > 0 && myMeals.size() > 0) { // If there are any meals and they are in the combo box...
            // A new meal has been selected. Place all used recipes into the combo box.
            usedRecipes.clear(); //Remove any recipes present from a previous meal.
            // Fetch the recipes used for this meal and place them in the combobox.                        
            getUsedRecipes(mealsComboBox.getSelectedItem().toString());
            putUsedRecipesInComboBox();
        }
    }//GEN-LAST:event_mealsComboBoxActionPerformed

    private boolean mealIsNotEmpty(String mealName) {
        // Make sure the meal's name is not empty.
        if (mealName == null || mealName.equals("")) {
            JOptionPane.showMessageDialog(this,
                    "The meal name is empty.",
                    "Meal Not Added",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean mealDoesNotAlreadyExist(boolean editingMode, String mealName) {
        // Check if the meal is already in the list.
        int selectedIngredient = mealsComboBox.getSelectedIndex();
        for (int i = 0; i < myMeals.size(); i++) {
            if (editingMode && i == selectedIngredient) {
                continue;
            }
            if (myMeals.get(i).equals(mealName)) {
                JOptionPane.showMessageDialog(this,
                        "The meal already exists.",
                        "Meal Not Added",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // Passed all the checks - return true so the meal can be submitted to the database.
        return true;
    }

    private void addMealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMealButtonActionPerformed
        System.out.println("used recipes size = " + usedRecipes.size());
        if (addMealButton.getText().equals("Add a Meal")) {
            usedRecipesComboBox.removeAllItems();
            mealNameTextField.setText("");
            addMealButton.setText("Submit New Meal");
            deleteMealButton.setText("Cancel");

            // Hide irrelevant components.
            mealsComboBox.setVisible(false);
            myMealsLabel.setVisible(false);
            removeRecipeButton.setVisible(false);
            usedRecipesLabel.setVisible(false);
            usedRecipesComboBox.setVisible(false);
            editMealButton.setVisible(false);

            // Unhide components that allow user to edit the meal.
            mealNameTextField.setVisible(true);
            mealNameLabel.setVisible(true);
            deleteMealButton.setVisible(true);
            addRecipeButton.setVisible(true);
            unusedRecipesLabel.setVisible(true);
            unusedRecipesComboBox.setVisible(true);

            getUnusedRecipes("");  // Get all recipes for this (null) meal - should place all recipes into the combo box.
            putUnUsedRecipesInComboBox();

        } else {
            String mealName = mealNameTextField.getText().toString();
            if (mealDoesNotAlreadyExist(false, mealName) && mealIsNotEmpty(mealName)) {  // Verify input.
                insertIntoMeals(mealName);
                insertIntoServedDuringMeal(mealName);
                // Place in the combo box of meals.
                mealsComboBox.addItem(mealName);
                mealsComboBox.setSelectedItem(mealName);

                // Change GUI components.
                addMealButton.setText("Add a Meal");
                deleteMealButton.setText("Delete Meal");
                editMealButton.setText("Edit Meal");

                addRecipeButton.setVisible(false);
                removeRecipeButton.setVisible(false);
                unusedRecipesComboBox.setVisible(false);
                unusedRecipesLabel.setVisible(false);
                mealNameLabel.setVisible(false);
                mealNameTextField.setVisible(false);
                editMealButton.setVisible(true);
                myMealsLabel.setVisible(true);
                mealsComboBox.setVisible(true);

            }
        }

    }//GEN-LAST:event_addMealButtonActionPerformed

    private void addRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRecipeButtonActionPerformed
        String recipe = unusedRecipesComboBox.getSelectedItem().toString();
        if (newUnUsedRecipes.contains(recipe)) {
            newUnUsedRecipes.remove(recipe);
        } else {
            newUsedRecipes.add(recipe);
        }
        if (usedRecipesComboBox.getItemCount() > 0 && usedRecipesComboBox.getItemAt(0).equals(" ")) {  // For some reason, Java will put one blank item in a combo box if nothing else is there. So, I'm removing that item.
            usedRecipesComboBox.removeItemAt(0);
        }
        usedRecipesComboBox.addItem(recipe);
        usedRecipesComboBox.setSelectedItem(recipe);  // select this recipe so the user can see recipes being added
        unusedRecipesComboBox.removeItemAt(unusedRecipesComboBox.getSelectedIndex());

        // Check if GUIs need hidden/unhidden.
        if (unusedRecipesComboBox.getItemCount() == 0) {
            unusedRecipesComboBox.setVisible(false);
            unusedRecipesLabel.setVisible(false);
            addRecipeButton.setVisible(false);
        }
        if (!usedRecipesComboBox.isVisible()) {
            usedRecipesComboBox.setVisible(true);
            usedRecipesLabel.setVisible(true);
            removeRecipeButton.setVisible(true);
        }
    }//GEN-LAST:event_addRecipeButtonActionPerformed

    private void removeRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeRecipeButtonActionPerformed
        String recipe = usedRecipesComboBox.getSelectedItem().toString();
        if (newUsedRecipes.contains(recipe)) {
            newUsedRecipes.remove(recipe);
        } else {
            newUnUsedRecipes.add(recipe);
        }
        unusedRecipesComboBox.addItem(recipe);
        unusedRecipesComboBox.setSelectedItem(recipe);  //select this recipe so the user can see things changing on the screen
        usedRecipesComboBox.removeItemAt(usedRecipesComboBox.getSelectedIndex());

        // Check if GUIs need hidden/unhidden.
        if (usedRecipesComboBox.getItemCount() == 0) {
            usedRecipesComboBox.setVisible(false);
            usedRecipesLabel.setVisible(false);
            removeRecipeButton.setVisible(false);
        }
        if (!unusedRecipesComboBox.isVisible()) {
            unusedRecipesComboBox.setVisible(true);
            unusedRecipesLabel.setVisible(true);
            addRecipeButton.setVisible(true);
        }
    }//GEN-LAST:event_removeRecipeButtonActionPerformed

    private void deleteMealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMealButtonActionPerformed
        if (deleteMealButton.getText().equals("Cancel")) {
            deleteMealButton.setText("Delete Meal");
            addMealButton.setText("Add a Meal");
            addMealButton.setVisible(true);
            editMealButton.setText("Edit Meal");
            addRecipeButton.setVisible(false);
            removeRecipeButton.setVisible(false);
            unusedRecipesComboBox.setVisible(false);
            unusedRecipesLabel.setVisible(false);
            mealNameLabel.setVisible(false);
            mealNameTextField.setVisible(false);

            // Want to remove any recipes from the used recipes combo box, as they may have been added during editing but were not submitted to the database
            if (mealsComboBox.getItemCount() > 0) { // there are meals to be selectd from
                getUsedRecipes(mealsComboBox.getSelectedItem().toString());
                putUsedRecipesInComboBox();
            }

            if (myMeals.size() > 0) {
                editMealButton.setVisible(true);
                myMealsLabel.setVisible(true);
                mealsComboBox.setVisible(true);
            } else {  //No meals.
                deleteMealButton.setVisible(false);
                editMealButton.setVisible(false);
                myMealsLabel.setVisible(false);
                mealsComboBox.setVisible(false);
            }
        } else {
            // Delete from database.
            String mealName = mealsComboBox.getSelectedItem().toString();
            deleteMeal(mealName);  // removes the meal from the database
            getMeals();
            putMealsInComboBox();
        }
    }//GEN-LAST:event_deleteMealButtonActionPerformed

    private void unusedRecipesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unusedRecipesComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unusedRecipesComboBoxActionPerformed

    private void usedRecipesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usedRecipesComboBoxActionPerformed

    }//GEN-LAST:event_usedRecipesComboBoxActionPerformed

    private void editMealButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMealButtonActionPerformed
        if (editMealButton.getText().equals("Edit Meal")) {
            editMealButton.setText("Submit Changes");
            deleteMealButton.setText("Cancel");

            String mealName = mealsComboBox.getSelectedItem().toString();
            mealsComboBox.setVisible(false);
            myMealsLabel.setVisible(false);
            addMealButton.setVisible(false);
            mealNameLabel.setVisible(true);
            mealNameTextField.setVisible(true);
            mealNameTextField.setText(mealName);
            removeRecipeButton.setVisible(true);

            //Fill used/unused recipe combo boxes for this meal.
            getUnusedRecipes(mealName);
            putUnUsedRecipesInComboBox();
            getUsedRecipes(mealName);
            putUsedRecipesInComboBox();

        } else { // Add to db and change screen GUI accordingly
            String oldMealName = mealsComboBox.getSelectedItem().toString();
            String newMealName = mealNameTextField.getText();
            updateMeal(oldMealName, newMealName);

            // Remove the meal's old name from the combo box.
            mealsComboBox.removeItem(oldMealName);

            // Place the new name into the combobox.
            mealsComboBox.addItem(newMealName);

            // Update the recipes served during the meal for this meal.
            insertIntoServedDuringMeal(newMealName);

            // Delete any recipes that are no longer served during this meal.
            deleteFromServedDuringMeal(newMealName);

            editMealButton.setText("Edit Meal");
            deleteMealButton.setText("Delete Meal");

            mealsComboBox.setVisible(true);
            myMealsLabel.setVisible(true);
            addMealButton.setVisible(true);
            mealNameLabel.setVisible(false);
            mealNameTextField.setVisible(false);
            unusedRecipesComboBox.setVisible(false);
            unusedRecipesLabel.setVisible(false);
            addRecipeButton.setVisible(false);
            removeRecipeButton.setVisible(false);
        }
    }//GEN-LAST:event_editMealButtonActionPerformed

    private void updateMeal(String oldName, String newName) {
        try {
            String sqlUpdateStmt = "update MEALS set name = '" + newName + "' where name = '" + oldName + "'";
            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlUpdateStmt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
        } finally {
            try {  // Try closing the connection and the statement.
                ConnectDb.close(conn);
                ConnectDb.close(stmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
            }
        }
    }

    private void insertIntoServedDuringMeal(String mealName) {
        for (int i = 0; i < newUsedRecipes.size(); i++) {
            try {
                String sqlInsertStmt = "insert into SERVEDDURINGMEAL values ('" + newUsedRecipes.get(i) + "', '" + mealName + "')";
                conn = ConnectDb.setupConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlInsertStmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            } finally {
                try {  // Try closing the connection and the statement.
                    ConnectDb.close(conn);
                    ConnectDb.close(stmt);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
                }
            }
        }
    }

    private void deleteMeal(String newName) {
        try {
            String sqlDeleteStmt = "delete from MEALS where name = '" + newName + "'";
            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlDeleteStmt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
        } finally {
            try {  // Try closing the connection and the statement.
                ConnectDb.close(conn);
                ConnectDb.close(stmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
            }
        }
    }

    private void deleteFromServedDuringMeal(String mealName) {
        for (int i = 0; i < newUnUsedRecipes.size(); i++) {
            try {
                String sqlDeleteStmt = "delete from SERVEDDURINGMEAL where recipeTitle = '" + newUnUsedRecipes.get(i) + "'";

                conn = ConnectDb.setupConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlDeleteStmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.
            } finally {
                try {  // Try closing the connection and the statement.
                    ConnectDb.close(conn);
                    ConnectDb.close(stmt);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
                }
            }
        }
    }

    private void insertIntoMeals(String mealName) {
        try {
            String sqlInsertStmt = "insert into MEALS values ('" + mealName + "')";

            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlInsertStmt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);  // Show the exception message.
        } finally {
            try {  // Try closing the connection and the statement.
                ConnectDb.close(conn);
                ConnectDb.close(stmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);  // Show the exception message.}
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addMealButton;
    private javax.swing.JButton addRecipeButton;
    private javax.swing.JButton deleteMealButton;
    private javax.swing.JButton editMealButton;
    private javax.swing.JLabel mealNameLabel;
    private javax.swing.JTextField mealNameTextField;
    private javax.swing.JComboBox<String> mealsComboBox;
    private javax.swing.JLabel myMealsLabel;
    private javax.swing.JButton removeRecipeButton;
    private javax.swing.JComboBox<String> unusedRecipesComboBox;
    private javax.swing.JLabel unusedRecipesLabel;
    private javax.swing.JComboBox<String> usedRecipesComboBox;
    private javax.swing.JLabel usedRecipesLabel;
    // End of variables declaration//GEN-END:variables
}
