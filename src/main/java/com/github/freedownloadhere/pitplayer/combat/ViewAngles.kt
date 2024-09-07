package com.github.freedownloadhere.pitplayer.combat

class ViewAngles(val yaw : Float = 0.0f, val pitch : Float = 0.0f) {
    operator fun times(scalar : Float) : ViewAngles {
        return ViewAngles(yaw * scalar, pitch * scalar)
    }
}