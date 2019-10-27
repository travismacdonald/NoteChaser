package com.example.notechaser.activities.eartraining;

import android.util.Log;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.notechaser.activities.data.UserAnswer;
import com.example.notechaser.models.midiplayer.MidiPlayer;
import com.example.notechaser.models.notefilter.NoteFilter;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessor;
import com.example.notechaser.models.ncpitchprocessor.NCPitchProcessorObserver;

public class EarTrainingPresenter implements EarTrainingContract.Presenter, NCPitchProcessorObserver {

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

    private Pattern mCurPattern;

    private Note mLastNoteAdded;

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
    }

    @Override
    public void start() {

    }

    @Override
    public void handlePitchResult(int pitchIx) {
        if (mState == State.LISTENING) {
            final Note curNote;
            if (pitchIx == -1) {
                curNote = mLastNoteAdded = null;
            }
            else {
                curNote = new Note(pitchIx);
            }
            // Todo: filter pitch result
            if (mNoteFilter.isNoteValid(curNote) && curNote != mLastNoteAdded) {
                mUserAnswer.addNote(curNote);

                if (mUserAnswer.size() == mCurPattern.size()) {
                    boolean result = mPatternEngine.isAnswerCorrect(mUserAnswer.getAnswer());
                }
            }
        }
        else {
            Log.d("debug", "you got some shit going down");
        }
    }

}
