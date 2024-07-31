package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.event.EventFinishedPathing
import com.github.freedownloadhere.pitplayer.event.IObservable
import com.github.freedownloadhere.pitplayer.event.IObserver
import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

object GPS : IObservable {
    override val observers = arrayListOf<IObserver>(StateMachine)

    var route : MutableList<Vec3>? = null
    var dest : Vec3i? = null

    fun traverseRoute() {
        route = Pathfinder.pathfind(dest, player.blockBelow)
        if(route == null) { dispatch(EventFinishedPathing()); return }
        while(route!!.isNotEmpty() && player.positionVector.squareDistanceToXZ(route!!.last()) <= 0.5)
            route!!.removeLast()
        if(PlayerRemote.isWalking) PlayerRemote.stopWalking()
        if(PlayerRemote.isJumping) PlayerRemote.stopJumping()
        if(route!!.isEmpty()) { dispatch(EventFinishedPathing()); return }
        val target = route!!.last()
        PlayerRemote.lookAtYaw(target)
        PlayerRemote.lookForward()
        PlayerRemote.walkForward()
        if(target.y > player.positionVector.y)
            PlayerRemote.jump()
    }
}