package com.example.notechaser.activities.eartraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.R;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.midiplayer.MidiPlayerImpl;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.patternengine.PatternEngineImpl;

import java.util.List;

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

    public void stopPattern(View view) {
        mMidiPlayer.stopCurPlayback();
    }

}
