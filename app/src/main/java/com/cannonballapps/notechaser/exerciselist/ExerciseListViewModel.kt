package com.cannonballapps.notechaser.exerciselist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonballapps.notechaser.common.ExerciseSettings
import com.cannonballapps.notechaser.exerciselist.ExerciseListUiState.Loading
import com.cannonballapps.notechaser.exerciselist.ExerciseListUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ExerciseListViewModel @Inject constructor(

) : ViewModel() {

    private val _exerciseListFlow = MutableStateFlow<ExerciseListUiState>(Loading)
    val exerciseListFlow = _exerciseListFlow.asStateFlow()

    val fubar = "hehehe"

    fun onCreateExerciseButtonClicked() {
        Timber.tag("fubar").d("foo")
    }

    fun fetchExerciseList() {
        viewModelScope.launch {
            delay(2000)
            _exerciseListFlow.value = Success(emptyList())
        }
    }

}

sealed interface ExerciseListUiState {
    object Loading : ExerciseListUiState
    data class Success(val exerciseSettings: List<ExerciseSettings>) : ExerciseListUiState
}
