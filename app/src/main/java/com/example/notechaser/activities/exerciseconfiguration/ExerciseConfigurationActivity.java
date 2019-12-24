package com.example.notechaser.activities.exerciseconfiguration;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.notechaser.R;
import com.example.notechaser.activities.eartraining.EarTrainingFragment;
import com.example.notechaser.activities.eartraining.EarTrainingPresenter;
import com.example.notechaser.models.answerchecker.AnswerChecker;
import com.example.notechaser.models.answerchecker.AnswerCheckerImpl;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.midiplayer.MidiPlayerImpl;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorImpl;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.notefilter.NoteFilterImpl;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.patternengine.PatternEngineImpl;
import com.example.notechaser.models.soundpoolplayer.SoundPoolPlayer;
import com.example.notechaser.models.soundpoolplayer.SoundPoolPlayerImpl;

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

        mPresenter = new ExerciseConfigurationPresenter(frag);
    }

}
