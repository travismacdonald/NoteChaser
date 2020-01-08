package com.example.notechaser.activities.exerciseconfiguration;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.AbstractTemplate;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;

public class ExerciseConfigurationContract {

    interface View extends BaseView<Presenter> {

        void showEarTrainingActivity();

        void showModes(ModeCollection modeCollection);

        void showSessionValid();

        void showSessionInvalid();

    }

    interface Presenter extends BasePresenter {

        void loadBanacosSettings();

        void setPatternDynamic();

        void setPatternFixed();

        void addMode(Mode mode);

        void removeMode(Mode mode);

        void addDynamicTemplate(AbstractTemplate template);

        void removeDynamicTemplate(AbstractTemplate template);

        void addFixedTemplate(IntervalTemplate template);

        void removedFixedTemplate(IntervalTemplate template);

        void setPlaybackAscending();

        void setPlaybackDescending();

        void setPlaybackChord();

        void setPlaybackRandom();

        void setMatchOctave(boolean shouldMatch);

        void setMatchOrder(boolean shouldMatch);

        void setUpperBound(int upperBound);

        void setLowerBound(int lowerBound);

        void setPlayCadence(boolean shouldPlayCadence);

        // Bad methods

        EarTrainingSettings getSettings();

        PatternEngine getPatternEngine();

    }

}
