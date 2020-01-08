package com.example.notechaser.activities.exerciseconfiguration;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.notechaser.R;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.patternengine.PatternEngineImpl;

public class ExerciseConfigurationActivity extends AppCompatActivity {

    ExerciseConfigurationPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_configuration_activity);

        ExerciseConfigurationFragment frag =
                (ExerciseConfigurationFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (frag == null) {
            frag = ExerciseConfigurationFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, frag);
            transaction.commit();
        }

        PatternEngine patternEngine = new PatternEngineImpl();
        EarTrainingSettings settings = new EarTrainingSettings();
        ModeCollection modeCollection = new ModeCollection();

        mPresenter = new ExerciseConfigurationPresenter(
                frag,
                patternEngine,
                settings,
                modeCollection);
    }

}
