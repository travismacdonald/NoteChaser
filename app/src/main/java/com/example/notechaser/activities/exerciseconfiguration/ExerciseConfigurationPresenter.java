package com.example.notechaser.activities.exerciseconfiguration;

import com.example.notechaser.data.ModeCollection;

/* Todo:
 *  show all the modes on the configuration screen
 *  - will have to pass modes to view
 *  - will have to receive modes that the user selected
 *  -
 *
 */

public class ExerciseConfigurationPresenter {

    ExerciseConfigurationContract.View mView;

    ModeCollection mModeCollection;

    public ExerciseConfigurationPresenter(ExerciseConfigurationContract.View view
                                          ) {
        mView = view;


    }

}
