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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.common.toIntRange
import com.cannonballapps.notechaser.common.toRangeInt
import com.cannonballapps.notechaser.common.ui.DiscreteRangeBar
import com.cannonballapps.notechaser.common.ui.DiscreteSlider
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.ui.core.DevicePreviews
import com.cannonballapps.notechaser.ui.core.NCDialog
import com.cannonballapps.notechaser.ui.theme.NoteChaserTheme

data class DialogData(
    val values: List<String>,
    val initialSelectedValue: Int,
    val onValueSelected: (Int) -> Unit,
)

@Composable
fun ExerciseSetupScreen(
    viewModel: ExerciseSetupViewModel = viewModel(),
) {
    val exerciseSettingsState: State<ExerciseSettings> =
        viewModel.exerciseSettingsStream.collectAsState()

    var dialogData: DialogData? by remember { mutableStateOf(null) }

    ExerciseSetupList(
        exerciseSettings = exerciseSettingsState.value,
        viewModel = viewModel,
        setDialogContent = {
            dialogData = it
        },
    )

    dialogData?.let {
        NCDialog(
            dialogData = it,
            onDismissRequest = { dialogData = null },
        )
    }
}

@Composable
fun ExerciseSetupList(
    exerciseSettings: ExerciseSettings,
    viewModel: ExerciseSetupViewModel,
    setDialogContent: (DialogData) -> Unit,
) {
    val questionSettingsHeader = ExerciseSetupUiItem.Header(
        text = stringResource(R.string.answerSettings_header),
    )
    val questionKeyItem = ExerciseSetupUiItem.ListPicker(
        headerText = stringResource(R.string.questionKey_title),
        bodyText = exerciseSettings.sessionQuestionSettings.questionKey.toString(),
        values = PitchClass.values().toList().map { it.toString() }, // TODO hoist
        onClick = {
            setDialogContent(
                DialogData(
                    values = PitchClass.values().toList().map { it.toString() },
                    initialSelectedValue = exerciseSettings.sessionQuestionSettings.questionKey.ordinal,
                    onValueSelected = { selectedIx -> viewModel.setQuestionKey(PitchClass.values()[selectedIx]) },
                ),
            )
        },
    )
    val playStartingPitchItem = ExerciseSetupUiItem.Switch(
        headerText = stringResource(R.string.playStartingPitch_title),
        bodyText = stringResource(R.string.playStartingPitch_summary),
        imageResourceId = R.drawable.ic_music_note_black_40dp,
        isEnabled = exerciseSettings.sessionQuestionSettings.shouldPlayStartingPitch,
        onSwitchChange = viewModel::onPlayStartingPitchCheckChanged,
    )
    val matchOctaveItem = ExerciseSetupUiItem.Switch(
        headerText = stringResource(R.string.matchOctave_title),
        bodyText = stringResource(R.string.matchOctave_summary),
        imageResourceId = null,
        isEnabled = exerciseSettings.sessionQuestionSettings.shouldMatchOctave,
        onSwitchChange = viewModel::onMatchOctaveCheckChanged,
    )
    val playableRangeItem = ExerciseSetupUiItem.RangeBar(
        headerText = stringResource(R.string.questionRange_title),
        bodyText = exerciseSettings.sessionQuestionSettings.playableBounds.toPlayableRangeString(),
        bounds = Range(
            MusicTheoryUtils.MIN_MIDI_NUMBER,
            MusicTheoryUtils.MAX_MIDI_NUMBER,
        ),
        stepSize = 1,
        currentValue = Range(
            exerciseSettings.sessionQuestionSettings.playableBounds.lower.midiNumber,
            exerciseSettings.sessionQuestionSettings.playableBounds.upper.midiNumber,
        ),
        onValueChange = { values -> viewModel.onPlayableBoundsChanged(values.toNoteRange()) },
    )

    val lengthHeader = ExerciseSetupUiItem.Header(
        text = stringResource(R.string.sessionSettings_header),
    )
    val limitTypeItem = ExerciseSetupUiItem.ListPicker(
        headerText = stringResource(R.string.sessionType_title),
        bodyText = exerciseSettings.sessionLengthSettings.toFormattedString(),
        onClick = null, // TODO
    )
    val limitValueItem: ExerciseSetupUiItem? = when (val lengthSettings = exerciseSettings.sessionLengthSettings) {
        is SessionLengthSettings.QuestionLimit -> {
            ExerciseSetupUiItem.Slider(
                headerText = stringResource(R.string.numQuestions_title),
                bodyText = lengthSettings.numQuestions.toString(),
                bounds = Range(
                    integerResource(R.integer.numQuestions_min),
                    integerResource(R.integer.numQuestions_max),
                ),
                stepSize = 5,
                currentValue = lengthSettings.numQuestions,
                onValueChange = viewModel::onNumQuestionsChange,
            )
        }
        is SessionLengthSettings.TimeLimit -> {
            ExerciseSetupUiItem.Slider(
                headerText = stringResource(R.string.sessionTimeLimit_title),
                bodyText = stringResource(R.string.sessionTimeLimit_summary),
                bounds = Range(
                    integerResource(R.integer.sessionTimeLimit_min),
                    integerResource(R.integer.sessionTimeLimit_max),
                ),
                stepSize = 5,
                currentValue = lengthSettings.timeLimitMinutes,
                onValueChange = viewModel::onTimeLimitMinutesChange,
            )
        }
        SessionLengthSettings.NoLimit -> {
            null
        }
    }

    val notePoolHeaderItem = ExerciseSetupUiItem.Header(
        text = stringResource(R.string.questionSettings_header),
    )
    val notePoolTypeListItem = ExerciseSetupUiItem.ListPicker(
        headerText = stringResource(R.string.notePoolType_title),
        bodyText = exerciseSettings.notePoolType.toString(),
        onClick = null, // TODO
    )

    ExerciseSetupList(
        listOfNotNull(
            questionSettingsHeader,
            questionKeyItem,
            playStartingPitchItem,
            matchOctaveItem,
            playableRangeItem,

            lengthHeader,
            limitTypeItem,
            limitValueItem,

            notePoolHeaderItem,
            notePoolTypeListItem,
        )
    )

}

@Composable
fun ExerciseSetupList(
    exerciseSetupUiItems: List<ExerciseSetupUiItem>,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(exerciseSetupUiItems) { item ->
            when (item) {
                is ExerciseSetupUiItem.ListPicker -> ExerciseSetupListPickerCard(item)
                is ExerciseSetupUiItem.Header -> ExerciseSetupHeaderCard(item)
                is ExerciseSetupUiItem.Switch -> ExerciseSetupSwitchCard(item)
                is ExerciseSetupUiItem.RangeBar -> ExerciseSetupRangeBarCard(item)
                is ExerciseSetupUiItem.Slider -> ExerciseSetupSliderCard(item)
            }
        }
    }
}

@Composable
fun ExerciseSetupHeaderCard(
    item: ExerciseSetupUiItem.Header,
) {
    // TODO: extract hardcoded color
    Surface(color = Color(0xFFDDDDDD)) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = item.text,
                style = MaterialTheme.typography.body1,
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
    // TODO: add list item values
    // TODO: add on click listener
    // TODO: add on list item selected click listener
    Surface(
        color = Color.White,
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
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        end = 16.dp,
                    ),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.bodyText,
                    style = MaterialTheme.typography.subtitle2,
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
    Surface(color = Color.White) {
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
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .padding(top = 16.dp),
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = item.bodyText,
                    style = MaterialTheme.typography.subtitle2,
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
    Surface(color = Color.White) {
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
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )
                    Text(
                        text = item.bodyText,
                        style = MaterialTheme.typography.subtitle2,
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
    Surface(color = Color.White) {
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
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(start = 8.dp),
                    )
                    Text(
                        text = item.bodyText,
                        style = MaterialTheme.typography.subtitle2,
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
                tint = Color.Black,
                modifier = Modifier
                    .fillMaxSize(),
                contentDescription = null,
            )
        }
    }
}

private fun Range<Int>.toNoteRange(): Range<Note> = Range(
    Note(this.lower),
    Note(this.upper),
)

private fun Range<Note>.toPlayableRangeString(): String = "$lower - $upper"

private fun SessionLengthSettings.toFormattedString(): String = when (this) {
    is SessionLengthSettings.QuestionLimit -> "Question Limit"
    is SessionLengthSettings.TimeLimit -> "Time Limit"
    SessionLengthSettings.NoLimit -> "Unlimited"
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

@DevicePreviews
@Composable
private fun ExerciseSetupListPreview() {
    NoteChaserTheme {
        ExerciseSetupList(
            exerciseSetupUiItems = listOf()
        )
        // TODO: fix preview when mapping is finalized
//        ExerciseSetupList(
//            exerciseSettings = ExerciseSettings(
//                notePoolType = NotePoolType.Diatonic(
//                    degrees = booleanArrayOf(true, true, false),
//                    scale = ParentScale.Major.Ionian,
//                ),
//                sessionSettings = SessionSettings(
//                    questionKey = PitchClass.C,
//                    shouldMatchOctave = false,
//                    shouldPlayStartingPitch = false,
//                    playableLowerBound = Note(midiNumber = 36),
//                    playableUpperBound = Note(midiNumber = 60),
//                ),
//                sessionLengthSettings = SessionLengthSettings.QuestionLimit(
//                    numQuestions = 10,
//                ),
//            ),
//
//        )
    }
}
