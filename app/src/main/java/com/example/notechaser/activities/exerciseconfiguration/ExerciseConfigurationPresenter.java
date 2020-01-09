package com.example.notechaser.activities.exerciseconfiguration;

import com.example.keyfinder.Mode;
import com.example.keyfinder.eartraining.AbstractTemplate;
import com.example.keyfinder.eartraining.IntervalTemplate;
import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;

/* Todo:
 *  show all the modes on the configuration screen
 *  - will have to pass modes to view
 *  - will have to receive modes that the user selected
 *
 */

public class ExerciseConfigurationPresenter implements ExerciseConfigurationContract.Presenter {

    // ****************
    // MEMBER VARIABLES
    // ****************

    ExerciseConfigurationContract.View mView;

    PatternEngine mPatternEngine;

    EarTrainingSettings mSettings;

    ModeCollection mModeCollection;

    // ************
    // CONSTRUCTORS
    // ************

    public ExerciseConfigurationPresenter(ExerciseConfigurationContract.View view,
                                          PatternEngine patternEngine,
                                          EarTrainingSettings settings,
                                          ModeCollection modeCollection) {
        mView = view;
        mPatternEngine = patternEngine;
        mSettings = settings;
        mModeCollection = modeCollection;

        mView.setPresenter(this);
    }

    // *****************
    // INTERFACE METHODS
    // *****************

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    // Just a test method
    @Override
    public void loadBanacosSettings() {
        // Simulate Banacos method
        setPatternFixed();
        Integer[] interval = {0};
        IntervalTemplate template = new IntervalTemplate(interval);
        addFixedTemplate(template);
        setMatchOctave(true);
        setMatchOrder(true);
        setPlayCadence(true);
        setPlaybackAscending();
    }

    /**
     * Checks if engine will generate valid patterns.
     */
    @Override
    public boolean isValid() {
        return mPatternEngine.hasSufficientSpace();
    }

    @Override
    public void setPatternDynamic() {
        mPatternEngine.setTypeDynamic();
    }

    @Override
    public void setPatternFixed() {
        mPatternEngine.setTypeFixed();
    }

    @Override
    public void addMode(Mode mode) {
        mPatternEngine.addMode(mode);
    }

    @Override
    public void removeMode(Mode mode) {
        mPatternEngine.removeMode(mode);
    }

    @Override
    public void addDynamicTemplate(AbstractTemplate template) {
        mPatternEngine.addAbstractTemplate(template);
    }

    @Override
    public void removeDynamicTemplate(AbstractTemplate template) {
        mPatternEngine.removeAbstractTemplate(template);
    }

    @Override
    public void addFixedTemplate(IntervalTemplate template) {
        mPatternEngine.addIntervalTemplate(template);
    }

    @Override
    public void removedFixedTemplate(IntervalTemplate template) {
        mPatternEngine.removeIntervalTemplate(template);
    }

    @Override
    public void setPlaybackAscending() {
        mSettings.setPlaybackAscending();
    }

    @Override
    public void setPlaybackDescending() {
        mSettings.setPlaybackDescending();
    }

    @Override
    public void setPlaybackChord() {
        mSettings.setPlaybackChord();
    }

    @Override
    public void setPlaybackRandom() {
        mSettings.setPlaybackRandom();
    }

    @Override
    public void setMatchOctave(boolean shouldMatch) {
        mSettings.setMatchOctave(shouldMatch);
    }

    @Override
    public void setMatchOrder(boolean shouldMatch) {
        mSettings.setMatchOrder(shouldMatch);
    }

    @Override
    public void setUpperBound(int upperBound) {
        mPatternEngine.setUpperBound(upperBound);
    }

    @Override
    public void setLowerBound(int lowerBound) {
        mPatternEngine.setLowerBound(lowerBound);
    }

    @Override
    public void setPlayCadence(boolean shouldPlayCadence) {
        mSettings.setPlayCadence(shouldPlayCadence);
    }

    public PatternEngine getPatternEngine() {
        return mPatternEngine;
    }

    @Override
    public EarTrainingSettings getSettings() {
        return mSettings;
    }

}
