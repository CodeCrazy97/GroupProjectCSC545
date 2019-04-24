package groupproject545;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.*;

/*
Class to hold all the meals served on certain days (Sunday, Monday, etc.) during
certain meals (breakfast, brunch, lunch, etc.) for a specified meal plan.
 */
public class MealDays {

    private String mealPlanTitle;
    
    private String mealName;
    private String mealTitle;
    private String dayOfWeek;

    public MealDays(String mealPlanTitle) {
        this.mealPlanTitle = mealPlanTitle;

        // query the database and initialize the meal titles, days, and meal names.
    }

    /*
    mealTitle: the title of the meal to be served (e.g. "Thanksgiving meal", "My Favorite Spaghetti Meal", "Mom's ultimate veggie combo")
    mealName: the name of the meal that the meal title is served in (e.g. "Lunch", "Dinner", "Snack")
    dayOfWeek: the day of the week that the meal is served on (e.g. "Sunday", "Monday", etc.)
     */
    public MealDays(String mealTitle, String mealName, String dayOfWeek) {
        this.mealName = mealName;
        this.mealTitle = mealTitle;
        this.dayOfWeek = dayOfWeek;
    }
    
    public MealDays(String mealTitle, String mealName, String dayOfWeek, String mealPlanTitle) {
        this.mealName = mealName;
        this.mealTitle = mealTitle;
        this.dayOfWeek = dayOfWeek;
        this.mealPlanTitle = mealPlanTitle;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealTitle() {
        return mealTitle;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    public String getMealPlanTitle() {
        return mealPlanTitle;
    }
    
    public void add() throws Exception{
        Connection conn = ConnectDb.setupConnection();
        OraclePreparedStatement addMealDay = null;
        try {
            String sqlStatement = "Insert into MEALDAY (dayOfWeek, mealTitle, mealName, mealPlanTitle) " 
                    + "values (?, ?, ?, ?)";
            addMealDay = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            addMealDay.setString(1, this.dayOfWeek);
            addMealDay.setString(2, this.mealTitle);
            addMealDay.setString(3, this.mealName);
            addMealDay.setString(4, this.mealPlanTitle);
            addMealDay.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    /**
     * Gets the meal plan based on the primary key
     * @throws Exception 
     */
    public static List<MealDays> getByMealPlanTitle(String title) throws Exception {
        List<MealDays> mealDaysList = new ArrayList<MealDays>();

        Connection conn = ConnectDb.setupConnection();
        String sqlStatement = "select * from MealDay where mealPlanTitle=?";
        OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
        pst.setString(1, title);
        OracleResultSet rs = (OracleResultSet) pst.executeQuery();
        try {
            
            while (rs.next()) {
                String dayOfWeek = rs.getString(1);
                String mealTitle = rs.getString(2);
                String mealName = rs.getString(3);
                String mealPlanTitle = rs.getString(4);
                mealDaysList.add(new MealDays(mealTitle, mealName, dayOfWeek, mealPlanTitle));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return mealDaysList;
    }
    /**
     * Gets mealDays by the Meal Name, don't know if needed
     * @param name
     * @return List of meal days
     * @throws Exception 
     */
    public static List<MealDays> getByMealName(String name) throws Exception {
        List<MealDays> mealDaysList = new ArrayList<>();

        Connection conn = ConnectDb.setupConnection();
        String sqlStatement = "select * from MealDay where mealName=?";
        OraclePreparedStatement pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
        pst.setString(1, name);
        OracleResultSet rs = (OracleResultSet) pst.executeQuery();
        try {
            
            while (rs.next()) {
                String dayOfWeek = rs.getString(1);
                String mealTitle = rs.getString(2);
                String mealName = rs.getString(3);
                String mealPlanTitle = rs.getString(4);
                mealDaysList.add(new MealDays(mealTitle, mealName, dayOfWeek, mealPlanTitle));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            ConnectDb.close(rs);
            ConnectDb.close(pst);
            ConnectDb.close(conn);
        }
        return mealDaysList;
    }
}
