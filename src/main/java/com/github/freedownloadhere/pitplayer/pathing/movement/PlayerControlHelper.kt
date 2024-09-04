package com.github.freedownloadhere.pitplayer.pathing.movement

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.mixin.AccessorKeyBinding
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object PlayerControlHelper {
    private var enabled : Boolean = true
    val state : String
        get() = "\u00A7lRemote: \u00A7${if(enabled) "aYes" else "cNo"}"
    val ingame : Boolean
        get() = mc.thePlayer != null && mc.theWorld != null
    private val affectedKeys = mutableSetOf<AccessorKeyBinding>()


    fun toggle() {
        enabled = !enabled
        if(!enabled) reset()
    }

    fun reset() {
        for(key in affectedKeys) {
            key.pressed_pitplayer = false
            key.pressTime_pitplayer = 0
        }
        affectedKeys.clear()
    }

    fun press(key : KeyBinding) {
        if(!enabled) return
        val accessor = key as AccessorKeyBinding
        affectedKeys.add(accessor)
        accessor.pressed_pitplayer = true
        accessor.pressTime_pitplayer = 1
    }

    fun release(key : KeyBinding) {
        if(!enabled) return
        val accessor = key as AccessorKeyBinding
        affectedKeys.add(accessor)
        accessor.pressed_pitplayer = false
        accessor.pressTime_pitplayer = 0
    }

    fun lookAt(entity : EntityLiving) {
        val pos = entity.eyePosition
        lookAtYaw(pos)
        lookAtPitch(pos)
    }

    fun lookAtYaw(pos : Vec3) {
        if(!enabled) return
        val distance = pos.subtract(player.headPositionVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        player.rotationYaw += deltaYaw
    }

    fun lookAtPitch(pos : Vec3) {
        if(!enabled) return
        val distance = pos.subtract(player.headPositionVector)
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        player.rotationPitch += deltaPitch
    }

    fun lookAtPitch(pitch : Float) {
        if(!enabled) return;
        player.rotationPitch = pitch;
    }
}