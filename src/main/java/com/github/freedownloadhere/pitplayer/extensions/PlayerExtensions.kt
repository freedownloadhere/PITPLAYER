package com.github.freedownloadhere.pitplayer.extensions

import com.github.freedownloadhere.pitplayer.mixin.AccessorMinecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.Vec3

val EntityPlayerSP.headPositionVector : Vec3
    get() = positionVector.addVector(0.0, 1.625, 0.0)

val EntityPlayerSP.motionVectorXZ : Vec3
    get() = Vec3(motionX, 0.0, motionZ) * (0.05 / 0.027)

val EntityPlayerSP.nextPosition : Vec3
    get() = positionVector + player.motionVectorXZ

val EntityPlayerSP.partialPositionVector : Vec3
    get() {
        val partialTicks = (mc as AccessorMinecraft).timer_pitplayer.renderPartialTicks.toDouble()
        return Vec3(
            prevPosX + (posX - prevPosX) * partialTicks,
            prevPosY + (posY - prevPosY) * partialTicks,
            prevPosZ + (posZ - prevPosZ) * partialTicks
        )
    }