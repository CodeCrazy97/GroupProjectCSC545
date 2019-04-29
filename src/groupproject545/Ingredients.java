package groupproject545;

import java.sql.Connection;
import java.util.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 * Ingredients class implements functions to work with ingredients table
 */
public class Ingredients {

    //
    static Connection conn = null;
    static OraclePreparedStatement pst = null;
    static OracleResultSet rs = null;

    private String name;

    private String foodGroup;
    private boolean inStock;
    private String nutritionFacts;

    /**
     * Constructor
     */
    public Ingredients() {
        this.name = "";
        this.foodGroup = "";
        this.inStock = false;
        this.nutritionFacts = "";
    }

    public Ingredients(String name, String foodGroup, boolean inStock, String nutritionFacts) {
        this.name = name;
        this.foodGroup = foodGroup;
        this.inStock = inStock;
        this.nutritionFacts = nutritionFacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoodGroup() {
        return foodGroup;
    }

    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public String getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(String nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    /**
     * Gets list of inStock ingredients
     *
     * @return list of Ingredients
     */
    public List<Ingredients> getInStockIngredients() {
        List<Ingredients> inStockIngredients = new ArrayList<Ingredients>();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from INGREDIENTS where inStock='Y'";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String foodGroup = rs.getString(2);
                String inStock = rs.getString(3);
                String nutritionFacts = rs.getString(4);
                // ternary operator
                boolean isInStock = inStock.equals("Y");
                inStockIngredients.add(new Ingredients(name, foodGroup, isInStock, nutritionFacts));
            }
            System.out.println("5");

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return inStockIngredients;
    }

    /**
     * Gets all ingredients
     *
     * @return List of all ingredients
     */
    public List<Ingredients> getAllIngredients() {
        conn = ConnectDb.setupConnection();
        List<Ingredients> AllIngredients = new ArrayList<Ingredients>();
        try {
            String sqlStatement = "select * from INGREDIENTS order by name";  // Get all ingredients, sorting by name.

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String foodGroup = rs.getString(2);
                String inStock = rs.getString(3);
                String nutritionFacts = rs.getString(4);
                // ternary operator
                boolean isInStock = inStock.equals("Y");
                AllIngredients.add(new Ingredients(name, foodGroup, isInStock, nutritionFacts));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return AllIngredients;
    }

       /**
     * Updates the ingredient in the database
     * TODO: Check if it actually works
     * @author Chris
     */
    public void update() throws Exception {
        Connection conn = ConnectDb.setupConnection();
        OraclePreparedStatement updateIngredient = null;
        
        try {
            String sqlStatement = "Update INGREDIENTS set foodGroup=?, inStock=?, nutritionFacts=? where name=?";
            updateIngredient = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            updateIngredient.setString(1, this.foodGroup.replace("'", "''"));
            updateIngredient.setString(2, this.inStock ? "Y": "N");
            updateIngredient.setString(3, this.nutritionFacts.replace("'", "''"));
            updateIngredient.setString(4, this.name.replace("'", "''"));
            updateIngredient.executeUpdate();
            conn.commit();
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    /**
     * Adds ingredient to the database
     * @throws Exception 
     */
    public void add() throws Exception {
        Connection conn = ConnectDb.setupConnection();
        OraclePreparedStatement addIngredient = null;
        try {
            String sqlStatement = "Insert into INGREDIENTS (name, foodGroup, inStock, nutritionFacts) " 
                    + "values (?, ?, ?, ?)";
            addIngredient = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            addIngredient.setString(1, this.name.replace("'", "''"));
            addIngredient.setString(2, this.foodGroup.replace("'", "''"));
            addIngredient.setString(3, this.inStock ? "Y": "N");
            addIngredient.setString(4, this.nutritionFacts.replace("'", "''"));
            addIngredient.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
