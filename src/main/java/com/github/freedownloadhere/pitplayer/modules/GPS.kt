package com.github.freedownloadhere.pitplayer.modules

import com.github.freedownloadhere.pitplayer.utils.PlayerHelper
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.awt.Color

@OptIn(DelicateCoroutinesApi::class)
object GPS {
    private var route : MutableList<Vec3>? = null
    private var dest : Vec3i? = null

    fun makeRouteTo(pos : Vec3i) {
        GlobalScope.launch {
            dest = pos
            route = Pathfinder.pathfind(dest, player.blockBelow)
        }
    }

    fun updateRouteTraversal() {
        PlayerHelper.reset()

        if(route == null) { dest = null; return }
        while(route!!.isNotEmpty() && player.positionVector.squareDistanceToXZ(route!!.last()) <= 0.5)
            route!!.removeLast()
        if(route!!.isEmpty()) { dest = null; return }

        val target = route!!.last()
        PlayerHelper.lookAtYaw(target)
        PlayerHelper.lookForward()
        PlayerHelper.press(settings.keyBindForward)

        if(target.y > player.positionVector.y)
            PlayerHelper.press(settings.keyBindJump)
    }

    fun renderPath() {
        if(route.isNullOrEmpty()) return
        val lastBlock = route!!.removeLast()
        Renderer.blocks(route!!, Color.GRAY)
        Renderer.block(lastBlock, Color.GREEN)
        Renderer.line(player.partialPos, lastBlock, Color.GREEN)
        route!!.add(lastBlock)
    }
}