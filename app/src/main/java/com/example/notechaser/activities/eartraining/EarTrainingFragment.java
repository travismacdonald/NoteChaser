package com.example.notechaser.activities.eartraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keyfinder.MusicTheory;
import com.example.keyfinder.Note;
import com.example.notechaser.R;

public class EarTrainingFragment extends Fragment implements EarTrainingContract.View {

    private EarTrainingContract.Presenter mPresenter;

    private View mRoot;

    private Button mToggleButton;
    private TextView mNoteCountTv;
    private TextView mNoteTv;
    private TextView mPitchTv;


    public EarTrainingFragment() {
        // Required empty public constructor
    }

    static public EarTrainingFragment newInstance() {
        return new EarTrainingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.ear_training_fragment, container, false);
        setupView();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void showNumNotesHeard(int curCount, int answerCount) {
        mNoteCountTv.setText(curCount + "/" + answerCount);
    }

    @Override
    public void showPitchResult(int pitchInHz, Note note) {
        mPitchTv.setText(Integer.toString(pitchInHz));
        mNoteTv.setText(note.getName() + note.getIx() / MusicTheory.TOTAL_NOTES);
    }

    @Override
    public void showNoPitchDetected() {
        mPitchTv.setText("-1");
        mNoteTv.setText("null");
    }

    @Override
    public void showAnswerCorrect() {
        // this is fine for now
        Toast.makeText(mRoot.getContext(), "correct", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showListening() {

    }

    @Override
    public void showPatternIsPlaying() {

    }

    @Override
    public void showInactive() {

    }

    @Override
    public void setPresenter(EarTrainingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void setupView() {
        mToggleButton = mRoot.findViewById(R.id.play_button);
        mToggleButton.setOnClickListener(v -> {
            if (mToggleButton.getText().toString().equals("Start")) {
                mToggleButton.setText("Stop");
                mPresenter.startEarTrainingExercise();
            }
            else {
                mToggleButton.setText("Start");
                mPresenter.stopEarTrainingExercise();
            }
        });

        mNoteCountTv = mRoot.findViewById(R.id.notes_heard_tv);

        mPitchTv = mRoot.findViewById(R.id.pitch_tv);
        mNoteTv = mRoot.findViewById(R.id.note_tv);
    }

}
