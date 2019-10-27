package com.example.notechaser.activities.eartraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notechaser.R;

public class EarTrainingFragment extends Fragment {



    public EarTrainingFragment() {
        // Required empty public constructor
    }

    static public EarTrainingFragment newInstance() {
        return new EarTrainingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ear_training, container, false);
    }

}
