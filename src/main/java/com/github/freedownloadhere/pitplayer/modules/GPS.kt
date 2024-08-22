package com.github.freedownloadhere.pitplayer.modules

import com.github.freedownloadhere.pitplayer.utils.PlayerRemote
import com.github.freedownloadhere.pitplayer.event.EventFinishedPathing
import com.github.freedownloadhere.pitplayer.event.IObservable
import com.github.freedownloadhere.pitplayer.event.IObserver
import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.state.StateMachine
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue
import kotlin.math.floor

object GPS : IObservable {
    override val observers = arrayListOf<IObserver>(StateMachine)

    val wanderPoints = arrayListOf(Vec3i(0, 81, 0), Vec3i(10, 81, 10), Vec3i(-10, 81, -10))

    var route : MutableList<Vec3>? = null
    var dest : Vec3i? = null

    fun findWanderPoint() {
        val playerPos = player.blockBelow ?: return
        val queue = PriorityQueue {
            v1 : Vec3i, v2 : Vec3i -> Int
            floor(playerPos.distanceSq(v1) - playerPos.distanceSq(v2)).toInt()
        }
        while(queue.isNotEmpty()) {
            val pos = queue.remove()
            val r = Pathfinder.pathfind(pos, playerPos)
            if(r.isNullOrEmpty()) continue
            route = r
            break
        }
    }

    fun makeRoute() {
        route = Pathfinder.pathfind(dest, player.blockBelow)
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
}