package com.cannonballapps.notechaser.models.playablegenerator

import com.cannonballapps.notechaser.playablegenerator.Playable

interface PlayableGenerator {

    fun generatePlayable(): Playable

}