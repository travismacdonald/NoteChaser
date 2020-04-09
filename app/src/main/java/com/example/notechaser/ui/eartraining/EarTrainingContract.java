package com.example.notechaser.ui.eartraining;


import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;
import com.example.notechaser.patterngenerator.Note;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNumNotesHeard(int curCount, int totalCount);

        void showAnswerCorrect();

        void showListening();

        void showPatternIsPlaying();

        void showInactive();

        void showPitchResult(int pitchInHz, Note note);

        void showNoPitchDetected();

    }

    interface Presenter extends BasePresenter {

        void startEarTrainingExercise();

        void startEarTrainingExercise(int delay);

        void stopEarTrainingExercise();

    }

}
