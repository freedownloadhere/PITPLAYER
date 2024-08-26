package com.github.freedownloadhere.pitplayer.debug

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.GPS
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import com.github.freedownloadhere.pitplayer.pathing.moveset.NeighbourCones
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.pathing.utils.Bresenham
import com.github.freedownloadhere.pitplayer.rendering.RendererSmallNodeAdaptor
import net.minecraft.util.ChatComponentText
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.awt.Color

object Debug {
    object Logger {
        enum class Level(val v : Char) {
            Normal('f'),
            Alert('e')
        }

        fun send(message : String, level : Level = Level.Normal) {
            player.addChatComponentMessage(ChatComponentText("\u00A7${level.v}\u00A7l[Debug]\u00A7r $message"))
        }
    }

    var showBresenham = true
    var showPath = true
    var showClosestValid = true
    var showMotionVec = false
    var showNextBlock = false

    var bresenhamLine : List<Vec3i>? = null
    var path : List<Pathfinder.SmallNode>? = null
    var closestValid : Movement? = null
    var closestValidRelativeTo : Vec3i? = null
    var currentCone : NeighbourCones? = null

    fun computeBresenham(pos1 : Vec3i, pos2 : Vec3i) {
        bresenhamLine = Bresenham.xz(pos1, pos2)
        Logger.send("Computed Bresenham for points \u00A73$pos1 and \u00A73$pos2")
    }

    fun computePath(pos1 : Vec3i, pos2 : Vec3i) {
        path = Pathfinder.pathfind(pos2, pos1)
        Logger.send("Computed path for points \u00A73$pos1 and \u00A73$pos2")
    }

    fun computeClosestValid(pos : Vec3i, cone : NeighbourCones) {
        closestValid = Pathfinder.findClosestValidPos(pos, cone, setOf())
        closestValidRelativeTo = pos
        currentCone = cone
        Logger.send("Computed closest valid move for \u00A73$pos and cone \u00A73${cone.name}")
        Logger.send("The movement found is: \u00A73$closestValid")
    }

    fun createRoute(pos : Vec3i) {
        GPS.makeRouteTo(pos)
        Logger.send("Created a route to \u00A73$pos")
    }

    fun renderBresenham() {
        if(!showBresenham || bresenhamLine == null) return
        Renderer.blocksVec3i(bresenhamLine!!, Color.BLUE)
    }

    fun renderPath() {
        if(!showPath || path == null) return
        RendererSmallNodeAdaptor.blocks(path!!, Color.GREEN)
    }

    fun renderClosestValid() {
        if(!showClosestValid || closestValid == null) return
        currentCone!!.drawCone(closestValidRelativeTo!!)
        Renderer.blockVec3i(closestValidRelativeTo!! + closestValid!!.dir, Color.ORANGE)
    }

    fun renderMotionVec() {
        if(!showMotionVec) return
        Renderer.vectorFromPlayer(player.motionVectorXZ, Color.RED)
    }

    fun renderNextBlock() {
        if(!showNextBlock) return
        val pos1 = player.positionVector
        val pos2 = player.nextPosition
        val color = if(pos1.toBlockPos().matches(pos2.toBlockPos())) Color.BLUE else Color.RED
        Renderer.block(pos2, color)
    }
}