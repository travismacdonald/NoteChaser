package com.cannonballapps.notechaser.models

//import com.cannonballapps.notechaser.playablegenerator.Playable
//import timber.log.Timber
//import java.util.*
//
//class AnswerChecker {
//
//    var targetAnswer: Playable? = null
//
//    var userAnswer: MutableList<Int> = LinkedList()
//
//    // todo: this needs to match the value from ExerciseSetupSettings
//    var matchOctave = false
//
//    fun addUserNote(note: Int) {
//        if (targetAnswer != null && userAnswer.size == targetAnswer!!.size) {
//            // Pop if user answer is same size
//            userAnswer.removeAt(0)
//        }
//        userAnswer.add(note)
//
//        Timber.d("target: ${targetAnswer!!.notes}, user: $userAnswer")
//    }
//
//    fun areAnswersSame() : Boolean {
//        if (targetAnswer == null || targetAnswer!!.size != userAnswer.size) {
//            return false
//        }
//        for (i in 0 until targetAnswer!!.size) {
//            if (targetAnswer!!.notes[i].ix != userAnswer[i]) {
//                return false
//            }
//        }
//        return true
//    }
//
//
//
//}