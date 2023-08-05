package com.cannonballapps.notechaser.exercisegenerator

import androidx.lifecycle.ViewModel
import com.cannonballapps.notechaser.musicutilities.MusicTheoryUtils
import com.cannonballapps.notechaser.musicutilities.ParentScale
import com.cannonballapps.notechaser.musicutilities.Scale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExerciseGeneratorViewModel @Inject constructor() : ViewModel() {

    val exerciseFlow = MutableStateFlow<ExerciseState>(ExerciseState.Loading)

    fun setType(type: GeneratorType) {
        exerciseFlow.value = ExerciseState.Available(
            ScaleHarmonic(
                scale = listOf(ParentScale.Major, ParentScale.HarmonicMinor, ParentScale.MelodicMinor).random(),
                stringGroup = (1..3).random(),
                cycle = (1..7).random(),
                voicingType = listOf<String>(
                    "Drop 2",
                    "Drop 3",
                    "Drop 2 & 3",
                    "Drop 2 & 4",
                    "Closed Triad",
                    "Open Triad",
                ).random(),
            )
        )
    }
}

sealed interface ExerciseState {
    object Loading : ExerciseState
    data class Available(val exercise: ScaleHarmonic) : ExerciseState
}

//sealed interface GenExercise {
    data class ScaleHarmonic(
        val scale: ParentScale,
        val stringGroup: Int,
        val cycle: Int,
        val voicingType: String,
//    ) : GenExercise
    )

//    data class StandardHarmonic(
//        val
//    ) : GenExercise
//
//}
