package com.example.notechaser.activities.eartraining;

import com.example.keyfinder.Note;
import com.example.notechaser.activities.data.BasePresenter;
import com.example.notechaser.activities.data.BaseView;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNoteHeard(Note note);

        void showListening();

        void showPlayingPattern();

        void showInactive();

    }

    interface Presenter extends BasePresenter {



    }

}
