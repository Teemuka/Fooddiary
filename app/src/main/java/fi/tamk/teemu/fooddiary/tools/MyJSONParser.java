package fi.tamk.teemu.fooddiary.tools;

import android.app.Activity;

import com.google.common.io.ByteStreams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import fi.tamk.teemu.fooddiary.R;
import fi.tamk.teemu.fooddiary.models.FoodStorage;
import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;

/**
 * Custom JSONParser.
 *
 * Parses data from two different JSON files foods.json and nutrition.json and merges the data to
 * array.
 *
 * Parser works in its own thread preventing application for not being usable.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 5.5.2016
 * @version 2.0
 * @since 1.8
 */
public class MyJSONParser implements Runnable {

    /** pointers */
    FoodStorage foodStoragePointer;
    Activity contextPointer;

    /**
     *
     * @param storage pointer to storage where general food item array is
     * @param context pointer to MainActivity
     */
    public MyJSONParser(FoodStorage storage, Activity context) {

        foodStoragePointer = storage;
        contextPointer = context;
    }

    /**
     * Starts when thread is started,
     *
     * Parses the data from two different JSON files and adds food items to array according to
     * JSON files.
     *
     * @deprecated parsing data is too slow because of inconsistency in Finelli's data
     * todo data parsing or data itself must be fixed
     *
     */
    @Override
    public void run() {

        InputStream foodsJsonStream = contextPointer.getResources().openRawResource(R.raw.foods);
        InputStream nutritionsJsonStream = contextPointer.getResources().
                openRawResource(R.raw.nutrition);

        String foodString = "";
        String nutritionString = "";
        JSONObject jsonFoodObject;
        JSONObject jsonNutritionObject;

        try {
            foodString = new String(ByteStreams.toByteArray(foodsJsonStream));
            nutritionString = new String(ByteStreams.toByteArray(nutritionsJsonStream));

        } catch (IOException e) {

            e.printStackTrace();
        }

        if (!foodString.equals("") && !nutritionString.equals("")) {
            try {
                jsonFoodObject = new JSONObject(foodString);
                jsonNutritionObject = new JSONObject(nutritionString);

                JSONArray jsonFoods = jsonFoodObject.getJSONArray("foods");
                JSONArray jsonNutritions = jsonNutritionObject.getJSONArray("nutritions");

                int pointer = 0;

                for(int i = 0; i < jsonFoods.length(); i++) {

                    JSONObject food = jsonFoods.getJSONObject(i);
                    String name = food.getString("FOODNAME").replaceAll("\\s+","");
                    int foodid = food.getInt("FOODID");
                    int prot = 0;
                    int carb = 0;
                    int fat = 0;
                    int cal = 0;

                    for (int j = pointer; j < jsonNutritions.length(); j++) {

                        JSONObject nutrition = jsonNutritions.getJSONObject(j);
                        String nutritionName = nutrition.getString("EUFDNAME");
                        int nutritionid = nutrition.getInt("FOODID");

                        if(foodid == nutritionid) {

                            switch (nutritionName) {
                                case "ENERC":
                                    cal = Math.round(nutrition.getInt("BESTLOC") / 4.1868f);
                                    break;
                                case "CHOAVL":
                                    carb = nutrition.getInt("BESTLOC");
                                    break;
                                case "FAT":
                                    fat = nutrition.getInt("BESTLOC");
                                    break;
                                default:
                                    prot = nutrition.getInt("BESTLOC");
                                    break;
                            }
                        }
                    }

                    pointer = pointer + 3;
                    foodStoragePointer.addAllFoods(new GeneralFoodItem(name, prot, carb, fat, cal));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}