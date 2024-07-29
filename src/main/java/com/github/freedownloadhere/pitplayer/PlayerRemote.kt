package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.ChatComponentText
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object PlayerRemote {
    fun message(s : String, prefix : String? = null) {
        var msg = ""
        if(prefix != null) msg += "[$prefix] "
        msg += s
        player.addChatComponentMessage(ChatComponentText(msg))
    }

    fun lookAt(pos : Vec3) {
        val distance = pos.subtract(player.headPosVector)
        val playerYaw = player.rotationYaw.cropAngle180()
        val posYaw = -atan2(distance.x, distance.z).toDegrees().toFloat().cropAngle180()
        val deltaYaw = (posYaw - playerYaw).cropAngle180()
        if(deltaYaw >= 90.0f) return
        val playerPitch = player.rotationPitch
        val posPitch = -atan2(distance.y, hypot(distance.x, distance.z)).toDegrees().toFloat()
        val deltaPitch = posPitch - playerPitch
        player.rotationYaw += deltaYaw
        player.rotationPitch += deltaPitch
    }
}