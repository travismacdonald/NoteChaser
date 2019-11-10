package com.example.notechaser.models.midiplayer;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MidiPlayerImpl implements MidiPlayer {

    /* Some Midi Constants */

    private static final int[][] sCadence = {
            { 36 },
            { 41 },
            { 43 },
            { 36 }
    };

    private static final int START = 0X90;

    private static final int STOP = 0X80;

    private static final int PROGRAM_CHANGE = 0XC0;

    private static final int VOLUME_OFF = 0;

    private static final int DEFAULT_VOLUME = 65;

    private static final int NOTE_DURATION = 500;

    private static final int REVERB_DURATION = 850;

    private Thread mCadenceThread;

    private Thread mPatternThread;

    private MidiDriver mMidiDriver;

    private int mVolume;

    private int mPlugin;

    private Set<Note> mActiveNotes;

    private Pattern mCurrentPattern;

    public MidiPlayerImpl() {
        mMidiDriver = new MidiDriver();
        mVolume = DEFAULT_VOLUME;
        mPlugin = -1;
        mCadenceThread = null;
        mPatternThread = null;
        mActiveNotes = null;
        mActiveNotes = new HashSet<>();
        mCurrentPattern = null;
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
    public void playCadence() {
        // blah
    }

    @Override
    public Thread playPattern(Pattern toPlay) {
        return playPattern(toPlay, null, 0);
    }

    @Override
    public Thread playPattern(Pattern toPlay, PatternPlayerObserver observer) {
        return playPattern(toPlay, observer, 0);
    }

    @Override
    public Thread playPattern(Pattern toPlay, PatternPlayerObserver observer, int startDelay) {
        mCurrentPattern = toPlay;
        Runnable curPattern = () -> {
            try {
                Thread.sleep(startDelay);
            } catch (Exception e) {
                System.out.println(e);
            }
            for (Note curNote : toPlay.getNotes()) {
                startNotePlayback(curNote);
                try {
                    Thread.sleep(NOTE_DURATION);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (toPlay.canInterrupt()) {
                    toPlay.setCanInterrupt(false);
                    return;
                }
                else {
                    stopNotePlayback(curNote);
                }
            }
            if (observer != null) {
                // Added sleep here so the pitch processor doesn't pick up the reverb from the last note
                try {
                    Thread.sleep(REVERB_DURATION);
                } catch (Exception e) {
                    System.out.println(e);
                }
                observer.handlePatternFinished();
            }
        };
        mPatternThread = new Thread(curPattern, "Playback thread");
        mPatternThread.start();
        return mPatternThread;
    }

    @Override
    public void stopCurPlayback() {
        mCurrentPattern.setCanInterrupt(true);
        mCurrentPattern = null;

        // Todo: concurrent modification error happening here
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
        sendMessage(START, toStart.getIx(), mVolume);
        mActiveNotes.add(toStart);
    }

    private void stopNotePlayback(Note toStop) {
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

    @Override
    public boolean playbackIsActive() {
        return !mActiveNotes.isEmpty();
    }
}
