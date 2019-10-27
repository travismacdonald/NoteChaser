package com.example.notechaser;

import com.example.keyfinder.MajorMode;
import com.example.keyfinder.MelodicMinorMode;
import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;
import com.example.keyfinder.eartraining.PhraseTemplate;
import com.example.notechaser.patterngenerator.RandomPatternGenerator;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomPatternGeneratorTest {

    @Test
    public void poorExampleOfTest() {
    }

    @Test
    public void testShit() {
        assertTrue(true);
    }

    @Test
    public void testRandomGeneration() {
        RandomPatternGenerator rpg = new RandomPatternGenerator();

        rpg.addMode(new MajorMode(0));
        rpg.addPhraseTemplate(pt_1_3_5());

//        assertEquals(7, rpg.calculateOverallSpaceNeeded());
    }

    @Test
    public void testRandomGeneration_2() {
        RandomPatternGenerator rpg = new RandomPatternGenerator();

        rpg.addMode(new MajorMode(0));
        rpg.addMode(new MelodicMinorMode(2));
        rpg.addPhraseTemplate(pt_1_3_5());

//        assertEquals(8, rpg.calculateOverallSpaceNeeded());
    }

    @Test
    public void testRandomGeneration_3() {
        RandomPatternGenerator rpg = new RandomPatternGenerator();

        rpg.addMode(new MajorMode(0));
        rpg.addMode(new MajorMode(3));
        rpg.addMode(new MajorMode(6));
        rpg.addPhraseTemplate(pt_3());

//        assertEquals(0, rpg.calculateOverallSpaceNeeded());
    }

    @Test
    public void testRandomGeneration_4() {
        RandomPatternGenerator rpg = new RandomPatternGenerator();

        rpg.addMode(new MajorMode(0));
        rpg.addMode(new MajorMode(3));
        rpg.addMode(new MajorMode(6));

        rpg.addPhraseTemplate(pt_3());
        rpg.addPhraseTemplate(pt_1_3_5());
        rpg.addPhraseTemplate(pt_1_3_5_7());

//        assertEquals(11, rpg.calculateOverallSpaceNeeded());
    }

    @Test
    public void testRandomGeneration_5() {
        RandomPatternGenerator rpg = new RandomPatternGenerator();

        rpg.addMode(new MajorMode(0));
        rpg.addMode(new MajorMode(3));
        rpg.addMode(new MajorMode(6));
        rpg.addMode(new MelodicMinorMode(2));

        rpg.addPhraseTemplate(pt_3());
        rpg.addPhraseTemplate(pt_1_3_5());
        rpg.addPhraseTemplate(pt_1_3_5_7());
        rpg.addPhraseTemplate(pt_4_6_1_2_13());
        rpg.setLowerBound(30);
        rpg.setUpperBound(60);

        assertTrue(rpg.hasSufficientSpace());
//        rpg.setLowerBound(37);
//        assertFalse(rpg.hasSufficientSpace());
//        assertEquals(21, rpg.calculateOverallSpaceNeeded());

        for (int i = 0; i < 1000; i++) {
            boolean flag = false;
            boolean max = false;
            Pattern p = rpg.generatePattern();
            String pString = p.toString();
            for (Note note : p.getNotes()) {
                if (note.getIx() == rpg.getUpperBound()) {
                    pString += " <- max";
                    break;
                }
                else if (note.getIx() == rpg.getLowerBound()) {
                    pString += " <- min";
                    break;
                }
                else if (note.getIx() < rpg.getLowerBound() || note.getIx() > rpg.getUpperBound()) {
                    pString += " <- you fucked up";
                    break;
                }
            }
            System.out.println(pString);
        }
    }

    private PhraseTemplate pt_1_3_5() {
        PhraseTemplate temp = new PhraseTemplate();
        temp.addDegree(0);
        temp.addDegree(2);
        temp.addDegree(4);
        return temp;
    }

    private PhraseTemplate pt_1_3_5_7() {
        PhraseTemplate temp = new PhraseTemplate();
        temp.addDegree(0);
        temp.addDegree(2);
        temp.addDegree(4);
        temp.addDegree(6);
        return temp;
    }

    private PhraseTemplate pt_1_3_5_7_9() {
        PhraseTemplate temp = new PhraseTemplate();
        temp.addDegree(0);
        temp.addDegree(2);
        temp.addDegree(4);
        temp.addDegree(6);
        temp.addDegree(8);
        return temp;
    }

    private PhraseTemplate pt_3() {
        PhraseTemplate temp = new PhraseTemplate();
        temp.addDegree(2);
        return temp;
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