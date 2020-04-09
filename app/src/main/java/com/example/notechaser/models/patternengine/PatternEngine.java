package com.example.notechaser.models.patternengine;


import com.example.notechaser.patterngenerator.Pattern;
import com.example.notechaser.patterngenerator.PatternTemplate;

public interface PatternEngine {

    // See if pattern has enough space to generate at least one pattern
    boolean hasSufficientSpace();

    Pattern getCurPattern();

    Pattern generatePattern();

    void addPatternTemplate(PatternTemplate toAdd);

    void removePatternTemplate(PatternTemplate toRemove);

//    void addAbstractTemplate( toAdd);
//
//    void removeAbstractTemplate(AbstractTemplate toRemove);

    void setLowerBound(int lowerBound);

    void setUpperBound(int upperBound);

    void setBounds(int lowerBound, int upperBound);

//    void addMode(Mode mode);
//
//    void removeMode(Mode mode);

    void setTypeDynamic();

    void setTypeFixed();

}
