package com.cannonballapps.notechaser.musicutilities

val ParentScale.Major.Ionian: Scale
    get() = ParentScale.Major.scaleAtIndex(0)

val ParentScale.Major.Dorian: Scale
    get() = ParentScale.Major.scaleAtIndex(1)

val ParentScale.Major.Phrygian: Scale
    get() = ParentScale.Major.scaleAtIndex(2)

val ParentScale.Major.Lydian: Scale
    get() = ParentScale.Major.scaleAtIndex(3)

val ParentScale.Major.Mixolydian: Scale
    get() = ParentScale.Major.scaleAtIndex(4)

val ParentScale.Major.Aoelian: Scale
    get() = ParentScale.Major.scaleAtIndex(5)

val ParentScale.Major.Locrian: Scale
    get() = ParentScale.Major.scaleAtIndex(6)
