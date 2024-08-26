package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object PlayerHelper {
    var enabled : Boolean = true
        private set
    val state : String
        get() = "\u00A7lRemote: \u00A7${if(enabled) "aYes" else "cNo"}"

    private enum class Key(private val key : KeyBinding, private var active : Boolean = false) {
        Forward(mc.gameSettings.keyBindForward),
        Jump(mc.gameSettings.keyBindJump),
        Attack(mc.gameSettings.keyBindAttack);
        fun reset() { if(active) release() }
        fun hold() { key.hold(); active = true }
        fun press() { key.press() }
        fun release() { key.release(); active = false }
    }

    fun toggle() {
        enabled = !enabled
        reset()
    }

    fun reset() {
        for(k in Key.entries) k.reset()
    }

    fun lookForward() {
        if(!enabled) return
        player.rotationPitch = 0.0f
    }

    fun lookAtYaw(pos : Vec3) {
        if(!enabled) return
        val distance = pos.subtract(player.headPosVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        player.rotationYaw += deltaYaw
    }

    fun lookAtPitch(pos : Vec3) {
        if(!enabled) return
        val distance = pos.subtract(player.headPosVector)
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        player.rotationPitch += deltaPitch
    }

    fun lookAt(pos : Vec3) {
        if(!enabled) return
        lookAtYaw(pos)
        lookAtPitch(pos)
    }

    fun walkForward() {
        if(!enabled) return
        Key.Forward.hold()
    }

    fun stopWalking() {
        if(!enabled) return
        Key.Forward.reset()
    }

    fun jump() {
        if(!enabled) return
        Key.Jump.hold()
    }

    fun stopJumping() {
        if(!enabled) return
        Key.Jump.reset()
    }

    fun attack() {
        if(!enabled) return
        Key.Attack.press()
    }
}