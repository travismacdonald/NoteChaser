package com.example.notechaser.data.exercisesetup

// TODO: Refactor this so all items are contained in this class
sealed class ExerciseSetupItem {

    data class Header(val header: ExerciseSetupHeader) : ExerciseSetupItem()

    data class SingleList(val list: ExerciseSetupSingleList) : ExerciseSetupItem()

    data class MultiList(val list: ExerciseSetupMultiList) : ExerciseSetupItem()

    data class Switch(val switch: ExerciseSetupSwitch) : ExerciseSetupItem()

    data class Slider(val slider: ExerciseSetupSlider) : ExerciseSetupItem()

    data class RangeBar(val rangeBar: ExerciseSetupRangeBar) : ExerciseSetupItem()

}