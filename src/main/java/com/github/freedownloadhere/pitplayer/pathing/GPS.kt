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

object GPS {
    private var route : MutableList<Pathfinder.SmallNode>? = null
    private var dest : Vec3i? = null

    val isPathEmptyOrNull : Boolean
        get() = route.isNullOrEmpty()

    fun makeRouteTo(pos : Vec3i) {
        dest = pos
        route = Pathfinder.pathfind(dest, player.blockBelow)
    }

    fun updateRouteTraversal() {
        PlayerControlHelper.reset()
        if(route.isNullOrEmpty()) { dest = null; return }

        val node = route!!.last()
        PlayerControlHelper.lookAtYaw(node.pos)
        PlayerControlHelper.lookForward()
        PlayerControlHelper.press(settings.keyBindForward)

        if(PlayerMovementHelper.shouldJump(node))
            PlayerControlHelper.press(settings.keyBindJump)

        val nodePos = node.pos.toBlockPos()
        val nodePosXZ = Vec3i(nodePos.x, 0, nodePos.z)
        val playerPos = player.positionVector.toBlockPos()
        val playerPosXZ = Vec3i(playerPos.x, 0, playerPos.z)
        if(nodePosXZ.matches(playerPosXZ))
            route!!.removeLast()
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