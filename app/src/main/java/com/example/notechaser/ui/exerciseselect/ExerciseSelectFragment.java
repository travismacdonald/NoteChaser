package com.example.notechaser.ui.exerciseselect;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notechaser.Constants;
import com.example.notechaser.R;
import com.example.notechaser.ui.exerciseconfiguration.ExerciseConfigurationActivity;


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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.exercise_select_fragment, container, false);
        setupView();
        return mRoot;
    }


    @Override
    public void setPresenter(ExerciseSelectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showExerciseConfigurationActivity(String exerciseType) {
        Intent intent = new Intent(getContext(), ExerciseConfigurationActivity.class);
        intent.putExtra(Constants.EXERCISE_TYPE_TAG, exerciseType);
        startActivity(intent);
    }

    private void setupView() {
        mSingleNoteButton = mRoot.findViewById(R.id.singlenote_button);
        mSingleNoteButton.setTag(Constants.SINGLE_NOTE);
        mSingleNoteButton.setOnClickListener(v ->
            mPresenter.exerciseTypeSelected((String) mSingleNoteButton.getTag())
        );

        mIntervalButton = mRoot.findViewById(R.id.interval_button);
        mIntervalButton.setTag(Constants.INTERVAL);
        mIntervalButton.setOnClickListener(v ->
                mPresenter.exerciseTypeSelected((String) mIntervalButton.getTag())
        );

        mHarmonicButton = mRoot.findViewById(R.id.harmonic_button);
        mHarmonicButton.setTag(Constants.HARMONIC);
        mHarmonicButton.setOnClickListener(v ->
                mPresenter.exerciseTypeSelected((String) mHarmonicButton.getTag())
        );

        mScaleButton = mRoot.findViewById(R.id.scale_button);
        mScaleButton.setTag(Constants.SCALE);
        mScaleButton.setOnClickListener(v ->
                mPresenter.exerciseTypeSelected((String) mScaleButton.getTag())
        );

        mMelodicButton = mRoot.findViewById(R.id.melodic_button);
        mMelodicButton.setTag(Constants.MELODIC);
        mMelodicButton.setOnClickListener(v ->
                mPresenter.exerciseTypeSelected((String) mMelodicButton.getTag())
        );
    }

}
