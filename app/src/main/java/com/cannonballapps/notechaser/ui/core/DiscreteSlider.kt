package com.cannonballapps.notechaser.ui.core

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cannonballapps.notechaser.common.size
import com.cannonballapps.notechaser.common.toFloatRange
import com.cannonballapps.notechaser.common.toIntRange
import kotlin.math.roundToInt

@Composable
fun DiscreteSlider(
    value: Int,
    valueRange: IntRange,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    stepSize: Int = 1,
) {
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.roundToInt()) },
        enabled = true,
        valueRange = valueRange.toFloatRange(),
        modifier = modifier,
        steps = (valueRange.size / stepSize) - 1,
        colors = SliderDefaults.colors(
            activeTickColor = MaterialTheme.colorScheme.primary,
            inactiveTickColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscreteRangeBar(
    values: IntRange,
    valueRange: IntRange,
    onValueChange: (IntRange) -> Unit,
    modifier: Modifier = Modifier,
    stepSize: Int = 1,
) {
    RangeSlider(
        value = values.toFloatRange(),
        onValueChange = { onValueChange(it.toIntRange()) },
        enabled = true,
        valueRange = valueRange.toFloatRange(),
        modifier = modifier,
        steps = (valueRange.size / stepSize) - 1,
        colors = SliderDefaults.colors(
            activeTickColor = MaterialTheme.colorScheme.primary,
            inactiveTickColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )
}

@DevicePreviews
@Composable
private fun DiscreteSliderPreview() {
    DiscreteSlider(
        value = 2,
        valueRange = 0..10,
        onValueChange = { /* No-op */ },
    )
}

@DevicePreviews
@Composable
private fun DiscreteRangeBarPreview() {
    DiscreteRangeBar(
        values = 2..5,
        valueRange = 0..10,
        onValueChange = { /* No-op */ },
    )
}
