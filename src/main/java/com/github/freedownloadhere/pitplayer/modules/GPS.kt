package com.github.freedownloadhere.pitplayer.modules

import com.github.freedownloadhere.pitplayer.utils.PlayerRemote
import com.github.freedownloadhere.pitplayer.event.EventFinishedPathing
import com.github.freedownloadhere.pitplayer.event.IObservable
import com.github.freedownloadhere.pitplayer.event.IObserver
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.state.StateMachine
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.awt.Color
import java.util.PriorityQueue
import kotlin.math.floor

@OptIn(DelicateCoroutinesApi::class)
object GPS : IObservable {
    override val observers = arrayListOf<IObserver>(StateMachine)

    var route : MutableList<Vec3>? = null
    var dest : Vec3i? = null

    fun makeRoute() {
        GlobalScope.launch {
            route = Pathfinder.pathfind(dest, player.blockBelow)
        }
    }

    fun traverseRoute() {
        PlayerRemote.reset()

        if(route == null) { dest = null; dispatch(EventFinishedPathing()); return }
        while(route!!.isNotEmpty() && player.positionVector.squareDistanceToXZ(route!!.last()) <= 0.5)
            route!!.removeLast()
        if(route!!.isEmpty()) { dest = null; dispatch(EventFinishedPathing()); return }

        val target = route!!.last()
        PlayerRemote.lookAtYaw(target)
        PlayerRemote.lookForward()
        PlayerRemote.walkForward()
        if(target.y > player.positionVector.y)
            PlayerRemote.jump()
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