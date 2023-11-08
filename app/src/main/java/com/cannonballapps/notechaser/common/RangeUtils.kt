package com.cannonballapps.notechaser.common

import android.util.Range
import kotlin.math.roundToInt

fun Range<Int>.toIntRange(): IntRange =
    IntRange(start = this.lower, endInclusive = this.upper)

fun IntRange.toRangeInt(): Range<Int> =
    Range(start, endInclusive)

fun IntRange.toFloatRange(): ClosedFloatingPointRange<Float> =
    start.toFloat()..endInclusive.toFloat()

fun ClosedFloatingPointRange<Float>.toIntRange(): IntRange =
    start.roundToInt()..endInclusive.roundToInt()

val IntRange.size: Int
    get() = last - start
