package com.example.notechaser.activities.eartraining;

import android.util.Log;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.MelodicMinorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.data.UserAnswer;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.midiplayer.PatternPlayerObserver;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorObserver;

public class EarTrainingPresenter
        implements EarTrainingContract.Presenter, NCPitchProcessorObserver, PatternPlayerObserver {

    public static final int PATTERN_START_DELAY = 150;
    // for debugging
    private long mLastTimeAround = -1;

    enum State {
        INACTIVE, PLAYING_PATTERN, LISTENING
    }

    private final static int PLAY_PATTERN_AGAIN = 1500;

    private EarTrainingContract.View mView;

    private PatternEngine mPatternEngine;

    private MidiPlayer mMidiPlayer;

    private NCPitchProcessor mPitchProcessor;

    private NoteFilter mNoteFilter;

    private State mState;

    private UserAnswer mUserAnswer;

//    private Pattern mCurPattern;

    private Note mLastNoteAdded;

    // the initial time stamp that no note was heard
    private long mNullInitHeard;


    private boolean mLastNoteWasNull;

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
        mNullInitHeard = -1;

        mState = State.INACTIVE;
        mUserAnswer = null;
        mLastNoteAdded = null;
        mLastNoteWasNull = false;

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
        mMidiPlayer.playPattern(mPatternEngine.getCurPattern().getNotes(), this);
    }

    @Override
    public void startEarTrainingExercise() {
//        mState = State.PLAYING_PATTERN;
        mPatternEngine.generatePattern();
        resumeEarTrainingExercise();
    }

    // Run single exercise
    private void resumeEarTrainingExercise() {
//        if (mPitchProcessor.isRunning()) {
//            mPitchProcessor.stop();
//        }
        if (!mPitchProcessor.isRunning()) {
            mPitchProcessor.start();
        }
        mState = State.PLAYING_PATTERN;
        mUserAnswer = new UserAnswer(mPatternEngine.getCurPattern().size());
        mView.showNumNotesHeard(0, mPatternEngine.getCurPattern().size());
        mMidiPlayer.playPattern(mPatternEngine.getCurPattern(), this, PATTERN_START_DELAY);
    }

    @Override
    public void stopEarTrainingExercise() {
        mState = State.INACTIVE;
        if (mPitchProcessor.isRunning()) {
            mPitchProcessor.stop();
        }
    }

    @Override
    public void handlePitchResult(int pitchIx) {
        if (mLastTimeAround == -1) {
            mLastTimeAround = System.currentTimeMillis();
        }
        else {
            Log.d("debuggy", "" + (System.currentTimeMillis() - mLastTimeAround));
            mLastTimeAround = System.currentTimeMillis();
        }
        if (mState == State.LISTENING) {
//            Log.d("debuggy", "pitch = " + pitchIx);

            /* Determine Note */
            final Note curNote;
            if (pitchIx == -1) {
                // Null isn't really added,
                // but it means there was space in between the last heard note.
                // This is in place to avoid repeatedly adding the same note
                if (!mLastNoteWasNull) {
                    mLastNoteWasNull = true;
                    mNullInitHeard = System.currentTimeMillis();
                    mLastNoteAdded = null;
                }
                // Repeats pattern for user
                else if (System.currentTimeMillis() - mNullInitHeard > PLAY_PATTERN_AGAIN) {
                    resumeEarTrainingExercise();
                }
            }
            else {
                curNote = new Note(pitchIx);
                mLastNoteWasNull = false;

                /* Add note */
                if (mNoteFilter.isNoteValid(curNote) && !curNote.equals(mLastNoteAdded)) {
                    mUserAnswer.addNote(curNote);
                    mLastNoteAdded = curNote;
                    mView.showNumNotesHeard(mUserAnswer.size(), mPatternEngine.getCurPattern().size());

                    /* Answer reached */
                    if (mUserAnswer.size() == mPatternEngine.getCurPattern().size()) {
                        boolean answerCorrect = mPatternEngine.isAnswerCorrect(mUserAnswer.getAnswer());
                        if (answerCorrect) {
                            mView.showAnswerCorrect();
                            startEarTrainingExercise();
                        }
                    }
                }
            }
        }
        else {
//            Log.d("debuggy", "Playing pattern");
        }
    }

    @Override
    public void handlePatternFinished() {
        mState = State.LISTENING;
        mNullInitHeard = System.currentTimeMillis();
//        mPitchProcessor.start();
    }

    private void setupTest() {

        mPatternEngine.setBounds(40, 76); // low e to high e

        mPatternEngine.addMode(new MajorMode(0));
        mPatternEngine.addMode(new MajorMode(1));
        mPatternEngine.addMode(new MelodicMinorMode(3));


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

        PhraseTemplate otherOtherTemplate = new PhraseTemplate();
        otherOtherTemplate.addDegree(6);
        otherOtherTemplate.addDegree(0);
        otherOtherTemplate.addDegree(2);
        otherOtherTemplate.addDegree(4);
        otherOtherTemplate.addDegree(3);

        mPatternEngine.addPhraseTemplate(template);
        mPatternEngine.addPhraseTemplate(otherTemplate);
        mPatternEngine.addPhraseTemplate(otherOtherTemplate);

    }

}
