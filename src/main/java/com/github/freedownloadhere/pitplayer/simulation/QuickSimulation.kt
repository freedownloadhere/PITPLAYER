package com.github.freedownloadhere.pitplayer.simulation

import com.github.freedownloadhere.pitplayer.extensions.toRadians
import com.github.freedownloadhere.pitplayer.extensions.world
import net.minecraft.block.BlockAir
import net.minecraft.block.BlockIce
import net.minecraft.block.BlockSlime
import net.minecraft.util.BlockPos
import net.minecraft.util.MathHelper
import kotlin.math.max
import kotlin.math.pow

class QuickSimulation(image : PlayerImage) {
    companion object {
        const val BASE_ACCEL = 0.1
        const val BASE_AIR_ACCEL = 0.02
        const val DRAG_HORIZONTAL = 0.91
        const val DRAG_VERTICAL = 0.98
        const val GRAVITY = 0.08
        const val TERMINAL_VELO = -3.92
    }

    var prevImg = image
    var currImg = image

    fun slipperinessMult(pos : BlockPos) : Double {
        val block = world.getBlockState(pos).block
        return when(block) {
            is BlockAir -> 1.0
            is BlockIce -> 0.98
            is BlockSlime -> 0.8
            else -> 0.6
        }
    }

    fun movementMult() : Double {
        return 1.3 * 0.98
    }

    fun effects() : Double {
        return 1.0
    }

    fun simulate(sm : SimulatedMovement) {
        val sPrev = slipperinessMult(BlockPos(prevPos))
        val sCurr = slipperinessMult(BlockPos(pos))

        val momentumX = velX * sPrev * DRAG_HORIZONTAL
        val momentumZ = velZ * sPrev * DRAG_HORIZONTAL
        var dist = sm.left * sm.left + sm.forward * sm.forward
        if(dist < 1.0E-4f) return

        val friction = (if(grounded) BASE_ACCEL * (0.6 / sCurr).pow(3) else BASE_AIR_ACCEL).toFloat()

        dist = friction / max(1.0f, MathHelper.sqrt_float(dist))

        val maybeSinDist = sm.left * dist
        val maybeCosDist = sm.forward * dist
        val sinYaw = MathHelper.sin(yaw.toRadians())
        val cosYaw = MathHelper.cos(this.rotationYaw.toRadians())

        velX += (maybeSinDist * cosYaw - maybeCosDist * sinYaw).toDouble()
        velZ += (maybeCosDist * cosYaw + maybeSinDist * sinYaw).toDouble()
    }
}