package com.example.notechaser.models.midiplayer;



import com.example.notechaser.patterngenerator.Note;
import com.example.notechaser.patterngenerator.Pattern;

import java.util.List;

public interface MidiPlayer {

    void start();

    void stop();

//    void playPattern(List<Note> toPlay);

//    void playPattern(List<Note> toPlay, PatternPlayerObserver observer);

    void playCadence();

    Thread playPattern(Pattern toPlay);

    // Notify observer when pattern has finished playing
    Thread playPattern(Pattern toPlay, PatternPlayerObserver observer);

    Thread playPattern(Pattern toPlay, PatternPlayerObserver observer, int startDelay);

    Thread playPattern(Pattern toPlay,
                       PatternPlayerObserver observer,
                       int startDelay,
                       boolean shouldPlayCadence);

    boolean playbackIsActive();

    void stopCurPlayback();

    void playChord(List<Note> toPlay);

    int getPlugin();

    void setPlugin(int plugin);

    int getVolume();

    void setVolume(int volume);

}
