package fi.tamk.teemu.fooddiary.interfaces;


import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;

/**
 *
 * Interface for passing data from fragments to MainActivity
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 25.4.2016
 * @version 2.0
 * @since 1.8
 */
public interface MyDialogListener {

    /**
     * Passes selected food and amount to MainActivity.
     *
     * @param selectedFood user selected food
     * @param foodAmount amount of food
     */
    void onPositiveAddFoodAnswer(GeneralFoodItem selectedFood, int foodAmount);

    /**
     * Passes name of new user to MainActivity
     *
     * @param username name of the new user
     */
    void onPositiveAddUserAnswer(String username);

}
