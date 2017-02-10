package fi.tamk.teemu.fooddiary.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fi.tamk.teemu.fooddiary.R;
import fi.tamk.teemu.fooddiary.interfaces.MyDialogListener;
import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;
import fi.tamk.teemu.fooddiary.tools.MyTextWatcher;

/**
 * UserNameDialogFragment is used to create a dialog where user writes his/her name.
 * Class extends DialogFragment which is mandatory for creating custom Dialogs.
 *
 * UserNameDialogFragment class implements MyDialogListener interface which is used to pass data
 * to MainActivity.
 *
 * Name is user for identification when fetching data from backend.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 11.5.2016
 * @version 2.0
 * @since 1.8
 */
public class UserNameDialogFragment extends DialogFragment implements MyDialogListener {

    /** positive answer button */
    private Button addButton;

    /**
     * Creates static instance of this class
     *
     * @return UserNameDialogFragment
     */
    public static UserNameDialogFragment newInstance() {

        return new UserNameDialogFragment();
    }

    /**
     * Creates the visible view
     *
     * Local EditText et uses custom TextWatcher (MyTextWatcher) for preventing empty username
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

        /** Custom view for this DialogFragment */
        View view = inflater.inflate(R.layout.fragment_username_dialog, container);

        /** EditText (view) where user writes name */
        final EditText et = (EditText)view.findViewById(R.id.dialogUserName);
        addButton = (Button) view.findViewById(R.id.addUser);
        addButton.setEnabled(false);

        /** negative answer button */
        Button cancelButton = (Button) view.findViewById(R.id.cancelUser);

        et.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s != "") {
                    addButton.setEnabled(true);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDialogListener listener = (MyDialogListener) getActivity();
                listener.onPositiveAddUserAnswer(et.getText().toString());
                UserNameDialogFragment.this.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserNameDialogFragment.this.dismiss();

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