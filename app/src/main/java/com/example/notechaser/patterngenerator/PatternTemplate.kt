package com.example.notechaser.patterngenerator

import java.io.Serializable
import java.util.*

// todo: change serializable to parcelable
class PatternTemplate : Serializable {
    var indices: List<Int>
        private set
    private var highestIx = 0
    private var lowestIx = 0

    constructor() {
        indices = ArrayList()
        highestIx = -1
        lowestIx = -1
    }

    constructor(indices: Array<Int?>) : this(Arrays.asList<Int>(*indices)) {}
    constructor(indices: List<Int>) {
        this.indices = indices
        findRange()
    }

    private fun findRange() {
        if (size() == 0) {
            lowestIx = -1
            highestIx = lowestIx
        } else {
            lowestIx = indices[0]
            highestIx = lowestIx
            for (ix in indices) {
                if (ix < lowestIx) {
                    lowestIx = ix
                } else if (ix > highestIx) {
                    highestIx = ix
                }
            }
        }
    }

    val spaceRequired: Int
        get() = highestIx - lowestIx

    //    public Pattern generatePattern(int key) {
    //        Pattern pattern = new Pattern();
    //        for (int ix : indices) {
    //            pattern.getNotes().add(new Note(ix + key));
    //        }
    //        return pattern;
    //    }
    fun size(): Int {
        return indices.size
    }

}