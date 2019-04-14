/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package groupproject545;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.event.WindowEvent.WINDOW_CLOSING;
import java.awt.event.WindowListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ethan_2
 */
public class WelcomeScreenGUI extends javax.swing.JPanel {

    /**
     * Creates new form WelcomeScreenGUI
     */
    public WelcomeScreenGUI() {
        initComponents();
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

        ingredientsButton.setText("Ingredients");

        recipesButton.setText("Recipes");

        mealsButton.setText("Meals");

        mealPlansButton.setText("Meal Plans");
        mealPlansButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mealPlansButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mealsButton)
                    .addComponent(ingredientsButton))
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recipesButton)
                    .addComponent(mealPlansButton))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ingredientsButton)
                    .addComponent(recipesButton))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mealsButton)
                    .addComponent(mealPlansButton))
                .addContainerGap(161, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mealPlansButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mealPlansButtonActionPerformed

        // Show the meal plans form.
        JFrame frame = new JFrame("My Meal Planner");

        // Maximize the size of the jframe.
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Below is a custom designed close operation for the MealPlanGUI screen.
        // When the meal planner jframe is closed, reopen a new instance of the
        // WelcomeScreen.
        WindowListener exitListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Show the meal plans form.
                JFrame welcomeScreen = new JFrame("Welcome");

                // Maximize the size of the jframe.
                welcomeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
                welcomeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Exits the program after the JFrame is closed by the user.
                welcomeScreen.add(new WelcomeScreenGUI());
                welcomeScreen.pack();
                welcomeScreen.setLocationRelativeTo(null);
                welcomeScreen.setVisible(true);
            }
        };
        frame.addWindowListener(exitListener);  // Add the custom designed listener.

        frame.add(new MealPlanGUI());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).dispose();  // Close the welcome menu.
    }//GEN-LAST:event_mealPlansButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ingredientsButton;
    private javax.swing.JButton mealPlansButton;
    private javax.swing.JButton mealsButton;
    private javax.swing.JButton recipesButton;
    // End of variables declaration//GEN-END:variables
}
