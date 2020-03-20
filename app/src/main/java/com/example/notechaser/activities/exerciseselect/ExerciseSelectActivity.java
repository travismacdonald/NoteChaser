package com.example.notechaser.activities.exerciseselect;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.notechaser.R;

public class ExerciseSelectActivity extends AppCompatActivity {

    private ExerciseSelectPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_select_activity);

        ExerciseSelectFragment frag =
                (ExerciseSelectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (frag == null) {
            frag = ExerciseSelectFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, frag);
            transaction.commit();
        }

        mPresenter = new ExerciseSelectPresenter();
    }

}
