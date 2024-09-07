package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.interfaces.Killable
import com.github.freedownloadhere.pitplayer.utils.RandomHelper
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.min

object AimHelper : Killable("Aim Helper") {
    private var aimTime = 100L
    private var currTime = 0L
    private var shouldUpdate = false

    private var maxError = 0.2f
    private var delta = ViewAngles()

    fun update(deltaTime : Long) {
        if(killed) return
        if(!shouldUpdate) return

        currTime = min(aimTime, currTime + deltaTime)

        if(currTime == aimTime)
            shouldUpdate = false

        if(aimTime == 0L)
            instantAim()
        else
            gradualAim(deltaTime)
    }

    fun lookAt(pos : Vec3) : AimHelper {
        if(killed) return this

        val error = RandomHelper.fromRange(1.0f - maxError .. 1.0f + maxError)
        delta = calcDelta(pos) * error

        currTime = 0L
        shouldUpdate = true

        return this
    }

    fun setVA(yaw : Float?, pitch : Float?) : AimHelper {
        if(killed) return this

        if(yaw != null) player.rotationYaw = yaw
        if(pitch != null) player.rotationPitch = pitch

        return this
    }

    fun pitch(pitch : Float?) : AimHelper {
        if(killed) return this

        val deltaPitch = if(pitch == null) 0.0f else pitch - player.rotationPitch
        delta = ViewAngles(delta.yaw, deltaPitch)

        return this
    }

    private fun instantAim() {
        player.rotationYaw += delta.yaw
        player.rotationPitch += delta.pitch
    }

    private fun gradualAim(deltaTime : Long) {
        val alpha = deltaTime.toFloat() / aimTime.toFloat()
        player.rotationYaw += (delta.yaw * alpha)
        player.rotationPitch += (delta.pitch * alpha)
    }

    private fun calcDelta(pos : Vec3) : ViewAngles {
        val deltaYaw = calcDeltaYaw(pos)
        val deltaPitch = calcDeltaPitch(pos)
        return ViewAngles(deltaYaw, deltaPitch)
    }

    private fun calcDeltaYaw(pos : Vec3) : Float {
        val distance = pos.subtract(player.headPositionVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        return deltaYaw
    }

    private fun calcDeltaPitch(pos : Vec3) : Float {
        val distance = pos.subtract(player.headPositionVector)
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        return deltaPitch
    }
}