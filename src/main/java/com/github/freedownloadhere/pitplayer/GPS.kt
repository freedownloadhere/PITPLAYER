package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue

object GPS {
    var route = mutableListOf<Vec3>()

    fun makeRoute(dest : Vec3i) {
        val r = Pathfinder.pathfind(dest, player.blockBelow ?: return) ?: return
        if(r.isEmpty()) return
        route = r
    }

    fun traverseRoute() {
        while(route.isNotEmpty() && player.positionVector.squareDistanceToXZ(route.last()) <= 1)
            route.removeLast()
        if(AutoPilot.isJumping) AutoPilot.stopJumping()
        if(AutoPilot.isWalking) AutoPilot.stopWalking()
        if(route.isEmpty()) return
        val target = route.last()
        AutoPilot.lookAtYaw(target)
        AutoPilot.lookForward()
        AutoPilot.walkForward()
        if(target.y > player.positionVector.y)
            AutoPilot.jump()
    }

    fun pathfindToNearestEntity() {
        val pos = player.positionVector
        val entityHeap = PriorityQueue {
            e1 : Entity, e2: Entity -> Int
            (pos.squareDistanceTo(e1.positionVector) - pos.squareDistanceTo(e2.positionVector)).toInt()
        }

        for(entity in world.loadedEntityList) {
            if (entity == null || entity == player || entity !is EntityLiving) continue
            entityHeap.add(entity)
        }

        val playerPos = player.blockBelow?.toVec3() ?: return
        while(entityHeap.isNotEmpty()) {
            val entityPos = entityHeap.remove().blockBelow?.toVec3() ?: continue
            val entityRoute = Pathfinder.pathfind(entityPos.toBlockPos(), playerPos.toBlockPos()) ?: continue
            route = entityRoute
            break
        }
    }
}