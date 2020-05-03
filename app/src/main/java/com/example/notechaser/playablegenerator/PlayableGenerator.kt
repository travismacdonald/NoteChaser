package com.example.notechaser.playablegenerator

/**
 * Base generator class.
 */
interface PlayableGenerator {

    fun generatePlayable(): Playable

    fun setupGenerator()
}