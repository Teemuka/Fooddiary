package fi.tamk.teemu.fooddiary.models;

import java.io.Serializable;

/**
 * GeneralFoodItem which represents the foods in the drop down menu.
 *
 * Class implements Serializable so it can be transferred via Bundles.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 29.4.2016
 * @version 2.0
 * @since 1.8
 */
public class GeneralFoodItem extends FoodItem implements Serializable {

    /**
     * Explicit constructor.
     *
     * Constructor instantiates the general food item
     *
     * @param name name of the food
     * @param prot food's protein ampunt
     * @param carb food's carbohydrate amount
     * @param fat food's fat amount
     * @param cal food's calories
     */
    public GeneralFoodItem(String name, int prot, int carb, int fat, int cal) {

        this.name = name;
        this.prot = prot;
        this.carb = carb;
        this.fat = fat;
        this.cal = cal;

    }

    /**
     * Returns name of the food.
     *
     * @return <code>name</code>
     */
    @Override
    public String toString() {
        return this.name;
    }
}
