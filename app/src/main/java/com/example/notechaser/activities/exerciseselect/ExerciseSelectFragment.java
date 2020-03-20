package com.example.notechaser.activities.exerciseselect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.notechaser.Constants;
import com.example.notechaser.R;


public class ExerciseSelectFragment extends Fragment implements ExerciseSelectContract.View {

    ExerciseSelectContract.Presenter mPresenter;

    private View mRoot;

    private Button mSingleNoteButton;
    private Button mIntervalButton;
    private Button mHarmonicButton;
    private Button mScaleButton;
    private Button mMelodicButton;


    public ExerciseSelectFragment() {
        // todo: find out what to do with this
    }

    static public ExerciseSelectFragment newInstance() {
        return new ExerciseSelectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.exercise_select_fragment, container, false);
        setupView();
        return mRoot;

    }




    @Override
    public void setPresenter(ExerciseSelectContract.Presenter presenter) {

    }


    private void setupView() {
        mSingleNoteButton = mRoot.findViewById(R.id.singlenote_button);
        mSingleNoteButton.setTag(Constants.SINGLE_NOTE);
        mSingleNoteButton.setOnClickListener(v ->
            selectExerciseType((String) mSingleNoteButton.getTag())
        );

        mIntervalButton = mRoot.findViewById(R.id.interval_button);
        mIntervalButton.setTag(Constants.INTERVAL);
        mIntervalButton.setOnClickListener(v ->
                selectExerciseType((String) mIntervalButton.getTag())
        );

        mHarmonicButton = mRoot.findViewById(R.id.harmonic_button);
        mHarmonicButton.setTag(Constants.HARMONIC);
        mHarmonicButton.setOnClickListener(v ->
                selectExerciseType((String) mHarmonicButton.getTag())
        );

        mScaleButton = mRoot.findViewById(R.id.scale_button);
        mScaleButton.setTag(Constants.SCALE);
        mScaleButton.setOnClickListener(v ->
                selectExerciseType((String) mScaleButton.getTag())
        );

        mMelodicButton = mRoot.findViewById(R.id.melodic_button);
        mMelodicButton.setTag(Constants.MELODIC);
        mMelodicButton.setOnClickListener(v ->
                selectExerciseType((String) mMelodicButton.getTag())
        );
    }

    private void selectExerciseType(String exerciseType) {
        // todo: start config activity with exercise type
        Toast.makeText(mRoot.getContext(), exerciseType, Toast.LENGTH_SHORT).show();
    }
}
