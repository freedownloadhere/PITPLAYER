package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object PlayerRemote {
    val state : String
        get() = "\u00A7lRemote: \u00A7${if(isEnabled) "aYes" else "cNo"}"

    var isEnabled : Boolean = true
    var isWalking : Boolean = false
    var isJumping : Boolean = false

    fun toggle() {
        isEnabled = !isEnabled
        if(isEnabled) return
        mc.gameSettings.keyBindForward.release()
        isWalking = false
        mc.gameSettings.keyBindJump.release()
        isJumping = false
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
        mc.gameSettings.keyBindForward.hold()
        isWalking = true
    }

    fun stopWalking() {
        if(!isEnabled) return
        mc.gameSettings.keyBindForward.release()
        isWalking = false
    }

    fun jump() {
        if(!isEnabled) return
        mc.gameSettings.keyBindJump.hold()
        isJumping = true
    }

    fun stopJumping() {
        if(!isEnabled) return
        mc.gameSettings.keyBindJump.release()
        isJumping = false
    }

    fun attack() {
        if(!isEnabled) return
        mc.gameSettings.keyBindAttack.press()
    }
}