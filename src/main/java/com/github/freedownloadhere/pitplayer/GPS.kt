package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

object GPS {
    var route : MutableList<Vec3>? = null

    fun makeRoute(dest : Vec3i?) {
        route = Pathfinder.pathfind(dest, player.blockBelow)
    }

    fun traverseRoute() {
        if(route == null) return
        while(route!!.isNotEmpty() && player.positionVector.squareDistanceToXZ(route!!.last()) <= 0.5)
            route!!.removeLast()
        if(PlayerRemote.isWalking) PlayerRemote.stopWalking()
        if(PlayerRemote.isJumping) PlayerRemote.stopJumping()
        if(route!!.isEmpty()) return
        val target = route!!.last()
        PlayerRemote.lookAtYaw(target)
        PlayerRemote.lookForward()
        PlayerRemote.walkForward()
        if(target.y > player.positionVector.y)
            PlayerRemote.jump()
    }
}