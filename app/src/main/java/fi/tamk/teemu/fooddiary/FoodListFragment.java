package fi.tamk.teemu.fooddiary;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fi.tamk.teemu.fooddiary.adapters.FoodDiaryArrayAdapter;
import fi.tamk.teemu.fooddiary.models.MealSet;
import fi.tamk.teemu.fooddiary.models.UserFoodItem;

/**
 * List of the user's foods in the diary
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 19.4.2016
 * @version 2.0
 * @since 1.8
 */
public class FoodListFragment extends ListFragment {

    /** flag to certain mealset used to create headers */
    int number;
    /** pointer to user's food list */
    ArrayList<UserFoodItem> list;
    /** FoodDiaryArrayAdapter */
    ArrayAdapter<UserFoodItem> arrayAdapter;
    /** pointer to mealset */
    MealSet mealSet;
    /** text view of total nutritions */
    TextView totalNutrition;

    /** strings pointing to resource file */
    String prot;
    String carb;
    String fat;
    String cal;

    /**
     * Creates new static instance of FoodListFragment.
     *
     * @param position flag to mealset
     * @param mealset mealset that position flags
     * @return static instance of FoodListFragment
     */
    public static FoodListFragment newInstance(int position, MealSet mealset) {

        FoodListFragment f = new FoodListFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("mealset", mealset);
        f.setArguments(args);

        return f;
    }

    /**
     * Instantiates variables and pointers.
     *
     * In Androids life cycle this is called before OnCreateView.
     *
     * @param savedInstanceState A mapping from String values to various Parcelable types
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        number = getArguments() != null ? getArguments().getInt("position"):0;
        mealSet = getArguments() != null ? (MealSet)getArguments().getSerializable("mealset"):null;

        if (mealSet != null) {

            list= mealSet.getFoods();
            arrayAdapter = new FoodDiaryArrayAdapter(getContext(), list);
        }

        prot = getContext().getResources().getString(R.string.prot);
        carb = getContext().getResources().getString(R.string.carb);
        fat = getContext().getResources().getString(R.string.fat);
        cal = getContext().getResources().getString(R.string.cal);

    }

    /**
     * Creates the visible view of dialog.
     *
     * Adds FoodDiaryArrayAdapter to the list.
     *
     *
     * @param inflater Instantiates a layout XML file into its corresponding View objects
     * @param container Special view that can contain other views (called children.)
     * @param savedInstanceState A mapping from String values to various Parcelable types
     * @return v view of current FoodListFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food_list, container, false);

        TextView header = (TextView) v.findViewById(R.id.header);
        totalNutrition = (TextView) v.findViewById(R.id.totalNutrition);

        String tempProt = String.valueOf(mealSet.getTotalProt());
        String tempCarb = String.valueOf(mealSet.getTotalCarb());
        String tempFat = String.valueOf(mealSet.getTotalFat());
        String tempCal = String.valueOf(mealSet.getTotalCal());

        totalNutrition.setText(prot + tempProt + " " + carb + tempCarb
                + " " + fat  + tempFat + " " + cal + tempCal);

        header.setText(MainActivity.MEAL_TYPES[number]);

        ListView listView = (ListView) v.findViewById(android.R.id.list);

        if (arrayAdapter != null) {

            listView.setAdapter(arrayAdapter);
        }

        return v;
    }

    /**
     * Updates the view of user's diary.
     *
     * @param mealSet mealset to be updated
     */
    public void updateView(MealSet mealSet) {

        this.mealSet = mealSet;
        if(getView() != null) {

            String tempProt = String.valueOf(mealSet.getTotalProt());
            String tempCarb = String.valueOf(mealSet.getTotalCarb());
            String tempFat = String.valueOf(mealSet.getTotalFat());
            String tempCal = String.valueOf(mealSet.getTotalCal());

            totalNutrition.setText(prot + tempProt + " " + carb + tempCarb
                    + " " + fat  + tempFat + " " + cal + tempCal);
        }




    }
}
