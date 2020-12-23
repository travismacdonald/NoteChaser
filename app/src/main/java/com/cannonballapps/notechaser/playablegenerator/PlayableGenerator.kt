package com.cannonballapps.notechaser.playablegenerator

/**
 * Base generator class.
 */
interface PlayableGenerator {

    fun generatePlayable(): Playable

    fun setupGenerator()
}