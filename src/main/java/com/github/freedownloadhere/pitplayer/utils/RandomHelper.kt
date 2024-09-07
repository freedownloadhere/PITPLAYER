package com.github.freedownloadhere.pitplayer.utils

import kotlin.random.Random

object RandomHelper {
    fun fromRange(range : ClosedFloatingPointRange<Float>) : Float {
        return range.start + (range.endInclusive - range.start) * Random.nextFloat()
    }

    fun fromRange(range : LongRange) : Long {
        return range.first + (range.last - range.first) * Random.nextLong()
    }
}