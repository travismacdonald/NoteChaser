package com.example.notechaser.activities.eartraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.MelodicMinorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.R;
import com.example.notechaser.models.MidiPlayer.MidiPlayer;
import com.example.notechaser.models.MidiPlayer.MidiPlayerImpl;
import com.example.notechaser.models.PatternEngine.PatternEngine;
import com.example.notechaser.models.PatternEngine.PatternEngineImpl;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.PitchConverter;

public class EarTrainingActivity extends AppCompatActivity {

    MidiPlayer mMidiPlayer;

    PatternEngine mPatternEngine;

    PhraseTemplate template;

    PhraseTemplate otherTemplate;

    List<Note> toPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_training);

        mMidiPlayer = new MidiPlayerImpl();
        mPatternEngine = new PatternEngineImpl();

        mPatternEngine.setBounds(36, 60);

        mPatternEngine.addMode(new MajorMode(0));
        mPatternEngine.addMode(new MajorMode(1));

        template = new PhraseTemplate();
        template.addDegree(0);
        template.addDegree(2);
        template.addDegree(3);
        template.addDegree(4);

        otherTemplate = new PhraseTemplate();
        otherTemplate.addDegree(0);
        otherTemplate.addDegree(1);
        otherTemplate.addDegree(2);
        otherTemplate.addDegree(4);

        mPatternEngine.addPhraseTemplate(template);
        mPatternEngine.addPhraseTemplate(otherTemplate);

        mMidiPlayer.setPlugin(0);
        mMidiPlayer.start();

    }

    public void playPattern(View view) {
        mPatternEngine.generatePattern();
        mMidiPlayer.playPattern(mPatternEngine.getCurPattern().getNotes());
    }

}
