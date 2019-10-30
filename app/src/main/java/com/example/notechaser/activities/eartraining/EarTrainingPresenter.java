package com.example.notechaser.activities.eartraining;

import android.util.Log;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.data.UserAnswer;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorObserver;
import com.example.notechaser.models.patternengine.PatternEngineImpl;

public class EarTrainingPresenter implements EarTrainingContract.Presenter, NCPitchProcessorObserver {



    enum State {
        INACTIVE, PLAYING_PATTERN, LISTENING;
    }
    private EarTrainingContract.View mView;

    private PatternEngine mPatternEngine;

    private MidiPlayer mMidiPlayer;

    private NCPitchProcessor mPitchProcessor;

    private NoteFilter mNoteFilter;

    private State mState;

    private UserAnswer mUserAnswer;

    private Pattern mCurPattern;

    private Note mLastNoteAdded;

    // Todo: These are test variables; delete when no longer needed

    public EarTrainingPresenter(EarTrainingContract.View view,
                                PatternEngine patternEngine,
                                MidiPlayer midiPlayer,
                                NCPitchProcessor pitchProcessor,
                                NoteFilter noteFilter) {
        mView = view;
        mPatternEngine = patternEngine;
        mMidiPlayer = midiPlayer;
        mPitchProcessor = pitchProcessor;
        mNoteFilter = noteFilter;

        mState = State.INACTIVE;
        mUserAnswer = new UserAnswer();
        mCurPattern = null;
        mLastNoteAdded = null;

        mView.setPresenter(this);
        mPitchProcessor.addPitchObserver(this);

        setupTest();
    }


    @Override
    public void start() {
        mMidiPlayer.start();
    }

    @Override
    public void stop() {
        mMidiPlayer.stop();
    }

    @Override
    public void setLowerBound(int lowerBound) {
        mPatternEngine.setLowerBound(lowerBound);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mPatternEngine.setUpperBound(upperBound);
    }

    // Todo: only for testing, should be made private later or something
    @Override
    public void playRandomPattern() {
        mPatternEngine.generatePattern();
        mMidiPlayer.playPattern(mPatternEngine.getCurPattern().getNotes());
    }

    @Override
    public void handlePitchResult(int pitchIx) {
        if (mState == State.LISTENING) {
            /* Determine Note */
            final Note curNote;
            if (pitchIx == -1) {
                // Null isn't really added,
                // but it means there was space in between the last heard note.
                // This is in place to avoid repeatedly adding the same note
                curNote = mLastNoteAdded = null;
            }
            else {
                curNote = new Note(pitchIx);
            }

            /* Add note */
            if (mNoteFilter.isNoteValid(curNote) && curNote != mLastNoteAdded) {
                mUserAnswer.addNote(curNote);
                mLastNoteAdded = curNote;
                mView.showNoteHeard();

                /* Answer reached */
                if (mUserAnswer.size() == mCurPattern.size()) {
                    boolean answerCorrect = mPatternEngine.isAnswerCorrect(mUserAnswer.getAnswer());
                    mView.showAnswerResult(answerCorrect);

                    if (answerCorrect) {
                        // generate new pattern
                    }
                    else {
                        // reset user answer, play pattern again
                    }
                }
            }
        }
        else {
            Log.d("debug", "you got some shit going down");
        }
    }

    private void setupTest() {

        mPatternEngine.setBounds(36, 60);

        mPatternEngine.addMode(new MajorMode(0));
        mPatternEngine.addMode(new MajorMode(1));


        PhraseTemplate template = new PhraseTemplate();
        template.addDegree(0);
        template.addDegree(2);
        template.addDegree(3);
        template.addDegree(4);

        PhraseTemplate otherTemplate = new PhraseTemplate();
        otherTemplate.addDegree(0);
        otherTemplate.addDegree(1);
        otherTemplate.addDegree(2);
        otherTemplate.addDegree(4);

        mPatternEngine.addPhraseTemplate(template);
        mPatternEngine.addPhraseTemplate(otherTemplate);

    }

}
