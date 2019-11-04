package com.example.notechaser.activities.exerciseconfiguration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notechaser.R;


public class ExerciseConfigurationFragment extends Fragment {

    private View mRoot;

    public ExerciseConfigurationFragment() {
        // Required empty public constructor
    }

    public ExerciseConfigurationFragment newInstance() {
        return new ExerciseConfigurationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.exercise_configuration_fragment, container, false);
        return mRoot;
    }

}
