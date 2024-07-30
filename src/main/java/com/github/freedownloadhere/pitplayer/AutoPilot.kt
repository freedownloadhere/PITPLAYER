package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.ChatComponentText
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object AutoPilot {
    var isEnabled : Boolean = true
    var isWalking : Boolean = false
    var isJumping : Boolean = false

    fun toggle() {
        isEnabled = !isEnabled
        if(isEnabled) return
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, false)
        isWalking = false
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false)
        isJumping = false
    }

    fun message(s : String, prefix : String? = null) {
        var msg = ""
        if(prefix != null) msg += "[$prefix] "
        msg += s
        player.addChatComponentMessage(ChatComponentText(msg))
    }

    fun lookForward() {
        if(!isEnabled) return
        player.rotationPitch = 0.0f
    }

    fun lookAtYaw(pos : Vec3) {
        if(!isEnabled) return
        val distance = pos.subtract(player.headPosVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        if(deltaYaw >= 90.0f) return
        player.rotationYaw += deltaYaw
    }

    fun lookAtPitch(pos : Vec3) {
        if(!isEnabled) return
        val distance = pos.subtract(player.headPosVector)
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        player.rotationPitch += deltaPitch
    }

    fun lookAt(pos : Vec3) {
        if(!isEnabled) return
        lookAtYaw(pos)
        lookAtPitch(pos)
    }

    fun walkForward() {
        if(!isEnabled) return
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, true)
        isWalking = true
    }

    fun stopWalking() {
        if(!isEnabled) return
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.keyCode, false)
        isWalking = false
    }

    fun jump() {
        if(!isEnabled) return
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, true)
        isJumping = true
    }

    fun stopJumping() {
        if(!isEnabled) return
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false)
        isJumping = false
    }
}