package com.example.notechaser.activities.exerciseconfiguration;


import com.example.notechaser.data.EarTrainingSettings;
import com.example.notechaser.data.ModeCollection;
import com.example.notechaser.models.patternengine.PatternEngine;
import com.example.notechaser.patterngenerator.PatternTemplate;

/* Todo:
 *  show all the modes on the configuration screen
 *  - will have to pass modes to view
 *  - will have to receive modes that the user selected
 *
 */

public class ExerciseConfigurationPresenter implements ExerciseConfigurationContract.Presenter {


    ExerciseConfigurationContract.View mView;

    PatternEngine mPatternEngine;

    EarTrainingSettings mSettings;

    ModeCollection mModeCollection;


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
//        Integer[] interval = {0};
//        PatternTemplate template = new PatternTemplate(interval);
//        addFixedTemplate(template);
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

//    @Override
//    public void addMode(Mode mode) {
//        mPatternEngine.addMode(mode);
//    }
//
//    @Override
//    public void removeMode(Mode mode) {
//        mPatternEngine.removeMode(mode);
//    }
//
//    @Override
//    public void addDynamicTemplate(AbstractTemplate template) {
//        mPatternEngine.addAbstractTemplate(template);
//    }
//
//    @Override
//    public void removeDynamicTemplate(AbstractTemplate template) {
//        mPatternEngine.removeAbstractTemplate(template);
//    }

    @Override
    public void addFixedTemplate(PatternTemplate template) {
        mPatternEngine.addIntervalTemplate(template);
    }

    @Override
    public void removedFixedTemplate(PatternTemplate template) {
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
        mSettings.setPlaybackHarmonic();
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
