package com.example.notechaser.ui.exerciseconfiguration;


import com.example.notechaser.data.BasePresenter;
import com.example.notechaser.data.BaseView;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.patterngenerator.PatternTemplate;

public class ExerciseConfigurationContract {

    interface View extends BaseView<Presenter> {

        void showEarTrainingActivity();

        void showModes(ModeCollection modeCollection);

        void showSessionValid();

        void showSessionInvalid();

    }

    interface Presenter extends BasePresenter {

        void loadBanacosSettings();

        boolean isValid();

        void setPatternDynamic();

        void setPatternFixed();

//        void addMode(Mode mode);
//
//        void removeMode(Mode mode);

//        void addDynamicTemplate(AbstractTemplate template);
//
//        void removeDynamicTemplate(AbstractTemplate template);

        void addFixedTemplate(PatternTemplate template);

        void removedFixedTemplate(PatternTemplate template);

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
