package com.cannonballapps.notechaser.exercisesetup

import android.util.Range
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cannonballapps.notechaser.R
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.common.SessionLengthSettings
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.Note
import com.cannonballapps.notechaser.musicutilities.PitchClass
import com.cannonballapps.notechaser.musicutilities.toStrings
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
    val questionKeyItem = getQuestionKeyItem(
        headerText = stringResource(id = R.string.questionKey_title),
        currentSelectedKey = exerciseSettings.sessionQuestionSettings.questionKey,
        values = exerciseSettings.sessionQuestionSettings.questionKeyValues,
        setDialogContent = setDialogContent,
        onQuestionKeySelected = { selectedKey -> viewModel.setQuestionKey(selectedKey) },
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
private fun getQuestionKeyItem(
    headerText: String,
    currentSelectedKey: PitchClass,
    values: List<PitchClass>,
    setDialogContent: (DialogData) -> Unit,
    onQuestionKeySelected: (PitchClass) -> Unit,
) =
    ExerciseSetupUiItem.ListPicker(
        headerText = headerText,
        bodyText = currentSelectedKey.toString(),
        onClick = {
            setDialogContent(
                DialogData(
                    values = values.toStrings(),
                    initialSelectedValue = values.indexOf(currentSelectedKey),
                    onValueSelected = { selectedIx ->
                        onQuestionKeySelected(values[selectedIx])
                    },
                ),
            )
        },
    )


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
