package com.github.freedownloadhere.pitplayer.utils

import kotlin.random.Random

object RandomHelper {
    fun fromRange(range : ClosedFloatingPointRange<Float>) : Float {
        return range.start + (range.endInclusive - range.start) * Random.nextFloat()
    }
}