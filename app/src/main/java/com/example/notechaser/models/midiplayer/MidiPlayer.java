package com.example.notechaser.models.midiplayer;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;

import java.util.List;

public interface MidiPlayer {

    void start();

    void stop();

    void playPattern(List<Note> toPlay);

    void playPattern(List<Note> toPlay, PatternPlayerObserver observer);

    void playPattern(Pattern toPlay);

    // Notify observer when pattern has finished playing
    void playPattern(Pattern toPlay, PatternPlayerObserver observer);

    void stopCurPlayback();

    void playChord(List<Note> toPlay);

    int getPlugin();

    void setPlugin(int plugin);

    int getVolume();

    void setVolume(int volume);

}
