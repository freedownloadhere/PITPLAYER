package com.github.freedownloadhere.pitplayer.debug

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement
import com.github.freedownloadhere.pitplayer.simulation.SimulatedPlayer
import com.mojang.authlib.GameProfile
import net.minecraft.util.ChatComponentText
import java.util.*

object Debug {
    object Logger {
        fun regular(message : String) {
            player.addChatComponentMessage(ChatComponentText("\u00A7f\u00A7l[Debug]\u00A7r $message"))
        }

        fun alert(message : String) {
            player.addChatComponentMessage(ChatComponentText("\u00A7e\u00A7l[Debug]\u00A7r $message"))
        }
    }

    private var fakePlayer : SimulatedPlayer? = null

    fun simulateNewPlayer() {
        val fakeGameProfile = GameProfile(UUID.randomUUID(), "Bot")
        fakePlayer = SimulatedPlayer(world, fakeGameProfile)
        world.spawnEntityInWorld(fakePlayer)
        fakePlayer!!.setPosition(0.0, 82.0, 0.0)
        Logger.regular("Added player in world.")
    }

    fun simulateMove(s : SimulatedMovement) {
        fakePlayer?.simulate(s)
    }
}