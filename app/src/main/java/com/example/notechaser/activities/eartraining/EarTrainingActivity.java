package com.example.notechaser.activities.eartraining;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.R;
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

import java.util.List;

public class EarTrainingActivity extends AppCompatActivity {

    private EarTrainingPresenter mEarTrainingPresenter; // todo : this can stay right here; good kitty

    MidiPlayer mMidiPlayer; //todo gotto go

    PatternEngine mPatternEngine; // todo gtfo

    PhraseTemplate template; // todo fuck out of here

    PhraseTemplate otherTemplate; // todo you too bitch

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
        MidiPlayer midiPlayer = new MidiPlayerImpl();
        SoundPoolPlayer soundPoolPlayer = new SoundPoolPlayerImpl(this);
        NCPitchProcessor pitchProcessor = new NCPitchProcessorImpl(this);
        NoteFilter noteFilter = new NoteFilterImpl();

        mEarTrainingPresenter = new EarTrainingPresenter(
                earTrainingFragment,
                patternEngine,
                midiPlayer,
                soundPoolPlayer,
                pitchProcessor,
                noteFilter);

    }

//    public void playPattern(View view) {
//        mPatternEngine.generatePattern();
//        mMidiPlayer.playPattern(mPatternEngine.getCurPattern());
//    }

//    public void stopPattern(View view) {
//        mMidiPlayer.stopCurPlayback();
//    }

}
