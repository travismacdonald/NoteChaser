package com.example.notechaser.activities.eartraining;

import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNumNotesHeard(int curCount, int totalCount);

        void showAnswerCorrect();

        void showListening();

        void showPatternIsPlaying();

        void showInactive();

    }

    interface Presenter extends BasePresenter {

        void setLowerBound(int lowerBound);

        void setUpperBound(int upperBound);


        void startEarTrainingExercise();

        void startEarTrainingExercise(int delay);

        void stopEarTrainingExercise();

    }

}
