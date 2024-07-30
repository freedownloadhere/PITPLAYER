package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
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
        while(route.isNotEmpty() && player.positionVector.squareDistanceToXZ(route.last()) <= 0.5)
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
            if (entity == null || entity == player || entity !is EntityLiving || pos.squareDistanceTo(entity.positionVector) >= 900) continue
            entityHeap.add(entity)
        }

        while(entityHeap.isNotEmpty()) {
            val entity = entityHeap.remove()
            val entityPos = entity.positionVector
            val playerHead = player.positionVector.toPlayerHead()
            val ray1 = world.rayTraceBlocks(playerHead, entityPos)
            println(ray1)
            val ray2 = world.rayTraceBlocks(playerHead, entityPos.toPlayerHead())
            println(ray2)
            if(ray1 != null && ray2 != null) continue
            makeRoute(entity.blockBelow ?: break)
            break
        }
    }
}