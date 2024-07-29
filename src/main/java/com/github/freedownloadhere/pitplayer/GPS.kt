package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue

object GPS {
    var route = mutableListOf<Vec3>()

    fun pathfindTo(dest : Vec3i) {
        val r = Pathfinder.pathfind(dest, player.blockBelow ?: return) ?: return
        if(r.isEmpty()) return
        route = r
        PlayerRemote.lookAt(route.last().toPlayerHead())
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