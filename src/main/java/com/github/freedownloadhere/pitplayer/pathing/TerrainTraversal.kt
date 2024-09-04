package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.BotState
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerMovementHelper
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.rendering.RendererSmallNodeAdaptor
import net.minecraft.util.Vec3i
import java.awt.Color

object TerrainTraversal {
    fun makeRouteTo(pos : Vec3i) {
        BotState.dest = pos
        BotState.path = Pathfinder.pathfind(BotState.dest, player.blockBelow)
    }

    fun updateRouteTraversal() {
        PlayerControlHelper.reset()
        if(BotState.path.isNullOrEmpty()) { BotState.dest = null; return }

        val node = BotState.path!!.last()
        PlayerControlHelper.lookAtYaw(node.pos)
        PlayerControlHelper.lookAtPitch(15.0f)
        PlayerControlHelper.press(settings.keyBindForward)

        if(PlayerMovementHelper.shouldJump(node))
            PlayerControlHelper.press(settings.keyBindJump)

        val nodePos = node.pos.toBlockPos()
        val nodePosXZ = Vec3i(nodePos.x, 0, nodePos.z)
        val playerPos = player.positionVector.toBlockPos()
        val playerPosXZ = Vec3i(playerPos.x, 0, playerPos.z)
        if(nodePosXZ.matches(playerPosXZ))
            BotState.path!!.removeLast()
    }

    fun renderPath() {
        if(BotState.path.isNullOrEmpty()) return

        val lastNode = BotState.path!!.removeLast()
        RendererSmallNodeAdaptor.blocks(BotState.path!!, Color.GRAY)
        val color = if(lastNode.flags.read(Movement.Flags.Jump)) Color.CYAN else Color.GREEN
        Renderer.block(lastNode.pos, color)
        Renderer.line(player.partialPositionVector, lastNode.pos, color)
        BotState.path!!.add(lastNode)
    }
}