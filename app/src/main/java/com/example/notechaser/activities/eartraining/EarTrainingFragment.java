package com.example.notechaser.activities.eartraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notechaser.R;

// TODO
/*
 * Steps for implemented ear training exercise:
 * X 1) change play button to start/stop button
 *   2) add text view which shows the number of notes heard
 *   3) when notes reached: if correct -> play new pattern; else repeat
 *   4) if notes haven't been heard for x amount of time, reset
 *
 *   a) add upper and lower bound edit texts
 */

public class EarTrainingFragment extends Fragment implements EarTrainingContract.View {

    private EarTrainingContract.Presenter mPresenter;

    private View mRoot;

    private Button mPlayButton;

    // todo: probably delete soon
    private Button mStopButton;

    private TextView mNotesHeardTv;

    public EarTrainingFragment() {
        // Required empty public constructor
    }

    static public EarTrainingFragment newInstance() {
        return new EarTrainingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.ear_training_fragment, container, false);

        // Todo: do shit
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
//        String numHeard = mNotesHeardTv.getText().toString();
        mNotesHeardTv.setText(curCount + "/" + answerCount);
    }

//    @Override
//    public void showNoNotesHeard(int totalNotes) {
//        mNotesHeardTv.setText(0 + "/" + totalNotes);
//    }

    @Override
    public void showAnswerResult(boolean answerIsCorrect) {

    }

    @Override
    public void showAnswerCorrect() {
        Log.d("debug", "correct");
        Toast.makeText(mRoot.getContext(), "correct", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAnswerIncorrect() {
        Log.d("debug", "not correct bitch");
        Toast.makeText(mRoot.getContext(), "incorrect", Toast.LENGTH_SHORT).show();
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
        mPlayButton = mRoot.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(v -> {
            if (mPlayButton.getText().toString().equals("Start")) {
                mPlayButton.setText("Stop");
                mPresenter.startEarTrainingExercise();
            }
            else {
                mPlayButton.setText("Start");
                mPresenter.stopEarTrainingExercise();
            }
        });

        mNotesHeardTv = mRoot.findViewById(R.id.notes_heard_tv);
    }
}
