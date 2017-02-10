package fi.tamk.teemu.fooddiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fi.tamk.teemu.fooddiary.R;
import fi.tamk.teemu.fooddiary.models.UserFoodItem;

/**
 * Custom array adapter for viewing users foods in diary.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 27.4.2016
 * @version 2.0
 * @since 1.8
 */
public class FoodDiaryArrayAdapter extends ArrayAdapter<UserFoodItem> {

    /** pointer to MainActivity */
    protected Context mContext;
    /** pointer to user's foods */
    protected ArrayList<UserFoodItem> mItems;

    /**
     *
     * @param context MainActivity
     * @param objects user's food for selected meal
     */
    public FoodDiaryArrayAdapter(Context context, ArrayList<UserFoodItem> objects) {
        super(context, R.layout.fragment_food, objects);

        mContext = context;
        mItems = objects;
    }

    /**
     * Creates the view of ListView
     *
     * @param position position of item in list
     * @param convertView The old view to reuse, if possible
     * @param parent Special view that can contain other views (called children.)
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_food, null);
        }

        ((TextView) convertView.findViewById(R.id.userFoodName)).setText(mItems.get(position).toString());
        ((TextView) convertView.findViewById(R.id.userFoodAmount)).setText(String.valueOf(mItems.get(position).getQuantity()));
        ((TextView) convertView.findViewById(R.id.userFoodProt)).setText("Pr: "+String.valueOf(mItems.get(position).getProt()));
        ((TextView) convertView.findViewById(R.id.userFoodCarb)).setText("Hi: "+String.valueOf(mItems.get(position).getCarb()));
        ((TextView) convertView.findViewById(R.id.userFoodFat)).setText("Ra: "+String.valueOf(mItems.get(position).getFat()));
        ((TextView) convertView.findViewById(R.id.userFoodCal)).setText("Ka: "+String.valueOf(mItems.get(position).getCal()));

        return convertView;
    }
}
