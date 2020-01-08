package com.example.notechaser.models.midiplayer;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MidiPlayer model responsible for playing patterns, chords, and cadences.
 */
public class MidiPlayerImpl implements MidiPlayer {

    // **************
    // MIDI CONSTANTS
    // **************

    private static final int START = 0X90;

    private static final int STOP = 0X80;

    private static final int PROGRAM_CHANGE = 0XC0;

    // ******************
    // PLAYBACK CONSTANTS
    // ******************

    private static final int VOLUME_OFF = 0;

    private static final int DEFAULT_VOLUME = 65;

    private static final int NOTE_DURATION = 500;

    private static final int CADENCE_DURATION = 800;

    private static final int REVERB_DURATION = 850;

    // *****************
    // PRIVATE VARIABLES
    //******************

    /**
     * Indices of root notes for I-IV-V-I cadence in C.
     */
    private Note[][] mCadence;

    private Thread mCadenceThread;

    private Thread mPatternThread;

    private MidiDriver mMidiDriver;

    private int mVolume;

    /**
     * Index of midi plugin.
     */
    private int mPlugin;

    /**
     * Current notes being played by midi.
     */
    private Set<Note> mActiveNotes;

    /**
     * Current pattern to be played by midi.
     */
    private Pattern mCurrentPattern;

    // ************
    // CONSTRUCTORS
    // ************

    public MidiPlayerImpl() {
        mMidiDriver = new MidiDriver();
        mVolume = DEFAULT_VOLUME;
        mPlugin = -1;
        mCadenceThread = null;
        mPatternThread = null;
        mActiveNotes = null;
        mActiveNotes = new HashSet<>();
        mCurrentPattern = null;

        loadCadence();
    }

    // *****************
    // INTERFACE METHODS
    // *****************

    /**
     * Function used to setup midi driver.
     * This method is called before the midi driver can be used for playback.
     */
    @Override
    public void start() {
        mMidiDriver.start();
        sendMidiSetup();
    }

    /**
     * Function for deconstructing midi driver.
     * This method is called when the activity using it is being destroyed.
     */
    @Override
    public void stop() {
        mMidiDriver.stop();
    }

    /**
     * Todo: Get this working?
     */
    @Override
    public void playCadence() {
        // blah
    }

    /**
     * Queue pattern for playback. No start delay and no observer.
     */
    @Override
    public Thread playPattern(Pattern toPlay) {
        return playPattern(toPlay, null, 0);
    }

    /**
     * Queue pattern for playback. No start delay.
     */
    @Override
    public Thread playPattern(Pattern toPlay, PatternPlayerObserver observer) {
        return playPattern(toPlay, observer, 0);
    }

    /**
     * Queues pattern for playback. Notifies observer if not null.
     *
     * @param toPlay the pattern to be played
     * @param observer object to be notified when pattern is finished playing
     * @param startDelay milliseconds to wait before starting playback
     * @return pattern playback thread.
     */
    @Override
    public Thread playPattern(Pattern toPlay, PatternPlayerObserver observer, int startDelay) {
        return playPattern(toPlay, observer, startDelay, false);
    }

    @Override
    public Thread playPattern(
            Pattern toPlay,
            PatternPlayerObserver observer,
            int startDelay,
            boolean shouldPlayCadence) {
        mCurrentPattern = toPlay;
        Runnable curPattern = () -> {
            try {
                Thread.sleep(startDelay);
            }
            catch (Exception e) {
                System.out.println(e);
            }
            // tODO: make playback sound a bit smoother
            if (shouldPlayCadence) {
                for (Note[] curChord : mCadence) {
                    for (Note curNote : curChord) {
                        startNotePlayback(curNote);
                    }
                    try {
                        Thread.sleep(CADENCE_DURATION);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    for (Note curNote : curChord) {
                        stopNotePlayback(curNote);
                    }
                }
                try {
                    Thread.sleep(NOTE_DURATION);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            for (Note curNote : toPlay.getNotes()) {
                startNotePlayback(curNote);
                try {
                    Thread.sleep(NOTE_DURATION);
                } catch (Exception e) {
                    System.out.println(e);
                }
                // Playback has already been stopped.
                if (toPlay.canInterrupt()) {
                    toPlay.setCanInterrupt(false);
                    return;
                }
                // Pattern is not interrupted.
                else {
                    stopNotePlayback(curNote);
                }
            }
            // Notify observer
            if (observer != null) {
                // Added sleep here so the pitch processor doesn't pick up the reverb from the last note
                try {
                    Thread.sleep(REVERB_DURATION);
                } catch (Exception e) {
                    System.out.println(e);
                }
                observer.notifyObserver();
            }
        };
        mPatternThread = new Thread(curPattern, "PlaybackType thread");
        mPatternThread.start();
        return mPatternThread;
    }

    /**
     * Dequeue's current playback pattern and terminates current playback.
     */
    @Override
    public void stopCurPlayback() {
        mCurrentPattern.setCanInterrupt(true);
        mCurrentPattern = null;
        // Todo: concurrent modification error happening here
        for (Note toStop : mActiveNotes) {
            stopNotePlayback(toStop);
        }
    }

    /**
     * Play notes at same time.
     * @param toPlay chord to play.
     */
    @Override
    public void playChord(List<Note> toPlay) {
        // Todo: write method
    }

    /**
     * Return plugin index.
     */
    @Override
    public int getPlugin() {
        // Todo: write method
        return -1;
    }

    /**
     * Set plugin index.
     */
    @Override
    public void setPlugin(int plugin) {
        mPlugin = plugin;
        sendMidiSetup();
    }

    /**
     * Get midi volume.
     */
    @Override
    public int getVolume() {
        // Todo: write method
        return -1;
    }

    /**
     * Set midi volume.
     */
    @Override
    public void setVolume(int volume) {
        // Todo: write method
    }

    // Todo: check if this is currently being used / working.
    /**
     * Check if pattern is currently being played.
     * @return true if notes are being played.
     */
    @Override
    public boolean playbackIsActive() {
        return !mActiveNotes.isEmpty();
    }

    // ***************
    // PRIVATE METHODS
    // ***************

    /**
     * Starts playback for note given.
     * @param toStart index of note.
     */
    private void startNotePlayback(Note toStart) {
        sendMessage(START, toStart.getIx(), mVolume);
        mActiveNotes.add(toStart);
    }

    /**
     * Stops playback for note given.
     * @param toStop index of note.
     */
    private void stopNotePlayback(Note toStop) {
        sendMessage(STOP, toStop.getIx(), VOLUME_OFF);
        mActiveNotes.remove(toStop);
    }

    /**
     * Send midi information.
     * @param event event for note (eg: on, off)
     * @param toSend index of note to send.
     * @param volume volume message.
     */
    private void sendMessage(int event, int toSend, int volume) {
        byte[] message = new byte[3];
        message[0] = (byte) event;
        message[1] = (byte) toSend;
        message[2] = (byte) volume;
        mMidiDriver.write(message);
    }

    /**
     * Initialize midi.
     */
    private void sendMidiSetup() {
        byte[] message = new byte[2];
        message[0] = (byte) PROGRAM_CHANGE;
        message[1] = (byte) mPlugin;
        mMidiDriver.write(message);
    }

    private void loadCadence() {
        mCadence = new Note[4][];

        // C Maj
        Note cbass = new Note(36);
        Note c3 = new Note(48);
        Note g3 = new Note(55);
        Note e4 = new Note(64);
        mCadence[0] = new Note[]{ cbass, c3, g3, e4 };

        // F Maj
        Note fBass = new Note(41);
        Note f3 = new Note(53);
        Note c4 = new Note(60);
        Note a4 = new Note(57);
        mCadence[1] = new Note[]{ fBass, f3, c4, a4 };

        // G Maj
        Note gBass = new Note(50);
        Note d4 = new Note(62);
        Note b5 = new Note(59);
        mCadence[2] = new Note[]{ gBass, g3, d4, b5 };

        // C Maj
        mCadence[3] = mCadence[0];

    }

}
