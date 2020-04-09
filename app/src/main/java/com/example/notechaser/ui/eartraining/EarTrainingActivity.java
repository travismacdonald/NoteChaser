package com.example.notechaser.ui.eartraining;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.notechaser.R;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.models.answerchecker.AnswerChecker;
import com.example.notechaser.models.answerchecker.AnswerCheckerImpl;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.midiplayer.MidiPlayerImpl;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorImpl;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.notefilter.NoteFilterImpl;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.soundpoolplayer.SoundPoolPlayer;
import com.example.notechaser.models.soundpoolplayer.SoundPoolPlayerImpl;


public class EarTrainingActivity extends AppCompatActivity {

    private EarTrainingPresenter mEarTrainingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ear_training_activity);

        EarTrainingFragment frag =
                (EarTrainingFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (frag == null) {
            frag = EarTrainingFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, frag);
            transaction.commit();
        }

        PatternEngine patternEngine = (PatternEngine) getIntent().getSerializableExtra("engine");
        AnswerChecker answerChecker = new AnswerCheckerImpl();
        MidiPlayer midiPlayer = new MidiPlayerImpl();
        SoundPoolPlayer soundPoolPlayer = new SoundPoolPlayerImpl(this);
        NCPitchProcessor pitchProcessor = new NCPitchProcessorImpl(this);
        NoteFilter noteFilter = new NoteFilterImpl();

        EarTrainingSettings settings = getIntent().getParcelableExtra("settings");

        mEarTrainingPresenter = new EarTrainingPresenter(
                frag,
                patternEngine,
                answerChecker,
                midiPlayer,
                soundPoolPlayer,
                pitchProcessor,
                noteFilter,
                settings);
    }

}
