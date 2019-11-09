package com.example.notechaser.models.patternengine;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PatternEngineImplTest {

    @Test
    public void testShit() {
        assertTrue(true);
    }

    @Test
    public void testMoreShit() {
        PatternEngine engine = new PatternEngineImpl();
        engine.setBounds(24, 60);
        engine.addPhraseTemplate(pt_4_6_1_2_13());
        engine.addMode(new MajorMode(3)); // lydian

        // Test begins
        engine.generatePattern();
        Pattern cur = engine.getCurPattern();

        /* play pattern */

        List<Note> answer = new ArrayList<Note>();

        System.out.println(cur);

        for (Note note : cur.getNotes()) {
            // Make copies of note
            answer.add(new Note(note.getIx()));
        }

//        assertTrue(engine.isAnswerCorrect(answer));
    }

    private PhraseTemplate pt_4_6_1_2_13() {
        PhraseTemplate temp = new PhraseTemplate();
        temp.addDegree(3);
        temp.addDegree(5);
        temp.addDegree(0);
        temp.addDegree(1);
        temp.addDegree(12);
        return temp;
    }

}