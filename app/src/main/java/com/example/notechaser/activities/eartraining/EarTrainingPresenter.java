package com.example.notechaser.activities.eartraining;

import android.util.Log;

import com.example.keyfinder.MajorMode;
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



    enum State {
        INACTIVE, PLAYING_PATTERN, LISTENING
    }
    private EarTrainingContract.View mView;

    private PatternEngine mPatternEngine;

    private MidiPlayer mMidiPlayer;

    private NCPitchProcessor mPitchProcessor;

    private NoteFilter mNoteFilter;

    private State mState;

    private UserAnswer mUserAnswer;

//    private Pattern mCurPattern;

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
        mUserAnswer = null;
//        mCurPattern = null;
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
        mMidiPlayer.playPattern(mPatternEngine.getCurPattern().getNotes(), this);
    }

    @Override
    public void startEarTrainingExercise() {

        mPatternEngine.generatePattern();
        // something about start ear training exercise
        _startEarTrainingExercise();
    }

    // Run single exercise
    private void _startEarTrainingExercise() {
        mUserAnswer = new UserAnswer(mPatternEngine.getCurPattern().size());
        mView.showNumNotesHeard(0, mPatternEngine.getCurPattern().size());
        mState = State.PLAYING_PATTERN;
//        Runnable exerciseRunnable = () -> {
            mMidiPlayer.playPattern(mPatternEngine.getCurPattern(), this);
//        };
//        Thread mExerciseThread = new Thread(exerciseRunnable);
//        mExerciseThread.start();
    }

    @Override
    public void stopEarTrainingExercise() {
        mState = State.INACTIVE;
    }

    @Override
    public void handlePitchResult(int pitchIx) {
        if (mState == State.LISTENING) {
//            Log.d("debug", "handle pitch result");
            /* Determine Note */
            final Note curNote;
            if (pitchIx == -1) {
                // Null isn't really added,
                // but it means there was space in between the last heard note.
                // This is in place to avoid repeatedly adding the same note
                mLastNoteAdded = null;
            }
            else {
                curNote = new Note(pitchIx);

                /* Add note */
                if (mNoteFilter.isNoteValid(curNote) && !curNote.equals(mLastNoteAdded)) {
                    mUserAnswer.addNote(curNote);
                    mLastNoteAdded = curNote;
                    mView.showNumNotesHeard(mUserAnswer.size(), mPatternEngine.getCurPattern().size());

                    /* Answer reached */
                    if (mUserAnswer.size() == mPatternEngine.getCurPattern().size()) {
                        Log.d("debug", "user: " + mUserAnswer.toString());
                        Log.d("debug", "actu: " + mPatternEngine.getCurPattern().toString());
                        boolean answerCorrect = mPatternEngine.isAnswerCorrect(mUserAnswer.getAnswer());
//                        mView.showAnswerResult(answerCorrect);
                        if (answerCorrect) {
                            mPitchProcessor.stop();
                            mView.showAnswerCorrect();
                            startEarTrainingExercise();
                        }
                        else {
//                            mView.showAnswerIncorrect();
//                            _startEarTrainingExercise();
                        }
                    }
                }
            }

        }
        else {
            Log.d("debug", "you got some shit going down");
        }
    }

    @Override
    public void handlePatternFinished() {
        Log.d("debug", "handle pitch result");
        mState = State.LISTENING;
        mPitchProcessor.start();
    }

    private void setupTest() {

        mPatternEngine.setBounds(40, 76); // low e to high e

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
