package groupproject545;

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

}
