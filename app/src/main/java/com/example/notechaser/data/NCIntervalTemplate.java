package com.example.notechaser.data;

import com.example.keyfinder.Note;
import com.example.keyfinder.eartraining.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Structure that only stores indices. Doesn't manipulate.
 */
public class NCIntervalTemplate {

    private List<Integer> mIndices;

    private int highestIx;

    private int lowestIx;

    public NCIntervalTemplate() {
        mIndices = new ArrayList<>();
        highestIx = -1;
        lowestIx = -1;
    }

    public NCIntervalTemplate(Integer[] indices) {
        this(Arrays.asList(indices));
    }

    public NCIntervalTemplate(List<Integer> indices) {
        mIndices = indices;
        findRange();
    }

    private void findRange() {
        if (size() == 0) {
            highestIx = lowestIx = -1;
        }
        else {
            highestIx = lowestIx = mIndices.get(0);
            for (int ix : mIndices) {
                if (ix < lowestIx) {
                    lowestIx = ix;
                }
                else if (ix > highestIx) {
                    highestIx = ix;
                }
            }
        }
    }

    public int getSpaceRequired() {
        return highestIx - lowestIx;
    }

    public Pattern generatePattern(int key) {
        Pattern pattern = new Pattern();
        for (int ix : mIndices) {
            pattern.getNotes().add(new Note(ix + key));
        }
        return pattern;
    }

    public int size() {
        return mIndices.size();
    }

}
