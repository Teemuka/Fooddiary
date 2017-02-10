package fi.tamk.teemu.fooddiary.dialogs;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.*;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 20.4.2016
 * @version 2.0
 * @since 1.8
 */
public class DatePickerFragment extends DialogFragment {

    /**
     * Creates new static instance of DialogFragment.
     *
     * This dialog is about selecting date in diary.
     *
     * @param text text to be shown in dialog
     * @param date currently selected date
     * @return static instance of fragment
     */
    public static DatePickerFragment newInstance(String text, Calendar date) {

        Bundle args = new Bundle();
        args.putString("text", text);
        args.putSerializable("date", date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates dialog.
     *
     * @param savedInstanceState A mapping from String values to various Parcelable types
     * @return current dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** Pointer to MainActivity */
        OnDateSetListener listener = (OnDateSetListener) getActivity();

        Calendar date = (Calendar) getArguments().getSerializable("date");

        int year = 0;
        int month = 0;
        int day = 0;

        if (date != null) {

            year = date.get(Calendar.YEAR);
            month = date.get(Calendar.MONTH);
            day = date.get(Calendar.DATE);
        }

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), listener, year, month, day);

        dialog.setMessage(getArguments().getString("text"));

        return  dialog;
    }
}

