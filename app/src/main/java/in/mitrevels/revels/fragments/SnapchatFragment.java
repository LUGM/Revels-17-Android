package in.mitrevels.revels.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mitrevels.revels.R;


/**
 * Created by Naman on 10/8/2016.
 */
public class SnapchatFragment extends DialogFragment {

    public SnapchatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snapchat_dialog, container, false);

        return view;
    }

}