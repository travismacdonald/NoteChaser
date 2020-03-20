package com.example.notechaser.activities.exerciseselect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.notechaser.R;
import com.example.notechaser.activities.exerciseconfiguration.ExerciseConfigurationPresenter;

public class ExerciseSelectActivity extends AppCompatActivity {

    private ExerciseConfigurationPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_select_activity);

        ExerciseSelectFragment frag =
                (ExerciseSelectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
    }

}
