package fi.tamk.teemu.fooddiary.tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import java.text.ParseException;

/**
 *
 * Custom TextWatcher for checking users input.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 4.5.2016
 * @version 2.0
 * @since 1.8
 */
public class MyTextWatcher implements TextWatcher {

    /** pointer to button */
    private Button pointer;
    /** flag for checking if text in food name field */
    private Boolean isNameField;
    /** static check for if name is ok */
    public static boolean NAME_OK;
    /** static check for if amount is ok */
    public static boolean AMOUNT_OK;

    /**
     * Explicit constructor.
     *
     * @param button pointer to AddButton in AddFoodDialog
     * @param flag if it is about name field
     */
    public MyTextWatcher(Button button, Boolean flag) {

        isNameField = flag;
        pointer = button;
    }

    /**
     * Default constructor
     */
    public MyTextWatcher() {

    }

    /**
     * Called when clicked editable text.
     *
     * @param s text in field
     * @param start index of beginning of new text
     * @param count number of characters
     * @param after new text length
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * Called when editing text.
     *
     * @param s text in field
     * @param start index of beginning of new text
     * @param count number of characters
     * @param before length of old text
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (isNameField) {
            NAME_OK = false;
            pointer.setEnabled(false);
        } else {

            try {
                int amount = Integer.parseInt(s.toString());
                if (amount <= 0) {
                    AMOUNT_OK = false;
                } else {
                    AMOUNT_OK = true;
                    if (MyTextWatcher.NAME_OK) {
                        pointer.setEnabled(true);
                    }
                }
            } catch (NumberFormatException e) {
                AMOUNT_OK = false;
                pointer.setEnabled(false);
            }
        }
    }

    /**
     * Called when stopped editing text
     *
     * @param s somewhere within s, the text has been changed
     */
    @Override
    public void afterTextChanged(Editable s) {

    }
}
