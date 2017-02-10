package fi.tamk.teemu.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * This class represents the foods that user has added to his/her diary.
 *
 * UserFoodItem implements Serializable so it can be transferred via Bundle and Parcelable
 * so it can be casted from Service to MainActivity.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 26.4.2016
 * @version 2.0
 * @since 1.8
 */
public class UserFoodItem extends FoodItem implements Serializable, Parcelable {

    /** quantity of food */
    private int quantity;

    /**
     * Explicit constructor.
     *
     * Constructs UserFoodItem of GeneralFoodItem and the amount of GeneralFoodItem
     *
     * @see GeneralFoodItem
     *
     * @param item general food item
     * @param quantity amount of general food item
     */
    public UserFoodItem(GeneralFoodItem item, int quantity) {

        this.name = item.getName();

        this.prot = Math.round((quantity / 100f) * item.getProt());
        this.carb = Math.round((quantity/100f) * item.getCarb());
        this.fat = Math.round((quantity/100f) * item.getFat());
        this.cal = Math.round((quantity/100f) * item.getCal());
        this.quantity = quantity;
    }

    /**
     * Explicit constructor.
     *
     * Constructs UserFoodItem of name of the food and the amount of nutritions.
     *
     * This constructor is used when fetching data from server
     *
     * @see GeneralFoodItem
     *
     * @param name name of food
     * @param quantity amount of food
     * @param prot amount of protein
     * @param carb amount of carbohydrates
     * @param fat amount of fat
     * @param cal amount of calories
     */
    public UserFoodItem(String name, int quantity, int prot, int carb, int fat, int cal) {

        this.name = name;
        this.setQuantity(quantity);
        this.carb = carb;
        this.fat = fat;
        this.prot = prot;
        this.cal = cal;
    }

    /**
     * Constructor needed for parsing object
     *
     * @param parcel Container for a message that can be sent through an IBinder
     */
    public UserFoodItem(Parcel parcel) {

        this.name = parcel.readString();
        this.prot = parcel.readInt();
        this.carb = parcel.readInt();
        this.fat = parcel.readInt();
        this.quantity = parcel.readInt();
        this.cal = parcel.readInt();
    }

    /**
     * Returns quantity of UserFoodItem.
     *
     * @return <code>quantity</code>
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity of food.
     *
     * @param quantity quantity of food
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns name of food.
     *
     * @return <code>name</code>
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes attributes to parcel.
     *
     * @param dest Container for a message that can be sent through an IBinder
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.prot);
        dest.writeInt(this.carb);
        dest.writeInt(this.fat);
        dest.writeInt(this.quantity);
        dest.writeInt(this.cal);
    }

    /**
     * Creates parcelable object.
     */
    public static Creator<UserFoodItem> CREATOR = new Creator<UserFoodItem>() {

        @Override
        public UserFoodItem createFromParcel(Parcel source) {
            return new UserFoodItem(source);
        }

        @Override
        public UserFoodItem[] newArray(int size) {
            return new UserFoodItem[size];
        }
    };
}
