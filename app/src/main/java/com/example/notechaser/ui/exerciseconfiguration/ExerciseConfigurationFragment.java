package com.example.notechaser.ui.exerciseconfiguration;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notechaser.R;
import com.example.notechaser.ui.eartraining.EarTrainingActivity;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;

import java.io.Serializable;


public class ExerciseConfigurationFragment extends Fragment implements ExerciseConfigurationContract.View{

    private View mRoot;

    private EditText mLowerEdit;

    private EditText mUpperEdit;

    private ExerciseConfigurationContract.Presenter mPresenter;

    private PatternEngine mPatternEngine;

    public ExerciseConfigurationFragment() {
        // Required empty public constructor
    }

    static public ExerciseConfigurationFragment newInstance() {
        return new ExerciseConfigurationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.exercise_configuration_fragment, container, false);
        setupView();
        return mRoot;
    }

    @Override
    public void showEarTrainingActivity() {
        if (mPresenter.isValid()) {
            Intent intent = new Intent(getContext(), EarTrainingActivity.class);
            intent.putExtra("engine", (Serializable) mPresenter.getPatternEngine());
            intent.putExtra("settings", (Parcelable) mPresenter.getSettings());
            startActivity(intent);
        }
        else {
            Toast.makeText(getContext(), "Not enough space", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showModes(ModeCollection modeCollection) {

    }

    @Override
    public void showSessionValid() {

    }

    @Override
    public void showSessionInvalid() {

    }

    @Override
    public void setPresenter(ExerciseConfigurationContract.Presenter presenter) {
        mPresenter = presenter;
    }


    private void setupView() {
        mLowerEdit = mRoot.findViewById(R.id.lower_edit);
        mUpperEdit = mRoot.findViewById(R.id.upper_edit);
        mRoot.findViewById(R.id.banacos).setOnClickListener(v -> {
            test_loadBanacosMethod();
            mPresenter.setLowerBound(Integer.parseInt(mLowerEdit.getText().toString()));
            mPresenter.setUpperBound(Integer.parseInt(mUpperEdit.getText().toString()));
            showEarTrainingActivity();
        });
    }

    /**
     * This method simulates the user choosing the Banacos ear training method
     */
    private void test_loadBanacosMethod() {
        mPresenter.loadBanacosSettings();
    }

}
