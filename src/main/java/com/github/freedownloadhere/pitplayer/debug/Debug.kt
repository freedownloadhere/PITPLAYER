package com.github.freedownloadhere.pitplayer.debug

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.GPS
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import com.github.freedownloadhere.pitplayer.pathing.moveset.NeighbourCones
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.pathing.utils.Bresenham
import com.github.freedownloadhere.pitplayer.rendering.RendererSmallNodeAdaptor
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement
import com.github.freedownloadhere.pitplayer.simulation.SimulatedPlayer
import com.mojang.authlib.GameProfile
import net.minecraft.util.ChatComponentText
import net.minecraft.util.Vec3i
import java.awt.Color
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

    enum class Option(var enabled : Boolean) {
        ShowBresenham(true),
        ShowPath(true),
        ShowClosestValid(true),
        ShowMotionVec(false),
        ShowNextBlock(false);

        companion object {
            val map = run {
                val temp = hashMapOf<String, Option>()
                for(option in Option.entries)
                    temp[option.name] = option
                temp
            }
        }
    }

    private var bresenhamLine : List<Vec3i>? = null
    private var path : List<Pathfinder.SmallNode>? = null
    private var closestValid : Movement? = null
    private var closestValidRelativeTo : Vec3i? = null
    private var currentCone : NeighbourCones? = null
    private var fakePlayer : SimulatedPlayer? = null

    fun setOption(name : String, value : Boolean) {
        val option = Option.map[name]
        if(option == null) {
            Logger.regular("The option \u00A7c$name does not exist")
            return
        }
        option.enabled = value
        Logger.regular("Set option \u00A7c$name \u00A7fto be \u00A73$value")
    }

    fun computeBresenham(pos1 : Vec3i, pos2 : Vec3i) {
        bresenhamLine = Bresenham.xz(pos1, pos2)
        Logger.regular("Computed Bresenham for points \u00A73$pos1 and \u00A73$pos2")
    }

    fun computePath(pos1 : Vec3i, pos2 : Vec3i) {
        path = Pathfinder.pathfind(pos2, pos1)
        Logger.regular("Computed path for points \u00A73$pos1 and \u00A73$pos2")
    }

    fun computeClosestValid(pos : Vec3i, cone : NeighbourCones) {
        closestValid = Pathfinder.findClosestValidPos(pos, cone, setOf())
        closestValidRelativeTo = pos
        currentCone = cone
        Logger.regular("Computed closest valid move for \u00A73$pos and cone \u00A73${cone.name}")
        Logger.regular("The movement found is: \u00A73$closestValid")
    }

    fun createRoute(pos : Vec3i) {
        GPS.makeRouteTo(pos)
        if(GPS.isPathEmptyOrNull)
            Logger.alert("The path is empty or null")
        else
            Logger.regular("Created a route to \u00A73$pos")
    }

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

    fun renderBresenham() {
        if(!Option.ShowBresenham.enabled || bresenhamLine == null) return
        Renderer.blocksVec3i(bresenhamLine!!, Color.BLUE)
    }

    fun renderPath() {
        if(!Option.ShowPath.enabled || path == null) return
        RendererSmallNodeAdaptor.blocks(path!!, Color.GREEN)
    }

    fun renderClosestValid() {
        if(!Option.ShowClosestValid.enabled || closestValid == null) return
        currentCone!!.drawCone(closestValidRelativeTo!!)
        Renderer.blockVec3i(closestValidRelativeTo!! + closestValid!!.dir, Color.ORANGE)
    }

    fun renderMotionVec() {
        if(!Option.ShowMotionVec.enabled) return
        Renderer.vectorFromPlayer(player.motionVectorXZ, Color.RED)
    }

    fun renderNextBlock() {
        // if(!Option.ShowNextBlock.enabled) return
        // val pos1 = player.positionVector
        // val pos2 = player.nextPosition
        // val color = if(pos1.toBlockPos().matches(pos2.toBlockPos())) Color.BLUE else Color.RED
        // Renderer.block(pos2, color)
        // println("Pos1: \u00A73$pos1 | Pos2: \u00A73$pos2")
        // println("Pos1Block: \u00A73${pos1.toBlockPos()} | Pos2: \u00A73${pos2.toBlockPos()}")
    }
}