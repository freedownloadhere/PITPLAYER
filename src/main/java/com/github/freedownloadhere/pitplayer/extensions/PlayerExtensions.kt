package com.github.freedownloadhere.pitplayer.extensions

import com.github.freedownloadhere.pitplayer.mixin.AccessorMinecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.Vec3

val EntityPlayerSP.headPositionVector : Vec3
    get() = positionVector.addVector(0.0, 1.625, 0.0)

val EntityPlayerSP.partialPositionVector : Vec3
    get() {
        val partialTicks = (mc as AccessorMinecraft).timer_pitplayer.renderPartialTicks.toDouble()
        return Vec3(
            prevPosX + (posX - prevPosX) * partialTicks,
            prevPosY + (posY - prevPosY) * partialTicks,
            prevPosZ + (posZ - prevPosZ) * partialTicks
        )
    }
