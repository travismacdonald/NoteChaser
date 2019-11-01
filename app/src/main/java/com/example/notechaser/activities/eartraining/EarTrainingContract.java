package com.example.notechaser.activities.eartraining;

import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNumNotesHeard(int curCount, int totalCount);

//        // Reset after answer correct or incorrect
//        void showNoNotesHeard(int totalNotes);

        void showAnswerResult(boolean answerIsCorrect);

        void showAnswerCorrect();

        void showAnswerIncorrect();

        void showListening();

        void showPatternIsPlaying();

        void showInactive();

    }

    interface Presenter extends BasePresenter {

        void setLowerBound(int lowerBound);

        void setUpperBound(int upperBound);

        // Todo: method is only for testing; delete when not needed
        void playRandomPattern();

        void startEarTrainingExercise();

        void stopEarTrainingExercise();

    }

}
