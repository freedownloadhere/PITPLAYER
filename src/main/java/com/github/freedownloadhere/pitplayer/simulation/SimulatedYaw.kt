package com.github.freedownloadhere.pitplayer.simulation

import net.minecraft.util.MathHelper

class SimulatedYaw(y : Float, val cost : Double) {
    val yaw = MathHelper.clamp_float(y, -180.0f, 180.0f)
}