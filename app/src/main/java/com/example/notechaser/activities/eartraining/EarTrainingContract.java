package com.example.notechaser.activities.eartraining;

import com.example.keyfinder.Note;
import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;

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
