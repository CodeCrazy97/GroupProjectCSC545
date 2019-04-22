/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject545;

import com.sun.javafx.scene.control.skin.VirtualFlow;
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
public class RecipesGUI extends javax.swing.JPanel {

    OraclePreparedStatement pst = null;
    OracleResultSet rs = null;
    Connection conn = null;
    Statement stmt = null;

    // Will use the below two data structures to keep up with what ingredients need 
    // inserting/removing from the CALLSFOR table.
    public List<String> ingredientsForDeleteFromCallsFor = new ArrayList<String>();
    public List<String> ingredientsForInsertIntoCallsFor = new ArrayList<String>();

    public List<Recipes> recipes = new ArrayList<Recipes>();
    public Recipes recipesClass = new Recipes();

    /**
     * Creates new form RecipesGUI
     */
    public RecipesGUI(JFrame frame) {
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

        frame.add(this);  // Pass the current instance of the IngredientsGUI class to the frame.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Get recipes, place them into the combo box. Also, place first recipe's info into the text fields.
        getRecipes();

        // Hide the adding a new ingredient components (until user wants to).
        changeEditingMode(false);
        removeIngredientButton.setVisible(false);  // Not editing, so hide this button.

        getRecipesFilterByCategory();
        getRecipesFilterByIngredient();
    }

    private void getRecipesFilterByCategory() {
        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select distinct category from RECIPES order by category";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            categoryFilterComboBox.removeAllItems();
            categoryFilterComboBox.addItem("-- PICK --");
            while (rs.next()) {
                String category = rs.getString(1);
                categoryFilterComboBox.addItem(category);
            }

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }

    }

    private void getRecipesFilterByIngredient() {

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select distinct ingredientName from CALLSFOR order by ingredientName";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            ingredientsFilterComboBox.removeAllItems();
            ingredientsFilterComboBox.addItem("-- PICK --");
            while (rs.next()) {
                String ingredientName = rs.getString(1);
                ingredientsFilterComboBox.addItem(ingredientName);
            }

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }

    }

    private void changeEditingMode(boolean mode) {
        if (mode) {
            deleteRecipeButton.setText("Cancel");
        } else {
            deleteRecipeButton.setText("Delete Recipe");
        }
        recipeTitleLabel.setVisible(mode);
        recipeTitleTextField.setVisible(mode);
        unusedIngredientComboBox.setVisible(mode);
        newIngredientLabel.setVisible(mode);
        addNewIngredientButton.setVisible(mode);
        instructionsTextArea.setEditable(mode);
        recipeCategoryTextField.setEditable(mode);
        recipeTitleTextField.setEditable(mode);

        if (mode) {  // Fetch all ingredients not currently being used in the recipe, place them in the combo box.
            if (ingredientsComboBox.getItemCount() != 0) {  // allow user to remove ingredients from the list, if there are ingredients to be deleted
                removeIngredientButton.setVisible(true);
            }

            String recipeTitle = recipeTitleTextField.getText();
            List<String> unusedIngredients = recipesClass.getIngredientsNotUsedInRecipe(recipeTitle);
            unusedIngredientComboBox.removeAllItems();
            if (unusedIngredients.size() > 0) {
                // Unhide relevant GUIs.
                unusedIngredientComboBox.setVisible(true);
                newIngredientLabel.setVisible(true);
                addNewIngredientButton.setVisible(true);

                // Loop over all the unused ingredients, placing them into the combo box.
                for (int i = 0; i < unusedIngredients.size(); i++) {
                    unusedIngredientComboBox.addItem(unusedIngredients.get(i));
                }
            }
        }
    }

    private void getRecipes() {
        recipes = recipesClass.getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            recipesComboBox.addItem(recipes.get(i).getTitle());
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

        recipesComboBox = new javax.swing.JComboBox<>();
        addNewRecipeButton = new javax.swing.JButton();
        deleteRecipeButton = new javax.swing.JButton();
        editRecipeButton = new javax.swing.JButton();
        recipeTitleTextField = new javax.swing.JTextField();
        recipeCategoryTextField = new javax.swing.JTextField();
        recipeTitleLabel = new javax.swing.JLabel();
        categoryLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        instructionsTextArea = new javax.swing.JTextArea();
        instructionsLabel = new javax.swing.JLabel();
        ingredientsComboBox = new javax.swing.JComboBox<>();
        ingredientsLabel = new javax.swing.JLabel();
        removeIngredientButton = new javax.swing.JButton();
        addNewIngredientButton = new javax.swing.JButton();
        unusedIngredientComboBox = new javax.swing.JComboBox<>();
        newIngredientLabel = new javax.swing.JLabel();
        existingRecipesPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        filteredRecipesTextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        filtersPanel = new javax.swing.JPanel();
        categoryFilterComboBox = new javax.swing.JComboBox<>();
        ingredientsFilterComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        recipesComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recipesComboBoxActionPerformed(evt);
            }
        });

        addNewRecipeButton.setText("Add New Recipe");
        addNewRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewRecipeButtonActionPerformed(evt);
            }
        });

        deleteRecipeButton.setText("Delete Recipe");
        deleteRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRecipeButtonActionPerformed(evt);
            }
        });

        editRecipeButton.setText("Edit Recipe");
        editRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRecipeButtonActionPerformed(evt);
            }
        });

        recipeTitleLabel.setText("Recipe title:");

        categoryLabel.setText("Category:");

        instructionsTextArea.setColumns(20);
        instructionsTextArea.setRows(5);
        jScrollPane1.setViewportView(instructionsTextArea);

        instructionsLabel.setText("Instructions:");

        ingredientsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsComboBoxActionPerformed(evt);
            }
        });

        ingredientsLabel.setText("Ingredients:");

        removeIngredientButton.setText("Remove Ingredient");
        removeIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeIngredientButtonActionPerformed(evt);
            }
        });

        addNewIngredientButton.setText("Add New Ingredient");
        addNewIngredientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewIngredientButtonActionPerformed(evt);
            }
        });

        unusedIngredientComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        unusedIngredientComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unusedIngredientComboBoxActionPerformed(evt);
            }
        });

        newIngredientLabel.setText("New Ingredient:");

        filteredRecipesTextArea.setEditable(false);
        filteredRecipesTextArea.setColumns(20);
        filteredRecipesTextArea.setRows(5);
        jScrollPane2.setViewportView(filteredRecipesTextArea);

        jLabel3.setText("Show recipes by category/ingredeints used:");

        categoryFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryFilterComboBoxActionPerformed(evt);
            }
        });

        ingredientsFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ingredientsFilterComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filtersPanelLayout = new javax.swing.GroupLayout(filtersPanel);
        filtersPanel.setLayout(filtersPanelLayout);
        filtersPanelLayout.setHorizontalGroup(
            filtersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filtersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filtersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(categoryFilterComboBox, 0, 91, Short.MAX_VALUE)
                    .addComponent(ingredientsFilterComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        filtersPanelLayout.setVerticalGroup(
            filtersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filtersPanelLayout.createSequentialGroup()
                .addComponent(categoryFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ingredientsFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jLabel5.setText("Ingredients");

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        jLabel4.setText("Category");

        javax.swing.GroupLayout existingRecipesPanel2Layout = new javax.swing.GroupLayout(existingRecipesPanel2);
        existingRecipesPanel2.setLayout(existingRecipesPanel2Layout);
        existingRecipesPanel2Layout.setHorizontalGroup(
            existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filtersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        existingRecipesPanel2Layout.setVerticalGroup(
            existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGroup(existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                        .addGroup(existingRecipesPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(filtersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5)))
                        .addGap(170, 170, 170))
                    .addGroup(existingRecipesPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addNewRecipeButton)
                                .addGap(31, 31, 31)
                                .addComponent(editRecipeButton)
                                .addGap(30, 30, 30)
                                .addComponent(deleteRecipeButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(recipeTitleLabel)
                                    .addComponent(categoryLabel))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(recipeCategoryTextField)
                                    .addComponent(recipeTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(existingRecipesPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(instructionsLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(370, 370, 370)
                        .addComponent(recipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ingredientsLabel)
                            .addComponent(newIngredientLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unusedIngredientComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ingredientsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeIngredientButton)
                            .addComponent(addNewIngredientButton))))
                .addContainerGap(353, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(existingRecipesPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(57, 57, 57)
                .addComponent(recipesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recipeTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recipeTitleLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recipeCategoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryLabel))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingredientsLabel)
                    .addComponent(ingredientsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeIngredientButton))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewIngredientButton)
                    .addComponent(newIngredientLabel)
                    .addComponent(unusedIngredientComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(instructionsLabel))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewRecipeButton)
                    .addComponent(deleteRecipeButton)
                    .addComponent(editRecipeButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void recipesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recipesComboBoxActionPerformed
        // New recipe has been selected. Load it's information.
        int pickedRecipe = recipesComboBox.getSelectedIndex();
        recipeTitleTextField.setText(recipes.get(pickedRecipe).getTitle());
        recipeCategoryTextField.setText(recipes.get(pickedRecipe).getCategory());
        instructionsTextArea.setText(recipes.get(pickedRecipe).getInstructions());

        // Populate the (used) ingredients combo box with ingredients that are used in this recipe.
        ingredientsComboBox.removeAllItems();  // Clear anything out of the ingredients combo box that was there from a previous recipe.
        List<String> ingredients = recipesClass.getIngredientsUsedInRecipe(recipes.get(pickedRecipe).getTitle());
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientsComboBox.addItem(ingredients.get(i));
        }
        if (ingredients.size() == 0) {  // Hide the combo box and label (may already be hidden).
            ingredientsComboBox.setVisible(false);
            ingredientsLabel.setVisible(false);
        } else {
            ingredientsComboBox.setVisible(true);
            ingredientsLabel.setVisible(true);
        }
    }//GEN-LAST:event_recipesComboBoxActionPerformed

    private boolean recipeIsNotEmpty(String recipeTitle) {
        if (recipeTitle == null || recipeTitle.equals("")) {
            JOptionPane.showMessageDialog(this,
                    "The recipe title is empty.",
                    "Recipe Not Added",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean recipeDoesNotAlreadyExist(boolean editingMode, String recipeTitle) {
        // Check if the recipe is already in the list.
        int selectedIngredient = recipesComboBox.getSelectedIndex();
        for (int i = 0; i < recipes.size(); i++) {
            if (editingMode && i == selectedIngredient) {  // Don't want this method throwing an error because user isn't changing the recipe's title (could be changing it's category facts, for example)
                continue;
            }
            if (recipes.get(i).getTitle().equals(recipeTitle)) {
                JOptionPane.showMessageDialog(this,
                        "The recipe already exists.",
                        "Recipe Not Added",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // Passed all the checks - return true so the recipe can be submitted to the database.
        return true;
    }

    private void addNewRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewRecipeButtonActionPerformed

        if (addNewRecipeButton.getText().equals("Add New Recipe")) {  // User wants to add a new ingredient. Hide some elements.
            // Clean up anything that had been done as far as remvoing/adding ingredients.
            ingredientsForDeleteFromCallsFor.clear();
            ingredientsForInsertIntoCallsFor.clear();

            recipeTitleTextField.setText("");  // Set to empty before doing anything else (makes changeEditingMode operations easier)
            ingredientsComboBox.removeAllItems(); // Remove anything already in the ingredients (there should not be any ingredients for a new recipe)
            addNewRecipeButton.setText("Submit New Recipe");

            // null out text in nutrition facts field and food group field.
            instructionsTextArea.setText("");
            recipeCategoryTextField.setText("");

            // Hide everything that relates to an old recipe
            removeIngredientButton.setVisible(false);
            ingredientsComboBox.setVisible(false);
            ingredientsLabel.setVisible(false);

            editRecipeButton.setVisible(false);
            recipesComboBox.setVisible(false);
            changeEditingMode(true);
        } else // Do opposite of if condition. Try to add recipe to screen and database.
        {
            if (recipeIsNotEmpty(recipeTitleTextField.getText()) && recipeDoesNotAlreadyExist(false, recipeTitleTextField.getText())) {  // Before submitting changes to the database, validate the recipe (make sure it's title is not null and that there is not already an recipe with that name).            
                String title = recipeTitleTextField.getText();
                String instructions = instructionsTextArea.getText();
                String category = recipeCategoryTextField.getText();
                Recipes recipe = new Recipes(title, instructions, category);

                // Must insert into RECIPES before we can insert into CALLSFOR.
                insertIntoRecipes(title, instructions, category);

                // Insert into the CALLSFOR table all the ingredients used in the new recipe.
                insertIntoCallsFor(title);

                // Place new item in the list and combo box.
                recipes.add(0, recipe);
                recipesComboBox.insertItemAt(title, 0);

                recipesComboBox.setVisible(true);
                // Select the new item.
                recipesComboBox.setSelectedIndex(0);

                editRecipeButton.setVisible(true);
                addNewRecipeButton.setText("Add New Recipe");
                removeIngredientButton.setVisible(false);
                changeEditingMode(false);
            }
        }
    }//GEN-LAST:event_addNewRecipeButtonActionPerformed

    private void deleteRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRecipeButtonActionPerformed
        // There are two times when this button gets clicked: when the user wants
        // to delete a recipe or when a user wishes to cancel out of edit mode.
        if (deleteRecipeButton.getText().equals("Delete Recipe")) {
            // Remove the recipe from the screen and the database.
            String title = recipesComboBox.getSelectedItem().toString();
            try {
                // First, delete from CALLSFOR.
                String sqlDeleteStmt = "delete from RECIPES where title = '" + title + "'";

                conn = ConnectDb.setupConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlDeleteStmt);
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

            recipesComboBox.removeItemAt(recipesComboBox.getSelectedIndex());
            if (recipesComboBox.getItemCount() == 0) {  // NOthing to delete/edit.
                deleteRecipeButton.setVisible(false);
                editRecipeButton.setVisible(false);
            }
        } else {

            removeIngredientButton.setVisible(false);

            // Cancel out of edit mode. Select first recipe so the fields will populate with its data.
            changeEditingMode(false);  // Get out of edit mode.
            recipesComboBox.setVisible(true);

            ingredientsLabel.setVisible(true);
            ingredientsComboBox.setVisible(true);
            recipesComboBox.setSelectedIndex(0);

            // Reset the buttons' text.
            addNewRecipeButton.setVisible(true);
            addNewRecipeButton.setText("Add New Recipe");
            editRecipeButton.setText("Edit Recipe");
            editRecipeButton.setVisible(true);
        }
    }//GEN-LAST:event_deleteRecipeButtonActionPerformed

    private void editRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRecipeButtonActionPerformed

        if (editRecipeButton.getText().equals("Edit Recipe")) {
            // Clean up anything that had been done as far as remvoing/adding ingredients.
            ingredientsForDeleteFromCallsFor.clear();
            ingredientsForInsertIntoCallsFor.clear();

            int pickedRecipe = recipesComboBox.getSelectedIndex();
            recipeTitleTextField.setText(recipes.get(pickedRecipe).getTitle());  // Place the title in the text field before calling changeEditMode

            // Populate with the instructions, category.            
            instructionsTextArea.setText(recipes.get(pickedRecipe).getInstructions());
            recipeCategoryTextField.setText(recipes.get(pickedRecipe).getCategory());

            recipesComboBox.setVisible(false);
            editRecipeButton.setText("Submit Changes");
            changeEditingMode(true);

            addNewRecipeButton.setVisible(false);
        } else if (recipeIsNotEmpty(recipeTitleTextField.getText()) && recipeDoesNotAlreadyExist(true, recipeTitleTextField.getText())) {  // Make sure the recipe does not already exist in the database.
            String title = recipeTitleTextField.getText();
            String instructions = instructionsTextArea.getText();
            String category = recipeCategoryTextField.getText();
            Recipes recipe = new Recipes(title, instructions, category);
            String oldTitle = recipesComboBox.getSelectedItem().toString();

            // Must work with CALLSFOR first, as it is a child database.
            // If there is something to insert or delete from the CALLSFOR table
            // (if the user removed/added ingredients used in the recipe), then
            // proceed accordingly...
            insertIntoCallsFor(oldTitle);     // Place the recipes and ingredients back in.
            deleteFromCallsFor(oldTitle);      // Remove the entry(ies) in the CALLSFOR table, if there are any. 

            if (oldTitle.equals(title)) {  // Recipe name not changed. Only need to delete all the ingredients in the combo box and update the contents of the recipe (excluding the title).
                updateRecipesNoTitleChange(title, instructions, category);
            } else {
                updateRecipesTitleChange(oldTitle, title, instructions, category);
            }

            // Remove the old version of the recipe from the screen.
            int pickedItem = recipesComboBox.getSelectedIndex();

            recipes.remove(pickedItem);
            recipesComboBox.removeItemAt(pickedItem);

            // Place new item in the list and combo box.
            recipes.add(0, recipe);
            recipesComboBox.insertItemAt(title, 0);

            recipesComboBox.setVisible(true);
            // Select the new item.
            recipesComboBox.setSelectedIndex(0);

            editRecipeButton.setVisible(true);
            editRecipeButton.setText("Edit Recipe");
            removeIngredientButton.setVisible(false);
            addNewRecipeButton.setVisible(true);
            changeEditingMode(false);

        }
    }//GEN-LAST:event_editRecipeButtonActionPerformed

    private void deleteFromCallsFor(String recipeTitle) {
        for (int i = 0; i < ingredientsForDeleteFromCallsFor.size(); i++) {
            try {
                // First, delete from CALLSFOR.
                String sqlDeleteStmt = "delete from CALLSFOR where recipeTitle = '"
                        + recipeTitle + "' and ingredientName = '" + ingredientsForDeleteFromCallsFor.get(i) + "'";
                System.out.println("deleting from callsfor: " + sqlDeleteStmt);
                conn = ConnectDb.setupConnection();
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlDeleteStmt);
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
    }

    private void updateRecipesNoTitleChange(String title, String instructions, String category) {
        // Insert the item into the database.
        try {
            String sqlUpdateStmt = "update RECIPES set instructions = '" + instructions + "', category = '"
                    + category + "' where title = '" + title + "'";
            System.out.println("about to update recipes...sql:" + sqlUpdateStmt);
            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
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

    private void updateRecipesTitleChange(String oldTitle, String title, String instructions, String category) {
        try {
            String sqlInsertStmt = "update RECIPES set title = '" + title + "', "
                    + "instructions = '" + instructions + "', category = '" + category + "' where "
                    + "title = '" + oldTitle + "'";

            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
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

    private void insertIntoRecipes(String title, String instructions, String category) {
        try {
            String sqlInsertStmt = "insert into RECIPES values ('" + title + "', '" + instructions + "', '" + category + "')";

            conn = ConnectDb.setupConnection();
            stmt = conn.createStatement();
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

    private void insertIntoCallsFor(String title) {
        // Insert into the CALLSFOR table the ingredients used in the recipe.
        for (int i = 0; i < ingredientsForInsertIntoCallsFor.size(); i++) {
            try {
                String sqlInsertStmt = "insert into CALLSFOR values ('" + ingredientsForInsertIntoCallsFor.get(i) + "', '" + title + "')";
                System.out.println("inserting into callsfor: sql=" + sqlInsertStmt);
                conn = ConnectDb.setupConnection();
                stmt = conn.createStatement();
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
    }

    private void addNewIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewIngredientButtonActionPerformed
        String currentIngredient = unusedIngredientComboBox.getSelectedItem().toString();
        if (ingredientsForDeleteFromCallsFor.contains(currentIngredient)) {
            // Remove from ingredientsForDeleteFromCallsFor (adding it as an ingredient cancels out the removing it, which had apparently been done earlier)
            ingredientsForDeleteFromCallsFor.remove(currentIngredient);
        } else {  // Go on and add to ingredients that need to be inserted.
            ingredientsForInsertIntoCallsFor.add(currentIngredient);
        }
        if (!ingredientsComboBox.isVisible()) {  // Time to unhide this combo box (something is about to be added to it)
            ingredientsComboBox.setVisible(true);
            ingredientsLabel.setVisible(true);
            removeIngredientButton.setVisible(true);
        }
        String unusedIngredient = unusedIngredientComboBox.getSelectedItem().toString();
        unusedIngredientComboBox.removeItemAt(unusedIngredientComboBox.getSelectedIndex());
        ingredientsComboBox.addItem(unusedIngredient);
        if (unusedIngredientComboBox.getItemCount() == 0) {  // If deleting this unusedingredient from the unused ingredients list resulted in an empty combo box, then hide the relevant guis.
            unusedIngredientComboBox.setVisible(false);
            addNewIngredientButton.setVisible(false);
            newIngredientLabel.setVisible(false);
        }
    }//GEN-LAST:event_addNewIngredientButtonActionPerformed

    private void ingredientsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ingredientsComboBoxActionPerformed

    private void removeIngredientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeIngredientButtonActionPerformed
        String currentIngredient = ingredientsComboBox.getSelectedItem().toString();
        if (ingredientsForInsertIntoCallsFor.contains(currentIngredient)) {
            // Remove from ingredientsForInsertIntoCallsFor
            ingredientsForInsertIntoCallsFor.remove(currentIngredient);
        } else {  // Go on and add to ingredients that need to be inserted.
            ingredientsForDeleteFromCallsFor.add(currentIngredient);
        }
        if (!unusedIngredientComboBox.isVisible()) {  // Time to unhide this combo box (something is about to be added to it)
            unusedIngredientComboBox.setVisible(true);
            newIngredientLabel.setVisible(true);
            addNewIngredientButton.setVisible(true);
        }
        String oldIngredient = ingredientsComboBox.getSelectedItem().toString();
        ingredientsComboBox.removeItemAt(ingredientsComboBox.getSelectedIndex());
        unusedIngredientComboBox.addItem(oldIngredient);
        if (ingredientsComboBox.getItemCount() == 0) {  // If deleting this unusedingredient from the unused ingredients list resulted in an empty combo box, then hide the relevant guis.
            ingredientsComboBox.setVisible(false);
            removeIngredientButton.setVisible(false);
            ingredientsLabel.setVisible(false);
        }
    }//GEN-LAST:event_removeIngredientButtonActionPerformed

    private void showFilteredRecipes(String ingredient, String category) {
        
        // clean out the text area
        filteredRecipesTextArea.setText("");
        
        List<String> recipesFilteredByIngredient = new ArrayList<String>();
        List<String> recipesFilteredByCategory = new ArrayList<String>();

        if (ingredient.equals("-- PICK --") && category.equals("-- PICK --")) {
            // do nothing
        } else if (ingredient.equals("-- PICK --")) {  // filter by category

            conn = ConnectDb.setupConnection();
            try {
                String sqlStatement = "select title from RECIPES where category = '" + category + "' order by title";
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                rs = (OracleResultSet) pst.executeQuery();
                while (rs.next()) {
                    filteredRecipesTextArea.append("\n" + rs.getString(1));
                }

            } catch (Exception ex) {
                System.out.println("ERROR: " + ex);
            } finally {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
            }
        } else if (category.equals("-- PICK --")) {  // filter by ingredient

            conn = ConnectDb.setupConnection();
            try {
                String sqlStatement = "select recipeTitle from CALLSFOR where ingredientName = '" + ingredient + "' order by recipeTitle";
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                rs = (OracleResultSet) pst.executeQuery();
                while (rs.next()) {
                    filteredRecipesTextArea.append("\n" + rs.getString(1));
                }

            } catch (Exception ex) {
                System.out.println("ERROR: " + ex);
            } finally {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
            }
        } else {  // filter by both

            conn = ConnectDb.setupConnection();
            try {
                String sqlStatement = "select recipeTitle from CALLSFOR where ingredientName = '" + ingredient + "' order by recipeTitle";
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                rs = (OracleResultSet) pst.executeQuery();
                while (rs.next()) {
                    String rSelection = rs.getString(1);
                    recipesFilteredByIngredient.add(rSelection);
                }

            } catch (Exception ex) {
                System.out.println("ERROR: " + ex);
            } finally {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
            }

            // get recipes filtered by category
            conn = ConnectDb.setupConnection();
            try {
                String sqlStatement = "select title from RECIPES where category = '" + category + "' order by title";
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                rs = (OracleResultSet) pst.executeQuery();
                while (rs.next()) {
                    String rSelection = rs.getString(1);
                    recipesFilteredByCategory.add(rSelection);
                }

            } catch (Exception ex) {
                System.out.println("ERROR: " + ex);
            } finally {
                ConnectDb.close(rs);
                ConnectDb.close(pst);
                ConnectDb.close(conn);
            }

            for (int i = 0; i < recipesFilteredByCategory.size(); i++) {
                if (recipesFilteredByIngredient.contains(recipesFilteredByCategory.get(i))) {
                    filteredRecipesTextArea.append("\n" + recipesFilteredByCategory.get(i));
                }
            }

        }

    }

    private void unusedIngredientComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unusedIngredientComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unusedIngredientComboBoxActionPerformed

    private void categoryFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryFilterComboBoxActionPerformed
        if (ingredientsFilterComboBox.getItemCount() > 0 && categoryFilterComboBox.getItemCount() > 0) {
            showFilteredRecipes(ingredientsFilterComboBox.getSelectedItem().toString(), categoryFilterComboBox.getSelectedItem().toString());
        }
    }//GEN-LAST:event_categoryFilterComboBoxActionPerformed

    private void ingredientsFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ingredientsFilterComboBoxActionPerformed
        if (ingredientsFilterComboBox.getItemCount() > 0 && categoryFilterComboBox.getItemCount() > 0) {
            showFilteredRecipes(ingredientsFilterComboBox.getSelectedItem().toString(), categoryFilterComboBox.getSelectedItem().toString());
        }
    }//GEN-LAST:event_ingredientsFilterComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewIngredientButton;
    private javax.swing.JButton addNewRecipeButton;
    private javax.swing.JComboBox<String> categoryFilterComboBox;
    private javax.swing.JLabel categoryLabel;
    private javax.swing.JButton deleteRecipeButton;
    private javax.swing.JButton editRecipeButton;
    private javax.swing.JPanel existingRecipesPanel2;
    private javax.swing.JTextArea filteredRecipesTextArea;
    private javax.swing.JPanel filtersPanel;
    private javax.swing.JComboBox<String> ingredientsComboBox;
    private javax.swing.JComboBox<String> ingredientsFilterComboBox;
    private javax.swing.JLabel ingredientsLabel;
    private javax.swing.JLabel instructionsLabel;
    private javax.swing.JTextArea instructionsTextArea;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel newIngredientLabel;
    private javax.swing.JTextField recipeCategoryTextField;
    private javax.swing.JLabel recipeTitleLabel;
    private javax.swing.JTextField recipeTitleTextField;
    private javax.swing.JComboBox<String> recipesComboBox;
    private javax.swing.JButton removeIngredientButton;
    private javax.swing.JComboBox<String> unusedIngredientComboBox;
    // End of variables declaration//GEN-END:variables
}
