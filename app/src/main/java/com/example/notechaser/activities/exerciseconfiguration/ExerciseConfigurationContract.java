package com.example.notechaser.activities.exerciseconfiguration;

import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

public class ExerciseConfigurationContract {

    interface View extends BaseView<Presenter> {

        void showEarTrainingActivity();

    }

    interface Presenter extends BasePresenter {

        void startEarTrainingActivity();

    }

}
