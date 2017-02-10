package fi.tamk.teemu.fooddiary.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import fi.tamk.teemu.fooddiary.adapters.FoodItemArrayAdapter;
import fi.tamk.teemu.fooddiary.R;
import fi.tamk.teemu.fooddiary.interfaces.MyDialogListener;
import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;
import fi.tamk.teemu.fooddiary.tools.MyTextWatcher;

/**
 * AddFoodDialogFragment class implements the dialog which shows up when user adds food to diary.
 *
 * Extending DialogFragment is mandatory for custom dialogs and MyDialogListener interface
 * takes care of passing data to MainActivity.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 25.4.2016
 * @version 2.0
 * @since 1.8
 */
public class AddFoodDialogFragment extends DialogFragment implements MyDialogListener {

    private AutoCompleteTextView autoCompleteTextView;
    /** users input for food amount */
    private EditText foodAmount;
    /** positive answer button */
    private Button addButton;
    /** negative answer button */
    private Button cancelButton;
    /** list of all foods */
    private ArrayList<GeneralFoodItem> listOfFoods;
    /** food that user has selected */
    private GeneralFoodItem selectedFood;

    /**
     * Creates new static instance of this Dialog.
     *
     * @param list1 list of all foods
     * @return <code>f</code> static instance of this class
     */
    public static AddFoodDialogFragment newInstance(ArrayList<GeneralFoodItem> list1) {

        AddFoodDialogFragment f = new AddFoodDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("list", list1);
        f.setArguments(args);

        return f;
    }

    /**
     * Creates the visible view of dialog
     *
     * AutoCompleteTextView forces user to use only preselected foods, so data is always available
     * for foods. AutoCompleteTextView uses also custom TextWatcher (MyTextWatcher) so adding
     * empty food is not possible.
     *
     * Also user has to give amount of selected food.
     *
     * Add button is not active at first. It becomes active after user has given proper info.
     *
     * @see MyTextWatcher
     *
     * @param inflater Instantiates a layout XML file into its corresponding View objects
     * @param container Special view that can contain other views (called children.)
     * @param savedInstanceState A mapping from String values to various Parcelable types
     * @return view view of current DialogFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().getWindow().setGravity(Gravity.TOP);

        View view = inflater.inflate(R.layout.fragment_addfood_dialog, container);
        MyTextWatcher.NAME_OK = false;
        MyTextWatcher.AMOUNT_OK = false;

        listOfFoods = new ArrayList<>();
        listOfFoods.addAll((ArrayList<GeneralFoodItem>) getArguments().getSerializable("list"));

        autoCompleteTextView = (AutoCompleteTextView)view.findViewById(R.id.dialogFoodName);
        foodAmount = (EditText)view.findViewById(R.id.dialogFoodAmount);
        addButton = (Button) view.findViewById(R.id.addFood);
        addButton.setEnabled(false);
        cancelButton = (Button) view.findViewById(R.id.cancelFood);

        final ArrayAdapter<GeneralFoodItem> adapter = new FoodItemArrayAdapter(getActivity(), listOfFoods);

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedFood = listOfFoods.get(position);
                autoCompleteTextView.setText(selectedFood.getName());
                MyTextWatcher.NAME_OK = true;
                if (MyTextWatcher.AMOUNT_OK) {
                    addButton.setEnabled(true);
                }
            }
        });

        autoCompleteTextView.addTextChangedListener(new MyTextWatcher(addButton, true));
        foodAmount.addTextChangedListener(new MyTextWatcher(addButton, false));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialogListener listener = (MyDialogListener) getActivity();
                listener.onPositiveAddFoodAnswer(selectedFood, Integer.parseInt(foodAmount.getText().toString()));
                AddFoodDialogFragment.this.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddFoodDialogFragment.this.dismiss();
            }
        });

        return view;
    }

    /**
     * Does nothing.
     *
     * Used only in MainActivity
     *
     *@see fi.tamk.teemu.fooddiary.MainActivity
     *
     * @param selectedFood selected food
     * @param amount amount of food
     */
    @Override
    public void onPositiveAddFoodAnswer(GeneralFoodItem selectedFood, int amount) {

    }

    /**
     * Does nothing.
     *
     * Used only in MainActivity
     *
     * @see fi.tamk.teemu.fooddiary.MainActivity
     *
     * @param username  name of the user
     */
    @Override
    public void onPositiveAddUserAnswer(String username) {

    }
}
