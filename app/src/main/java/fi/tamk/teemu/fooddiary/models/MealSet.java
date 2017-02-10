package fi.tamk.teemu.fooddiary.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 4.5.2016
 * @version 2.0
 * @since 1.8
 */
public class MealSet implements Serializable {

    /** user's selected foods */
    ArrayList<UserFoodItem> foods;
    /** total protein amount of user's foods */
    private int totalProt;
    /** total carbohydrate amount of user's foods */
    private int totalCarb;
    /** total fat amount of user's foods */
    private int totalFat;
    /** total calory amount of user's foods */
    private int totalCal;

    /**
     * Default constructor.
     *
     * Instantiates <code>foods</code> ArrayList which present users selected foods
     */
    public MealSet() {

        foods = new ArrayList<>();
    }

    /**
     * Sets total nutrition values of user selected foods.
     *
     * @param foods user selected foods
     */
    public void setTotalValues(ArrayList<UserFoodItem> foods) {

        for(UserFoodItem food : foods) {

            setTotalProt(food.getProt());
            setTotalFat(food.getFat());
            setTotalCarb(food.getCarb());
            setTotalCal(food.getCal());
        }
    }

    /**
     * Returns total protein amount of user's foods.
     *
     * @return <code>totalProt</code>
     */
    public int getTotalProt() {
        return totalProt;
    }

    /**
     * Increases total protein amount.
     *
     * @param totalProt amount to increase
     */
    public void setTotalProt(int totalProt) {
        this.totalProt += totalProt;
    }

    /**
     * Returns total protein amount of user's foods.
     *
     * @return <code>totalCarb</code>
     */
    public int getTotalCarb() {
        return totalCarb;
    }

    /**
     * Increases total carbohydrate amount.
     *
     * @param totalCarb amount to increase
     */
    public void setTotalCarb(int totalCarb) {
        this.totalCarb += totalCarb;
    }

    /**
     * Returns total protein amount of user's foods.
     *
     * @return <code>totalFat</code>
     */
    public int getTotalFat() {
        return totalFat;
    }

    /**
     * Increases total fat amount.
     *
     * @param totalFat amount to increase
     */
    public void setTotalFat(int totalFat) {
        this.totalFat += totalFat;
    }

    /**
     * Returns total protein amount of user's foods.
     *
     * @return <code>totalCal</code>
     */
    public int getTotalCal() {
        return totalCal;
    }

    /**
     * Increases total calorie amount.
     *
     * @param totalCal amount to increase
     */
    public void setTotalCal(int totalCal) {
        this.totalCal += totalCal;
    }

    /**
     * Adds food to user food list
     *
     * @param item user selected food
     */
    public void addFood(UserFoodItem item) {

        foods.add(item);
    }

    /**
     * Returns users food for selected meal
     *
     * @return foods
     */
    public ArrayList<UserFoodItem> getFoods() {
        return foods;
    }

    /**
     * Refreshes data of user food lists.
     *
     * @param newList new food list
     */
    public void refreshData(ArrayList<UserFoodItem> newList) {

        getFoods().clear();
        getFoods().addAll(newList);
        clearTotalValues();
        setTotalValues(newList);

    }

    /**
     * Clears total values.
     */
    private void clearTotalValues() {
        totalProt = 0;
        totalCarb = 0;
        totalFat = 0;
        totalCal = 0;

    }
}
