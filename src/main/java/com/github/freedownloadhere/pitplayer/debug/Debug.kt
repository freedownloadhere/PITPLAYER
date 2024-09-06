package com.github.freedownloadhere.pitplayer.debug

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.rendering.RendererNodeAdaptor
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement
import com.github.freedownloadhere.pitplayer.simulation.SimulatedPlayer
import com.github.freedownloadhere.pitplayer.utils.Vector3d
import com.mojang.authlib.GameProfile
import net.minecraft.util.ChatComponentText
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import java.awt.Color
import java.util.*

object Debug {
    object Logger {
        fun regular(message : String) {
            player.addChatComponentMessage(ChatComponentText("\u00A7f\u00A7l[PITPLAYER]\u00A7r $message"))
        }

        fun alert(message : String) {
            player.addChatComponentMessage(ChatComponentText("\u00A7e\u00A7l[PITPLAYER]\u00A7r $message"))
        }
    }

    private var fakePlayer : SimulatedPlayer? = null
    private var path : MutableList<Pathfinder.Node>? = null

    fun simulateNewPlayer() {
        fakePlayer = SimulatedPlayer()
        world.spawnEntityInWorld(fakePlayer)
        fakePlayer!!.setPosition(0.0, 82.0, 0.0)
        Logger.regular("Added player in world.")
    }

    fun simulateMove(arg1 : String, arg2 : String, arg3 : String) {
        if(fakePlayer == null) return
        val forward = arg1.toFloat().coerceIn(-1.0f .. 1.0f)
        val right = arg2.toFloat().coerceIn(-1.0f .. 1.0f)
        val jump = arg3.toBoolean()
        val sm = SimulatedMovement(forward, right, jump)
        fakePlayer!!.simulate(sm)
    }

    fun createPath(arg1 : String, arg2 : String, arg3 : String) {
        val x = arg1.toDouble()
        val y = arg2.toDouble()
        val z = arg3.toDouble()
        val vec = Vector3d(x, y, z)
        path = Pathfinder.pathfind(vec)
        if(path == null) {
            Logger.alert("The path is null!")
            return
        }

        Logger.regular("Created a path with ${path!!.size} nodes.")
    }

    fun renderPath() {
        if(path == null) return
        RendererNodeAdaptor.lines(path!!, Color.GREEN)
    }
}