package com.cannonballapps.notechaser.exercisesetup

import android.util.Range

data class ExerciseSetupUiData(
    val exerciseSetupUiItems: List<ExerciseSetupUiItem>,
)

// todo img resource
sealed interface ExerciseSetupUiItem {
    data class Header(
        val text: String,
    ) : ExerciseSetupUiItem

    data class ListPicker(
        val headerText: String,
        val bodyText: String,
        val imageResourceId: Int? = null,
        val onShowDialog: ((values: List<String>?) -> Unit)? = null, // TODO removve null
        val onClick: (() -> Unit)?,
        val values: List<String>? = null, // TODO remove null
        // todo list values
        // todo on list item selected
    ) : ExerciseSetupUiItem

    data class Switch(
        val headerText: String,
        val bodyText: String,
        val isEnabled: Boolean,
        val onSwitchChange: (Boolean) -> Unit,
        val imageResourceId: Int? = null,
    ) : ExerciseSetupUiItem

    // todo: replace usages of `Range<Int>` with `IntRange`
    data class RangeBar(
        val headerText: String,
        val bodyText: String,
        val bounds: Range<Int>,
        val stepSize: Int,
        val currentValue: Range<Int>,
        val onValueChange: (Range<Int>) -> Unit,
        val imageResourceId: Int? = null,
    ) : ExerciseSetupUiItem

    // todo: replace usages of `Range<Int>` with `IntRange`
    data class Slider(
        val headerText: String,
        val bodyText: String,
        val bounds: Range<Int>,
        val stepSize: Int,
        val currentValue: Int,
        val onValueChange: (Int) -> Unit,
        val imageResourceId: Int? = null,
    ) : ExerciseSetupUiItem
}

