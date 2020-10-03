package com.example.notechaser.models.noteprocessor

/**
 * Process incoming pitch data so that noise can be filtered out from actual notes.
 */
class NoteProcessor {
    // todo: tinker with this value
    private val PROBABILITY_THRESHOLD = 0.0f

    //    public void addNoteProcessorListener(NoteProcessorListener listener) {
    //        mListener.add(listener);
    //    }
    //
    //    public void removeNoteProcessorListener(NoteProcessorListener listener) {
    //        mListener.remove(listener);
    //    }
    var listener: NoteProcessorListener? = null

    // todo: either tinker, or turn this into a parameter
    var millisRequired = 200
    private var mPrevNoteHeardIx = -1
    private var mPrevHeardTimeStamp: Long = -1
    private var mLastDetectedNoteIx = -1
    private var mHasNotifiedDetected = false
    private var mHasNotifiedUndetected = false

    // todo: for now, pitch detection is only monophonic;
    //       if polyphonic detection is added in later, then
    //       this will need to be reworked
    fun onPitchDetected(noteIx: Int, probability: Float, isPitched: Boolean) {
        // todo: use other parameters later to better filter out noise / incorrect guesses
        if (noteIx != mPrevNoteHeardIx) {
            mHasNotifiedDetected = false
            //            if (!mHasNotifiedUndetected && mLastDetectedNoteIx != -1) {
//                if (mListener != null) {
//                    mListener.notifyNoteUndetected(mLastDetectedNoteIx);
//                }
//                mHasNotifiedUndetected = true;
//            }
            if (noteIx != -1) {
                mPrevHeardTimeStamp = System.currentTimeMillis()
            }
            mPrevNoteHeardIx = noteIx
        }
        else if (noteIx != -1 && !mHasNotifiedDetected && hasMetFilterLengthThreshold()) {
            if (listener != null) {
                listener!!.notifyNoteDetected(noteIx)
            }
            mLastDetectedNoteIx = noteIx
            mHasNotifiedUndetected = false
            mHasNotifiedDetected = true
        }
    }

    private fun hasMetFilterLengthThreshold(): Boolean {
        return (System.currentTimeMillis() - mPrevHeardTimeStamp).toInt() > millisRequired
    }
}