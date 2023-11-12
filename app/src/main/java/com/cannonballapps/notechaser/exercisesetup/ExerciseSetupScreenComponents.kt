package com.cannonballapps.notechaser.exercisesetup

import android.util.Range
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.toIntRange
import com.cannonballapps.notechaser.common.toRangeInt
import com.cannonballapps.notechaser.ui.core.DiscreteRangeBar
import com.cannonballapps.notechaser.ui.core.DiscreteSlider
import com.cannonballapps.notechaser.ui.core.DevicePreviews
import com.cannonballapps.notechaser.ui.theme.NoteChaserTheme

@Composable
fun ExerciseSetupHeaderCard(
    item: ExerciseSetupUiItem.Header,
) {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(all = 16.dp),
            )
        }
    }
}

@Composable
fun ExerciseSetupListPickerCard(
    item: ExerciseSetupUiItem.ListPicker,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.clickable { item.onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            ExerciseSetupCardIcon(item.imageResourceId)

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = item.headerText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        end = 16.dp,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.bodyText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        bottom = 16.dp,
                        end = 16.dp,
                    ),
                )
            }
        }
    }
}

@Composable
fun ExerciseSetupSwitchCard(
    item: ExerciseSetupUiItem.Switch,
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseSetupCardIcon(item.imageResourceId)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.headerText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = item.bodyText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                )
            }

            Switch(
                checked = item.isEnabled,
                onCheckedChange = { item.onSwitchChange(it) },
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun ExerciseSetupSliderCard(
    item: ExerciseSetupUiItem.Slider,
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseSetupCardIcon(item.imageResourceId)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = item.headerText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )
                    Text(
                        text = item.bodyText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(end = 24.dp),
                    )
                }
                DiscreteSlider(
                    value = item.currentValue,
                    onValueChange = { item.onValueChange(it) },
                    valueRange = item.bounds.toIntRange(),
                    stepSize = 5, // todo
                    modifier = Modifier.padding(end = 16.dp),
                )
            }
        }
    }
}

@Composable
fun ExerciseSetupRangeBarCard(
    item: ExerciseSetupUiItem.RangeBar,
) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseSetupCardIcon(item.imageResourceId)

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = item.headerText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )
                    Text(
                        text = item.bodyText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(end = 24.dp),
                    )
                }
                DiscreteRangeBar(
                    values = item.currentValue.toIntRange(),
                    onValueChange = { item.onValueChange(it.toRangeInt()) },
                    valueRange = item.bounds.toIntRange(),
                    stepSize = item.stepSize,
                    modifier = Modifier
                        .padding(end = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun ExerciseSetupCardIcon(
    imageResourceId: Int?,
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .requiredSize(size = 32.dp),
    ) {
        imageResourceId?.let { id ->
            Icon(
                painter = painterResource(id = id),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupHeaderCardPreview() {
    NoteChaserTheme {
        ExerciseSetupHeaderCard(
            ExerciseSetupUiItem.Header(
                text = "A header",
            ),
        )
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupListPickerCardPreview() {
    NoteChaserTheme {
        ExerciseSetupListPickerCard(
            ExerciseSetupUiItem.ListPicker(
                headerText = "Note Pool Type",
                bodyText = "Chromatic",
                onShowDialog = { /* No-op */ },
                imageResourceId = R.drawable.ic_music_note_black_40dp,
                onClick = { /* No-op */ },
            )
        )
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupSwitchCardPreview() {
    NoteChaserTheme {
        ExerciseSetupSwitchCard(
            ExerciseSetupUiItem.Switch(
                headerText = "Starting pitch",
                bodyText = "Play a reference pitch before starting the session",
                isEnabled = true,
                onSwitchChange = { /* No-op */ },
                imageResourceId = R.drawable.ic_music_note_black_40dp,
            ),
        )
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupSliderCardPreview() {
    NoteChaserTheme {
        ExerciseSetupSliderCard(
            ExerciseSetupUiItem.Slider(
                headerText = "Question Limit",
                bodyText = "40",
                bounds = Range(
                    10,
                    80,
                ),
                stepSize = 5,
                currentValue = 40,
                onValueChange = { /* No-op */ },
                imageResourceId = R.drawable.ic_music_note_black_40dp,
            ),
        )
    }
}

@DevicePreviews
@Composable
private fun ExerciseSetupRangeBarCardPreview() {
    NoteChaserTheme {
        ExerciseSetupRangeBarCard(
            ExerciseSetupUiItem.RangeBar(
                headerText = "Playable Bounds",
                bodyText = "C4 to G5",
                bounds = Range(
                    10,
                    80,
                ),
                stepSize = 1,
                currentValue = Range(
                    36,
                    50,
                ),
                onValueChange = { /* No-op */ },
                imageResourceId = R.drawable.ic_music_note_black_40dp,
            ),
        )
    }
}
