package com.cannonballapps.notechaser.playablegenerator


interface  PlayableGenerator {

    fun generatePlayable(): Playable

    fun setupGenerator()
}