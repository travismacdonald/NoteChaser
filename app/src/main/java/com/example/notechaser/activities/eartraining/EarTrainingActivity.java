package com.example.notechaser.activities.eartraining;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.notechaser.R;
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


public class EarTrainingActivity extends AppCompatActivity {

    private EarTrainingPresenter mEarTrainingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ear_training_activity);

        EarTrainingFragment earTrainingFragment =
                (EarTrainingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (earTrainingFragment == null) {
            earTrainingFragment = EarTrainingFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, earTrainingFragment);
            transaction.commit();
        }

        PatternEngine patternEngine = new PatternEngineImpl();
        AnswerChecker checker = new AnswerCheckerImpl();
        MidiPlayer midiPlayer = new MidiPlayerImpl();
        SoundPoolPlayer soundPoolPlayer = new SoundPoolPlayerImpl(this);
        NCPitchProcessor pitchProcessor = new NCPitchProcessorImpl(this);
        NoteFilter noteFilter = new NoteFilterImpl();

        mEarTrainingPresenter = new EarTrainingPresenter(
                earTrainingFragment,
                patternEngine,
                checker,
                midiPlayer,
                soundPoolPlayer,
                pitchProcessor,
                noteFilter);

    }

}
