package com.github.freedownloadhere.pitplayer.modules

import com.github.freedownloadhere.pitplayer.extensions.blockBelow
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.toPlayerHead
import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.utils.PlayerRemote
import kotlinx.coroutines.launch
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import java.util.*
import javax.xml.bind.JAXBElement.GlobalScope
import kotlin.math.floor

object CombatModule {
    val state : String
        get() = "\u00A7lTarget: \u00A7${if(target == null) "7None" else "6${target!!.name}"}"

    var target : Entity? = null
    private val targetList = PriorityQueue { e1 : Entity, e2 : Entity -> Int
        val playerPos = player.positionVector
        floor(playerPos.squareDistanceTo(e1.positionVector) - playerPos.squareDistanceTo(e2.positionVector)).toInt()
    }
    private var maxEngageDistance = 900

    private fun makeTargetList() {
        targetList.clear()
        val playerPos = player.positionVector
        val playerHead = playerPos.toPlayerHead()
        for(entity in world.loadedEntityList) {
            if (entity == null || entity == player || entity !is EntityLiving || entity.isDead) continue

            val entityPos = entity.positionVector

            if(playerPos.squareDistanceTo(entityPos) >= maxEngageDistance) continue

            val distance = playerPos.squareDistanceTo(entityPos)
            if(distance > maxEngageDistance) continue

            val ray1 = world.rayTraceBlocks(playerHead, entityPos)
            val ray2 = world.rayTraceBlocks(playerHead, entityPos.toPlayerHead())
            if(ray1 != null && ray2 != null) continue

            targetList.add(entity)
        }
    }

    fun findTarget() {
        target = null
        makeTargetList()
        while(targetList.isNotEmpty()) {
            val entity = targetList.remove()
            //TODO lol
            //Pathfinder.pathfind(entity.blockBelow, player.blockBelow)
            target = entity
            break
        }
    }

    fun attackTarget() {
        if(target == null) return
        val targetPos = target!!.blockBelow ?: return
        GPS.dest = targetPos
        GPS.makeRoute()
        GPS.traverseRoute()
        PlayerRemote.attack()
    }
}