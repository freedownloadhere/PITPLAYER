package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object AimAssist : Toggleable("AimAssist", true) {
    private var currentlyAimingYaw = false
    private var currentlyAimingPitch = false

    fun lookAt(entity : Entity, lookTicks: Long = 0L) {
        lookAt(entity.eyePosition, lookTicks)
    }

    fun lookAt(pos : Vec3, lookTicks: Long = 0L) {
        lookAtYaw(pos, lookTicks)
        lookAtPitch(pos, lookTicks)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun lookAtYaw(pos : Vec3, lookTicks : Long = 0L) {
        if(!toggled) return
        val deltaYaw = getLookYaw(pos)

        if(lookTicks == 0L) {
            player.rotationYaw += deltaYaw
            return
        }

        else if(!currentlyAimingYaw) GlobalScope.launch {
            applyYawGradually(deltaYaw, lookTicks)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun lookAtPitch(pos : Vec3, lookTicks: Long = 0L) {
        if(!toggled) return
        val deltaPitch = getLookPitch(pos)

        if(lookTicks == 0L) {
            player.rotationPitch += deltaPitch
            return
        }

        else if(!currentlyAimingPitch) GlobalScope.launch {
            applyPitchGradually(deltaPitch, lookTicks)
        }
    }

    fun changePitch(pitch : Float) {
        if(!toggled) return;
        player.rotationPitch = pitch;
    }

    private fun getLookYaw(pos : Vec3) : Float {
        val distance = pos.subtract(player.headPositionVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        return deltaYaw
    }

    private fun getLookPitch(pos : Vec3) : Float {
        val distance = pos.subtract(player.headPositionVector)
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        return deltaPitch
    }

    private suspend fun applyYawGradually(deltaYaw : Float, lookTicks: Long) {
        currentlyAimingYaw = true
        val increment = deltaYaw / lookTicks
        for(i in 1L..lookTicks) {
            player.rotationYaw += increment
            delay(50L)
        }
        currentlyAimingYaw = false
    }

    private suspend fun applyPitchGradually(deltaPitch : Float, lookTicks: Long) {
        currentlyAimingPitch = true
        val increment = deltaPitch / lookTicks
        for(i in 1L..lookTicks) {
            player.rotationPitch += increment
            delay(50L)
        }
        currentlyAimingPitch = false
    }
}