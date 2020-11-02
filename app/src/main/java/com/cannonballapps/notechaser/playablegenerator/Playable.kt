package com.cannonballapps.notechaser.playablegenerator



/**
 * Patterns store Notes that are transposed so that the lowest
 * Note index matches the given index.
 * The Notes in a Pattern are immutable, which is why it throws
 * an exception when it is given an empty PatternTemplate.
 */
class Playable(template: PlayableTemplate, ix: Int) {

    val notes: List<Note>

    val size = template.size

    init {
        if (template.isEmpty())
            throw IllegalStateException("Cannot construct Pattern from empty PatternTemplate.")
        else notes = template.intervalsTransposed.map { Note(it + ix) }
    }

    // todo: equals and hashcode method

    override fun equals(other: Any?): Boolean {
        return (other is Playable)
                && notes == other.notes
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun toStringFlat(): String {
        return notes.joinToString(separator = " ") { it.nameFlat }
    }

    fun toStringSharp(): String {
        return notes.joinToString(separator = " ") { it.nameSharp }
    }

    override fun toString(): String {
        return notes.joinToString(separator = " ") { it.ix.toString() }
    }

}