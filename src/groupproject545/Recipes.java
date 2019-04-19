package groupproject545;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class Recipes {

    //
    static Connection conn = null;
    static OraclePreparedStatement pst = null;
    static OracleResultSet rs = null;

    private String title;

    private String category;
    private String instructions;

    /**
     * Constructor
     */
    public Recipes() {
        this.title = "";
        this.instructions = "";
        this.category = "";
    }

    public Recipes(String title, String instructions, String category) {
        this.title = title;
        this.instructions = instructions;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getInstructions() {
        return instructions;
    }

    public List<Recipes> getRecipes() {
        List<Recipes> recipes = new ArrayList<Recipes>();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from RECIPES order by title";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String title = rs.getString(1);
                String instructions = rs.getString(2);
                String category = rs.getString(3);
                recipes.add(new Recipes(title, instructions, category));
            }

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return recipes;
    }

    public List<String> getIngredientsUsedInRecipe(String recipeTitle) {
        List<String> ingredients = new ArrayList<String>();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from CALLSFOR where recipeTitle = '" + recipeTitle + "' order by ingredientName";
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String ingredientName = rs.getString(1);
                ingredients.add(ingredientName);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return ingredients;
    }

    public List<String> getIngredientsNotUsedInRecipe(String recipeTitle) {
        List<String> unusedIngredients = new ArrayList<String>();

        conn = ConnectDb.setupConnection();
        try {
            String sqlStatement = "select * from ingredients where name <> all(select ingredientName from CALLSFOR where recipeTitle = '"
                    + recipeTitle + "') order by name";
            System.out.println("Sql: " + sqlStatement);
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            rs = (OracleResultSet) pst.executeQuery();
            while (rs.next()) {
                String ingredientName = rs.getString(1);
                unusedIngredients.add(ingredientName);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return unusedIngredients;
    }

}
