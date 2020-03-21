package com.example.notechaser.activities.exerciseselect;

import com.example.notechaser.activities.eartraining.EarTrainingContract;

public class ExerciseSelectPresenter implements ExerciseSelectContract.Presenter {

    ExerciseSelectContract.View mView;

    public ExerciseSelectPresenter(ExerciseSelectContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Nothing needed
    }

    @Override
    public void stop() {
        // Nothing needed
    }

    @Override
    public void exerciseTypeSelected(String exerciseType) {
        mView.showExerciseConfigurationActivity(exerciseType);
    }
}
