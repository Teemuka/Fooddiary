package fi.tamk.teemu.fooddiary.models;

/**
 *
 * Master class of food items.
 *
 * Contains only setters, getters and commons attributes
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 29.4.2016
 * @version 2.0
 * @since 1.8
 */
public class FoodItem {

    /** Name of the food */
    protected String name;
    /** Food's protein amount */
    protected int prot;
    /** Food's carbohydrate amount */
    protected int carb;
    /** Food's fat amount */
    protected int fat;
    /** Food's calories */
    protected int cal;

    /**
     * Returns calories of food.
     *
     * @return <code>cal</code> calories
     */
    public int getCal() {return cal;}

    /**
     * Sets food's calories.
     *
     * @param cal amount of calories
     */
    public void setCal(int cal) {this.cal = cal;}

    /**
     * Returns name of the food.
     *
     * @return <code>name</code> name of the food
     */
    public String getName() {return name;}

    /**
     * Sets food's name
     *
     * @param name name of the food
     */
    public void setName(String name) {this.name = name;}

    /**
     * Returns food's proteins.
     *
     * @return <code>prot</code> amount of proteins
     */
    public int getProt() {return prot;}

    /**
     * Sets food's proteins.
     *
     * @param prot proteins of the food
     */
    public void setProt(int prot) {this.prot = prot;}

    /**
     * Returns carbohydrates of the food.
     *
     * @return <code>carb</code> carbohydrates of the food
     */
    public int getCarb() {return carb;}

    /**
     * Sets food's carbohydrates.
     *
     * @param carb carbohydrates of the food
     */
    public void setCarb(int carb) {this.carb = carb;}

    /**
     * Returns fats of the food.
     *
     * @return <code>fat</code> fats of the food
     */
    public int getFat() {return fat;}

    /**
     * Sets fats of the food.
     *
     * @param fat fats of the food
     */
    public void setFat(int fat) {this.fat = fat;}
}
