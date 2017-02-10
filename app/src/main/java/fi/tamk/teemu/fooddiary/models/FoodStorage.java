package fi.tamk.teemu.fooddiary.models;

import java.util.ArrayList;

/**
 * This class supposed to be much more broad, but was later contracted to hold only ArrayList
 * of all food items.
 *
 * allFoodItems ArrayList is used to create drop down menu for user
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 26.4.2016
 * @version 2.0
 * @since 1.8
 */
public class FoodStorage {

    /** ArrayList of all foods */
    private ArrayList<GeneralFoodItem> allFoodItems;

    /**
     * Returns all foods.
     *
     * @see GeneralFoodItem
     * @return <code>allFoodItems</code> all foods
     */
    public ArrayList<GeneralFoodItem> getAllFoodItems() {
        return allFoodItems;
    }


    /**
     * Default constructor.
     *
     * Instantiates the allFoodItems ArrayList
     */
    public FoodStorage() {

        allFoodItems = new ArrayList<>();
    }

    /**
     * Adds item to allFoodItems ArrayList
     *
     * @see GeneralFoodItem
     * @param item food item
     */
    public void addAllFoods(GeneralFoodItem item) {
        allFoodItems.add(item);
    }
}
