package com.example.notechaser.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notechaser.data.ExerciseType
import com.example.notechaser.data.exercisesetup.ExerciseSetupItem
import com.example.notechaser.data.exercisesetup.ExerciseSetupSettings
import com.example.notechaser.playablegenerator.PlayableGenerator
import com.example.notechaser.playablegenerator.TemplatePlayableGenerator


class ExerciseSetupViewModel internal constructor(
        val exerciseType: ExerciseType
): ViewModel() {

    // TODO: Playable Generator
    val settings = ExerciseSetupSettings()

    val settingsItemList = MutableLiveData<ExerciseSetupItem>()

    lateinit var generator: PlayableGenerator



}