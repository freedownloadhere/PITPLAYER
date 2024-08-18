package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.Timer
import net.minecraft.util.Vec3

val EntityPlayerSP.headPosVector : Vec3
    get() = positionVector.addVector(0.0, 1.625, 0.0)

val EntityPlayerSP.partialPos : Vec3
    get() {
        // TODO expect issues in the future
        val timer = Minecraft::class.java.getDeclaredField("timer")
        timer.isAccessible = true
        val timerValue = timer.get(mc) as Timer
        val partialTicks = timerValue.renderPartialTicks.toDouble()
        return Vec3(
            prevPosX + (posX - prevPosX) * partialTicks,
            prevPosY + (posY - prevPosY) * partialTicks,
            prevPosZ + (posZ - prevPosZ) * partialTicks
        )
    }