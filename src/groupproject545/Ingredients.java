package groupproject545;

import java.sql.Connection;
import java.util.*;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 * Ingredients class implements functions to work with ingredients table
 */
public class Ingredients {

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
     * @return      list of Ingredients
     */
    public List<Ingredients> getInStockIngredients() {
        Connection conn = ConnectDb.setupConnection();
        List<Ingredients> inStockIngredients = new ArrayList<Ingredients>();
        try {
            String sqlStatement = "select name, foodGroup, inStock, nutritionFacts from INGREDIENTS where inStock='Y'";

            OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);

            OracleResultSet rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String foodGroup = rs.getString(2);
                String inStock = rs.getString(3);
                String nutritionFacts = rs.getString(4);
                // ternary operator
                boolean isInStock = inStock.equals("Y");
                inStockIngredients.add(new Ingredients(name, foodGroup, isInStock, nutritionFacts));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return inStockIngredients;
    }

    /**
     * Updates the ingredient in the database
     * TODO
     */
    public void updateIngredient() throws Exception {
        conn = ConnectDb.setupConnection();

    }

}