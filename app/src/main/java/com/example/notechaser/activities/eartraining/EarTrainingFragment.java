package com.example.notechaser.activities.eartraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notechaser.R;

public class EarTrainingFragment extends Fragment implements EarTrainingContract.View {

    private EarTrainingContract.Presenter mPresenter;

    private View mRoot;

    private Button mPlayButton;

    private Button mStopButton;

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
    public void showNoteHeard() {

    }

    @Override
    public void showNoNotesHeard() {

    }

    @Override
    public void showAnswerResult(boolean answerIsCorrect) {

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
        mStopButton = mRoot.findViewById(R.id.stop_button);

        mPlayButton.setOnClickListener(v -> {
            mPresenter.playRandomPattern();
        });
    }
}
