package com.example.notechaser.activities.eartraining;

import com.example.keyfinder.Note;
import com.example.notechaser.BasePresenter;
import com.example.notechaser.BaseView;

public class EarTrainingContract {

    interface View extends BaseView<Presenter> {

        void showNoteHeard(Note note);

    }

    interface Presenter extends BasePresenter {

    }

}
