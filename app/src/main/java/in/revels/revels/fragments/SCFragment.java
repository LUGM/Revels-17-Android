package in.revels.revels.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.revels.revels.R;


/**
 * Created by Naman on 10/8/2016.
 */
public class SCFragment extends DialogFragment {

    public SCFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sc_dialog, container, false);

        return view;
    }

}