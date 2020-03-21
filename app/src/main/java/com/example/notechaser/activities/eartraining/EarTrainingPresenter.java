package com.example.notechaser.activities.eartraining;

import android.util.Log;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.IntervalBank;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.UserAnswer;
import com.example.notechaser.models.answerchecker.AnswerChecker;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.midiplayer.PatternPlayerObserver;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorObserver;
import com.example.notechaser.models.soundpoolplayer.SoundPoolPlayer;


public class EarTrainingPresenter
        implements EarTrainingContract.Presenter, NCPitchProcessorObserver, PatternPlayerObserver {

    // for debugging
    private long mLastTimeAround = -1;

    public static final int PATTERN_START_DELAY = 1000;

    private final static int PLAY_PATTERN_AGAIN = 1750;

    enum State {
        INACTIVE, PLAYING_PATTERN, LISTENING
    }

    private EarTrainingContract.View mView;

    private PatternEngine mPatternEngine;

    private AnswerChecker mChecker;

    private MidiPlayer mMidiPlayer;

    private SoundPoolPlayer mSoundPoolPlayer;

    private NCPitchProcessor mPitchProcessor;

    private NoteFilter mNoteFilter;

    private EarTrainingSettings mSettings;

    private State mState;

    private UserAnswer mUserAnswer;

    private Note mLastNoteAdded;

    // the initial time stamp that no note was heard
    private long mNullInitHeard;

    private boolean mLastNoteIsNull;

    // Todo: These are test variables; delete when no longer needed

    public EarTrainingPresenter(EarTrainingContract.View view,
                                PatternEngine patternEngine,
                                AnswerChecker checker,
                                MidiPlayer midiPlayer,
                                SoundPoolPlayer soundPoolPlayer,
                                NCPitchProcessor pitchProcessor,
                                NoteFilter noteFilter,
                                EarTrainingSettings settings) {
        mView = view;
        mPatternEngine = patternEngine;
        mChecker = checker;
        mMidiPlayer = midiPlayer;
        mSoundPoolPlayer = soundPoolPlayer;
        mPitchProcessor = pitchProcessor;
        mNoteFilter = noteFilter;
        mSettings = settings;

        mNullInitHeard = -1;
        mState = State.INACTIVE;
        mUserAnswer = new UserAnswer();
        mLastNoteAdded = null;
        mLastNoteIsNull = false;

        mView.setPresenter(this);
        mPitchProcessor.addPitchObserver(this);
    }


    @Override
    public void start() {
        mMidiPlayer.start();
    }

    @Override
    public void stop() {
        stopEarTrainingExercise();
        mMidiPlayer.stop();
    }

    @Override
    public void startEarTrainingExercise(int delay) {
        mPatternEngine.generatePattern();
        resumeEarTrainingExercise(delay);
    }

    @Override
    public void startEarTrainingExercise() {
        startEarTrainingExercise(0);
    }

    @Override
    public void stopEarTrainingExercise() {
        mState = State.INACTIVE;
        if (mMidiPlayer.playbackIsActive()) {
            mMidiPlayer.stopCurPlayback();
        }
        if (mPitchProcessor.isRunning()) {
            mPitchProcessor.stop();
        }
    }

    // 217-498

    @Override
    public void notifyObserver(double pitchInHz, int pitchIx) {
        Log.d("accuracy", "Pitch: " + pitchIx + '\n' + System.currentTimeMillis());
        if (mLastTimeAround == -1) {
            mLastTimeAround = System.currentTimeMillis();
        }
        else {
            mLastTimeAround = System.currentTimeMillis();
        }
        if (mState == State.LISTENING) {
            /* Determine Note */
            final Note curNote;
            if (pitchIx == -1) {
                mView.showNoPitchDetected();
                // Null isn't really added,
                // but it means there was space in between the last heard note.
                // This is in place to avoid repeatedly adding the same note
                if (!mLastNoteIsNull) {
                    mLastNoteIsNull = true;
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
                mView.showPitchResult((int) pitchInHz, curNote);
                mLastNoteIsNull = false;

                /* Add note */
                if (mNoteFilter.isNoteValid(curNote) && !curNote.equals(mLastNoteAdded)) {
                    mUserAnswer.addNote(curNote);
                    mLastNoteAdded = curNote;
                    mView.showNumNotesHeard(mUserAnswer.size(), mPatternEngine.getCurPattern().size());

                    /* Answer reached */
                    if (mUserAnswer.size() == mPatternEngine.getCurPattern().size()) {
                        final boolean answerCorrect = checkAnswer(mUserAnswer);
                        if (answerCorrect) {
                            mView.showAnswerCorrect();
                            mSoundPoolPlayer.playAnswerCorrect();
                            startEarTrainingExercise(PATTERN_START_DELAY);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void notifyObserver() {
        mState = State.LISTENING;
        mNullInitHeard = System.currentTimeMillis();
    }

    private void resumeEarTrainingExercise() {
        resumeEarTrainingExercise(0);
    }

    // Run single exercise
    private void resumeEarTrainingExercise(int delay) {
        if (!mPitchProcessor.isRunning()) {
            mPitchProcessor.start();
        }
        mState = State.PLAYING_PATTERN;
        mUserAnswer.clear();
        mUserAnswer.setExpectedSize(mPatternEngine.getCurPattern().size());
        mView.showNumNotesHeard(0, mPatternEngine.getCurPattern().size());
        final Thread thread =
                mMidiPlayer.playPattern(mPatternEngine.getCurPattern(), this, delay, mSettings.shouldPlayCadence());
    }

    /**
     * Determines which method to check answer with and returns the result.
     */
    private boolean checkAnswer(UserAnswer answer) {
        if (mSettings.shouldMatchOctave() && mSettings.shouldMatchOrder()) {
            return mChecker.samePatternSameOctave(mPatternEngine.getCurPattern(), mUserAnswer);
        }
        else if (mSettings.shouldMatchOctave() && !mSettings.shouldMatchOrder()) {
            return mChecker.sameNotesSameOctave(mPatternEngine.getCurPattern(), mUserAnswer);
        }
        else if (!mSettings.shouldMatchOctave() && mSettings.shouldMatchOrder()) {
            return mChecker.samePatternAnyOctave(mPatternEngine.getCurPattern(), mUserAnswer);
        }
        else if (!mSettings.shouldMatchOctave() && !mSettings.shouldMatchOrder()) {
            return mChecker.sameNotesAnyOctave(mPatternEngine.getCurPattern(), mUserAnswer);
        }
        return false;
    }

}
