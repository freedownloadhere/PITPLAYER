package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import net.minecraft.entity.EntityLiving

object AutoFighter {
    var target : EntityLiving? = null
        private set

    fun findTarget() {
        val entityList = world.loadedEntityList
        val playerPos = player.positionVector
        var bestDist = Double.MAX_VALUE

        for(entity in entityList) {
            if(entity == null) continue
            if(entity == player) continue
            if(entity !is EntityLiving) continue
            if(entity.isDead) continue
            if(!isEntityReachable(entity)) continue

            val dist = playerPos.squareDistanceTo(entity.positionVector)
            if(dist > bestDist) continue

            bestDist = dist;
            target = entity
        }
    }

    fun attackTarget() {
        if(target == null || target!!.isDead) return
        PlayerControlHelper.lookAt(target!!)
        PlayerControlHelper.press(settings.keyBindAttack)
        PlayerControlHelper.press(settings.keyBindForward)
        PlayerControlHelper.press(settings.keyBindJump)
    }

    private fun isEntityReachable(entity : EntityLiving) : Boolean {
        // TODO
        return true
    }
}