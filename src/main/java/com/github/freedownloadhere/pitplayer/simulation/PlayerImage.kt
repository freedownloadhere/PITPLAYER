package com.github.freedownloadhere.pitplayer.simulation

import com.github.freedownloadhere.pitplayer.utils.Vector3d

data class PlayerImage(
    val pos : Vector3d,
    val rotationYaw : Float,
    val rotationPitch : Float,
    val motionX : Double,
    val motionY : Double,
    val motionZ : Double,
    val jumpTicks : Int,
    val jumpMovementFactor : Float,
    val walkSpeed : Float,
)