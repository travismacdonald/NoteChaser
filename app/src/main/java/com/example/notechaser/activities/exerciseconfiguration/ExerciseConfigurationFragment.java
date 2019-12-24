package com.example.notechaser.activities.exerciseconfiguration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notechaser.R;
import com.example.notechaser.data.ModeCollection;


public class ExerciseConfigurationFragment extends Fragment implements ExerciseConfigurationContract.View{

    private View mRoot;

    public ExerciseConfigurationFragment() {
        // Required empty public constructor
    }

    static public ExerciseConfigurationFragment newInstance() {
        return new ExerciseConfigurationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.exercise_configuration_fragment, container, false);
        return mRoot;
    }

    @Override
    public void showEarTrainingActivity() {

    }

    @Override
    public void loadModes(ModeCollection modeCollection) {

    }

    @Override
    public void showSessionValid() {

    }

    @Override
    public void showSessionInvalid() {

    }

    @Override
    public void setPresenter(ExerciseConfigurationContract.Presenter presenter) {

    }
}
