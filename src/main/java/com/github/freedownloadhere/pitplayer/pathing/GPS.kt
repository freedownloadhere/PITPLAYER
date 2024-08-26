package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerMovementHelper
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.rendering.RendererSmallNodeAdaptor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.util.Vec3i
import java.awt.Color

@OptIn(DelicateCoroutinesApi::class)
object GPS {
    private var route : MutableList<Pathfinder.SmallNode>? = null
    private var dest : Vec3i? = null

    fun makeRouteTo(pos : Vec3i) {
        GlobalScope.launch {
            dest = pos
            route = Pathfinder.pathfind(dest, player.blockBelow)
        }
    }

    fun updateRouteTraversal() {
        PlayerControlHelper.reset()

        if(route == null) { dest = null; return }
        while(route!!.isNotEmpty() && player.positionVector.squareDistanceToXZ(route!!.last().pos) <= 0.5)
            route!!.removeLast()
        if(route!!.isEmpty()) { dest = null; return }

        val node = route!!.last()
        PlayerControlHelper.lookAtYaw(node.pos)
        PlayerControlHelper.lookForward()
        PlayerControlHelper.press(settings.keyBindForward)

        if(PlayerMovementHelper.shouldJump(node))
            PlayerControlHelper.press(settings.keyBindJump)
    }

    fun renderPath() {
        if(route.isNullOrEmpty()) return

        val lastNode = route!!.removeLast()
        RendererSmallNodeAdaptor.blocks(route!!, Color.GRAY)
        val color = if(lastNode.flags.read(Movement.Flags.Jump)) Color.CYAN else Color.GREEN
        Renderer.block(lastNode.pos, color)
        Renderer.line(player.partialPositionVector, lastNode.pos, color)
        route!!.add(lastNode)
    }
}