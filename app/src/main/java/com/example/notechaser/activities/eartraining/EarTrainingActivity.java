package com.example.notechaser.activities.eartraining;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.notechaser.R;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.Random;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.PitchConverter;

public class EarTrainingActivity extends AppCompatActivity
    implements MidiDriver.OnMidiStartListener {

    TextView pitchText;

    public Random rng = new Random();
    public int curSoundNoteIx;
    public int prevSoundNoteIx;
    public int curHeardNoteIx;
    public long curHeardNoteIxRegistered;

    Button randomNoteButton;

    public int MIDI_STOP  = 0X80;
    public int MIDI_START = 0X90;

    // TarsosDSP
    public AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
    public MidiDriver midi;
    public int plugin = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_training);

        pitchText = findViewById(R.id.pitchText);

        // TarsosDSP
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        // More TarsosDSP
        AudioProcessor pitchProcessor = new PitchProcessor(
                PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

        // Construct Midi Driver.
        midi = new MidiDriver();
        midi.setOnMidiStartListener(this);

        randomNoteButton = findViewById(R.id.randomToneButton);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (midi != null)
            midi.start();
    }

    /**
     * Converts pitch (hertz) to note index.
     * @param       pitchInHz double;
     * @return      int; ix of note.
     */
    public int convertPitchToIx(double pitchInHz) {
        // No note is heard.
        if (pitchInHz == -1) {
            return -1;
        }
        return PitchConverter.hertzToMidiKey(pitchInHz);
    }

    public void processPitch(float pitchInHz) {
        int curIx = convertPitchToIx((double) pitchInHz);
        pitchText.setText("" + (int) pitchInHz);

        // If detected pitch matches the tone being produced.
        if (curIx == curSoundNoteIx) {
            // If pitch is just being heard.
            if (curIx != curHeardNoteIx) {
                curHeardNoteIx = curIx;
                // Time stamp.
                curHeardNoteIxRegistered = System.currentTimeMillis();
            }
            else {
                // If it has been heard long enough.
                if (System.currentTimeMillis() - curHeardNoteIxRegistered > 150) {
                    randomNoteButton.performClick();
                }
            }
        }

    }

    /**
     * https://github.com/billthefarmer/mididriver/blob/master/app/src/main/java/org/billthefarmer/miditest/MainActivity.java
     *
     * Listener for sending initial midi messages when the Sonivox
     * synthesizer has been started, such as program change.
     */
    @Override
    public void onMidiStart() {
        sendMidiSetup();
    }

    /**
     * https://github.com/billthefarmer/mididriver/blob/master/app/src/main/java/org/billthefarmer/miditest/MainActivity.java
     *
     * Initial setup data for midi.
     */
    protected void sendMidiSetup() {
        byte msg[] = new byte[2];
        msg[0] = (byte) 0XC0;    // 0XC0 == PROGRAM CHANGE
        msg[1] = (byte) plugin;
        midi.write(msg);
    }

    /**
     * https://github.com/billthefarmer/mididriver/blob/master/app/src/main/java/org/billthefarmer/miditest/MainActivity.java
     *
     * Send data that is to be synthesized by midi driver.
     * @param       event int; type of event.
     * @param       midiKey int; index of note (uses octaves).
     * @param       volume int; volume of note.
     */
    protected void sendMidiNote(int event, int midiKey, int volume) {
        byte msg[] = new byte[3];
        msg[0] = (byte) event;
        msg[1] = (byte) midiKey;
        msg[2] = (byte) volume;
        midi.write(msg);
    }

    /**
     * Sends multiple messages to be synthesized by midi driver.
     * Each note is given specifically.
     * @param       event int; type of event.
     * @param       midiKeys int[]; indexes of notes (uses octaves).
     * @param       volume int; volume of notes.
     */
    protected void sendMidiChord(int event, int[] midiKeys, int volume) {
        for (int key : midiKeys) {
            sendMidiNote(event, key, volume);
        }
    }

    private int getRandomNoteIx(Random rng) {
        return rng.nextInt(24) + 48;
    }

    public void generateRandomTone(View view) {
        sendMidiNote(0x80, curSoundNoteIx, 0);
        curSoundNoteIx = getRandomNoteIx(rng);
        sendMidiNote(0x90, curSoundNoteIx, 63);
    }
}
