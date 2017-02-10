package fi.tamk.teemu.fooddiary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 20.4.2016
 * @version 2.0
 * @since 1.8
 */
public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }
}
