package com.github.freedownloadhere.pitplayer

import kotlin.math.PI

fun Float.cropAngle180() : Float {
    var angle = this
    while(angle < -180.0f) angle += 360.0f
    while(angle >= 180.0f) angle -= 360.0f
    return angle
}

fun Double.toDegrees() : Double {
    return this * 180.0 / PI
}