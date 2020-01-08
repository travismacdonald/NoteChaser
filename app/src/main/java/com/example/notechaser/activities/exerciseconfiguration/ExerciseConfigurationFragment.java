package com.example.notechaser.activities.exerciseconfiguration;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notechaser.R;
import com.example.notechaser.activities.eartraining.EarTrainingActivity;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;


public class ExerciseConfigurationFragment extends Fragment implements ExerciseConfigurationContract.View{

    // ****************
    // MEMBER VARIABLES
    // ****************

    private View mRoot;

    private ExerciseConfigurationContract.Presenter mPresenter;

    /**
     * Generator that gets configured for the ear training activity.
     */
    private PatternEngine mPatternEngine;

    // ************
    // CONSTRUCTORS
    // ************

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
        mRoot.findViewById(R.id.banacos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test_loadBanacosMethod();
                showEarTrainingActivity();
            }
        });
        return mRoot;
    }


    @Override
    public void showEarTrainingActivity() {
        Intent intent = new Intent(getContext(), EarTrainingActivity.class);
        // Todo: will have to see if this works correctly
        intent.putExtra("pattern_generator", (Parcelable) mPatternEngine);
        startActivity(intent);
    }

    @Override
    public void showModes(ModeCollection modeCollection) {

    }

    @Override
    public void showSessionValid() {

    }

    @Override
    public void showSessionInvalid() {

    }

    @Override
    public void setPresenter(ExerciseConfigurationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * This method simulates the user choosing the Banacos ear training method
     */
    private void test_loadBanacosMethod() {

    }

}
