package com.example.notechaser.data.exercisesetup

sealed class ExerciseSetupItem {


    data class Header(val header: ExerciseSetupHeader) : ExerciseSetupItem()

    data class SingleList(val entries: List<String>, val values: List<String>) : ExerciseSetupItem() {

    }

    data class Spinner(val spinner: ExerciseSetupSpinner) : ExerciseSetupItem()

    data class MultiList(val entries: List<String>, val values: List<String>) : ExerciseSetupItem() {

    }

    data class Switch(val switch: ExerciseSetupSwitch) : ExerciseSetupItem()

}