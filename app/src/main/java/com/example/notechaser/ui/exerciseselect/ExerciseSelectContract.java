package com.example.notechaser.ui.exerciseselect;

import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

public class ExerciseSelectContract {

    interface View extends BaseView<Presenter> {

        void showExerciseConfigurationActivity(String exerciseType);

    }

    interface Presenter extends BasePresenter {

        void exerciseTypeSelected(String exerciseType);

    }

}
