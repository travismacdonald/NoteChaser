package com.example.notechaser;

import com.example.notechaser.patterngenerator.AbstractPatternGenerator;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractPatternGeneratorTest {
//
//    @Test
//    public void poorExampleOfTest() {
//    }
//
//    @Test
//    public void testShit() {
//        assertTrue(true);
//    }
//
//    @Test
//    public void testRandomGeneration() {
//        AbstractPatternGenerator rpg = new AbstractPatternGenerator();
//
//        rpg.addMode(new MajorMode(0));
//        rpg.addAbstractTemplate(pt_1_3_5());
//
////        assertEquals(7, rpg.calculateOverallSpaceNeeded());
//    }
//
//    @Test
//    public void testRandomGeneration_2() {
//        AbstractPatternGenerator rpg = new AbstractPatternGenerator();
//
//        rpg.addMode(new MajorMode(0));
//        rpg.addMode(new MelodicMinorMode(2));
//        rpg.addAbstractTemplate(pt_1_3_5());
//
////        assertEquals(8, rpg.calculateOverallSpaceNeeded());
//    }
//
//    @Test
//    public void testRandomGeneration_3() {
//        AbstractPatternGenerator rpg = new AbstractPatternGenerator();
//
//        rpg.addMode(new MajorMode(0));
//        rpg.addMode(new MajorMode(3));
//        rpg.addMode(new MajorMode(6));
//        rpg.addAbstractTemplate(pt_3());
//
////        assertEquals(0, rpg.calculateOverallSpaceNeeded());
//    }
//
//    @Test
//    public void testRandomGeneration_4() {
//        AbstractPatternGenerator rpg = new AbstractPatternGenerator();
//
//        rpg.addMode(new MajorMode(0));
//        rpg.addMode(new MajorMode(3));
//        rpg.addMode(new MajorMode(6));
//
//        rpg.addAbstractTemplate(pt_3());
//        rpg.addAbstractTemplate(pt_1_3_5());
//        rpg.addAbstractTemplate(pt_1_3_5_7());
//
////        assertEquals(11, rpg.calculateOverallSpaceNeeded());
//    }
//
//    @Test
//    public void testRandomGeneration_5() {
//        AbstractPatternGenerator rpg = new AbstractPatternGenerator();
//
//        rpg.addMode(new MajorMode(0));
//        rpg.addMode(new MajorMode(3));
//        rpg.addMode(new MajorMode(6));
//        rpg.addMode(new MelodicMinorMode(2));
//
//        rpg.addAbstractTemplate(pt_3());
//        rpg.addAbstractTemplate(pt_1_3_5());
//        rpg.addAbstractTemplate(pt_1_3_5_7());
//        rpg.addAbstractTemplate(pt_4_6_1_2_13());
//        rpg.setLowerBound(30);
//        rpg.setUpperBound(60);
//
//        assertTrue(rpg.hasSufficientSpace());
////        rpg.setLowerBound(37);
////        assertFalse(rpg.hasSufficientSpace());
////        assertEquals(21, rpg.calculateOverallSpaceNeeded());
//
//        for (int i = 0; i < 1000; i++) {
//            boolean flag = false;
//            boolean max = false;
//            Pattern p = rpg.generatePattern();
//            String pString = p.toString();
//            for (Note note : p.getNotes()) {
//                if (note.getIx() == rpg.getUpperBound()) {
//                    pString += " <- max";
//                    break;
//                }
//                else if (note.getIx() == rpg.getLowerBound()) {
//                    pString += " <- min";
//                    break;
//                }
//                else if (note.getIx() < rpg.getLowerBound() || note.getIx() > rpg.getUpperBound()) {
//                    pString += " <- you fucked up";
//                    break;
//                }
//            }
//            System.out.println(pString);
//        }
//    }
//
//    private AbstractTemplate pt_1_3_5() {
//        AbstractTemplate temp = new AbstractTemplate();
//        temp.addDegree(0);
//        temp.addDegree(2);
//        temp.addDegree(4);
//        return temp;
//    }
//
//    private AbstractTemplate pt_1_3_5_7() {
//        AbstractTemplate temp = new AbstractTemplate();
//        temp.addDegree(0);
//        temp.addDegree(2);
//        temp.addDegree(4);
//        temp.addDegree(6);
//        return temp;
//    }
//
//    private AbstractTemplate pt_1_3_5_7_9() {
//        AbstractTemplate temp = new AbstractTemplate();
//        temp.addDegree(0);
//        temp.addDegree(2);
//        temp.addDegree(4);
//        temp.addDegree(6);
//        temp.addDegree(8);
//        return temp;
//    }
//
//    private AbstractTemplate pt_3() {
//        AbstractTemplate temp = new AbstractTemplate();
//        temp.addDegree(2);
//        return temp;
//    }
//
//    private AbstractTemplate pt_4_6_1_2_13() {
//        AbstractTemplate temp = new AbstractTemplate();
//        temp.addDegree(3);
//        temp.addDegree(5);
//        temp.addDegree(0);
//        temp.addDegree(1);
//        temp.addDegree(12);
//        return temp;
//    }

}