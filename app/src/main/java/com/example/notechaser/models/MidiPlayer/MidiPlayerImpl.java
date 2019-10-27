package com.example.notechaser.models.MidiPlayer;

import android.util.Log;

import com.example.keyfinder.Note;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MidiPlayerImpl implements MidiPlayer {

    private static final int START = 0X90;

    private static final int STOP = 0X80;

    private static final int PROGRAM_CHANGE = 0XC0;

    private static final int VOLUME_OFF = 0;

    private static final int DEFAULT_VOLUME = 65;

    private static final int NOTE_PLAYBACK_DURATION_MILLIS = 500;

    private Thread mPlaybackThread;

    private MidiDriver mMidiDriver;

    private int mVolume;

    private int mPlugin;

    private Set<Note> mActiveNotes;

    private boolean mInterruptPlayback;

    public MidiPlayerImpl() {
        mMidiDriver = new MidiDriver();
        mVolume = DEFAULT_VOLUME;
        mPlugin = -1;
        mPlaybackThread = null;
        mActiveNotes = null;
        mInterruptPlayback = false;
        mActiveNotes = new HashSet<>();
    }

    @Override
    public void start() {
        mMidiDriver.start();
        sendMidiSetup();
    }

    @Override
    public void stop() {
        mMidiDriver.stop();

    }

    @Override
    public void playPattern(final List<Note> toPlay) {
        Runnable curPattern = () -> {
            for (Note curNote : toPlay) {
                startNotePlayback(curNote);
                try {
                    Thread.sleep(NOTE_PLAYBACK_DURATION_MILLIS);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (!mInterruptPlayback) {
                    stopNotePlayback(curNote);
                }
                else {
                    mInterruptPlayback = false;
                    break;
                }
            }
        };
        mPlaybackThread = new Thread(curPattern);
        mPlaybackThread.start();

    }

    @Override
    public void stopCurPlayback() {
        mInterruptPlayback = true;
        for (Note toStop : mActiveNotes) {
            stopNotePlayback(toStop);
        }
    }

    @Override
    public void playChord(List<Note> toPlay) {

    }

    @Override
    public int getPlugin() {
        return 0;
    }

    @Override
    public void setPlugin(int plugin) {
        mPlugin = plugin;
        sendMidiSetup();
    }

    @Override
    public int getVolume() {
        return 0;
    }

    @Override
    public void setVolume(int volume) {

    }

    private void startNotePlayback(Note toStart) {
        Log.d("test", "called from inside start note playback");
        sendMessage(START, toStart.getIx(), mVolume);
        mActiveNotes.add(toStart);
    }

    private void stopNotePlayback(Note toStop) {
        Log.d("test", "called from inside stop note playback");
        sendMessage(STOP, toStop.getIx(), VOLUME_OFF);
        mActiveNotes.remove(toStop);
    }

    private void sendMessage(int event, int toSend, int volume) {
        byte[] message = new byte[3];
        message[0] = (byte) event;
        message[1] = (byte) toSend;
        message[2] = (byte) volume;
        mMidiDriver.write(message);
    }

    private void sendMidiSetup() {
        byte[] message = new byte[2];
        message[0] = (byte) PROGRAM_CHANGE;
        message[1] = (byte) mPlugin;
        mMidiDriver.write(message);
    }

    private boolean isPlaybackActive() {
        return !mActiveNotes.isEmpty();
    }
}
