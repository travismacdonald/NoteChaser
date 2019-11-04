package com.example.notechaser.activities.exerciseconfiguration;

import com.example.keyfinder.Mode;
import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;
import com.example.notechaser.data.ModeCollection;

public class ExerciseConfigurationContract {

    interface View extends BaseView<Presenter> {

        void showEarTrainingActivity();

        // pass modes to view to show
        void loadModes(ModeCollection modeCollection);

        // Enough space to generate patterns
        void showSessionValid();

        // Not enough space to generate patterns
        void showSessionInvalid();

    }

    interface Presenter extends BasePresenter {

        void startEarTrainingActivity();

        void addMode(Mode mode);

        void removeMode(Mode mode);

    }

}
