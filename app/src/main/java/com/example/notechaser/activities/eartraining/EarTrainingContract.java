package com.example.notechaser.activities.eartraining;

import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNoteHeard();

        // Reset after answer correct or incorrect
        void showNoNotesHeard();

        void showAnswerResult(boolean answerIsCorrect);

        void showListening();

        void showPatternIsPlaying();

        void showInactive();

    }

    interface Presenter extends BasePresenter {

        void setLowerBound(int lowerBound);

        void setUpperBound(int upperBound);

        // Todo: method is only for testing; delete when not needed
        void playRandomPattern();

    }

}
