package com.example.notechaser.models.midiplayer;

import com.example.keyfinder.Note;

import java.util.List;

public interface MidiPlayer {

    void start();

    void stop();

    void playPattern(List<Note> toPlay);

    void stopCurPlayback();

    void playChord(List<Note> toPlay);

    int getPlugin();

    void setPlugin(int plugin);

    int getVolume();

    void setVolume(int volume);

}
