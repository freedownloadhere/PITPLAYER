package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.util.PriorityQueue

object GPS {
    var route = mutableListOf<Vec3>()

    fun inArea(area : AreaRect) : Boolean {
        return (
        area.x1 <= player.posX && player.posX <= area.x2 &&
        area.y1 <= player.posY && player.posY <= area.y2 &&
        area.z1 <= player.posZ && player.posZ <= area.z2)
    }

    fun pathfindTo(dest : Vec3i) {
        val r = Pathfinder.pathfind(dest, player.groundedBlockPos ?: return) ?: return
        route = r
    }

    fun pathfindToNearestEntity() {
        val pos = player.positionVector
        val entityHeap = PriorityQueue {
            e1 : Entity, e2: Entity -> Int
            (euclidean(pos, e1.positionVector) - euclidean(pos, e2.positionVector)).toInt()
        }

        for(entity in world.loadedEntityList) {
            if (entity == null || entity == player || entity !is EntityLiving) continue
            entityHeap.add(entity)
        }

        val playerPos = player.groundedBlockPos?.toVec3() ?: return
        while(entityHeap.isNotEmpty()) {
            val entityPos = entityHeap.remove().groundedBlockPos?.toVec3() ?: continue
            val entityRoute = Pathfinder.pathfind(entityPos.toVec3i(), playerPos.toVec3i()) ?: continue
            route = entityRoute
            break
        }
    }
}