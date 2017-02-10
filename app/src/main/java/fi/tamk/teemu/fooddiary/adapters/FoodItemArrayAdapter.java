package fi.tamk.teemu.fooddiary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import fi.tamk.teemu.fooddiary.R;
import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;

/**
 * FoodItemArrayAdapter is adapter for drop down foods when user adds new food.
 *
 * Static ViewHolder is part view holder pattern. Because there is over 3000 foods in the list
 * view holder pattern is needed for keeping the drop down lighter for phone.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 29.4.2016
 * @version 2.0
 * @since 1.8
 */
public class FoodItemArrayAdapter extends ArrayAdapter<GeneralFoodItem> {

    static class ViewHolder {

        TextView name;
        TextView prot;
        TextView carb;
        TextView fat;
        TextView cal;
    }

    /** pointer */
    protected Context mContext;

    /** holds all food items */
    protected ArrayList<GeneralFoodItem> mItems;

    public FoodItemArrayAdapter(Context context, ArrayList<GeneralFoodItem> objects) {
        super(context, R.layout.fragment_food, objects);

        mItems = new ArrayList<>();
        mItems.addAll(objects);

        mContext = context;

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

        ViewHolder viewHolder;

        if(convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.autocomplete_list_view_row,
                    null);

            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.foodItemSuggestion);
            viewHolder.prot = (TextView) convertView.findViewById(R.id.generalFoodItemProt);
            viewHolder.carb = (TextView) convertView.findViewById(R.id.generalFoodItemCarb);
            viewHolder.fat = (TextView) convertView.findViewById(R.id.generalFoodItemFat);
            viewHolder.cal = (TextView) convertView.findViewById(R.id.generalFoodItemCal);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        GeneralFoodItem foodItem = getItem(position);

        if (foodItem != null) {

            String prot = String.valueOf(foodItem.getProt());
            String carb = String.valueOf(foodItem.getCarb());
            String fat = String.valueOf(foodItem.getFat());
            String cal = String.valueOf(foodItem.getCal());

            viewHolder.name.setText(foodItem.getName());
            viewHolder.prot.setText(getContext().getResources().getString(R.string.prot) + prot);
            viewHolder.carb.setText(getContext().getResources().getString(R.string.carb) + carb);
            viewHolder.fat.setText(getContext().getResources().getString(R.string.fat) + fat);
            viewHolder.cal.setText(getContext().getResources().getString(R.string.cal) + cal);

        }

        return convertView;
    }

    /**
     * Inner class for custom filter.
     *
     * Now if typed text does not match any food it will show all foods.
     *
     * Also the typed text does not need to match the beginning of food's name
     */
    private Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((GeneralFoodItem)resultValue).getName();
        }

        /**
         * Filters suggestions according to typed text.
         *
         * @param constraint typed text
         * @return resulted items
         */
        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                ArrayList<GeneralFoodItem> suggestions = new ArrayList<GeneralFoodItem>();
                for (GeneralFoodItem foodItem : mItems) {
                    //"contains" to "startsWith" if only wanted starting matches
                    if (foodItem.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(foodItem);
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
            }

            return results;
        }

        /**
         * Shows results.
         *
         * @param constraint typed text
         * @param results resulted items
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {

                addAll((ArrayList<GeneralFoodItem>) results.values);
            } else {
                addAll(mItems);
            }
            notifyDataSetChanged();
        }
    };

    /**
     * Returns the custom filter.
     *
     * @return filter
     */
    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
