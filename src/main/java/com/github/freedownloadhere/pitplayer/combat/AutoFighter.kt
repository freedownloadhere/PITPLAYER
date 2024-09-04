package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import kotlinx.coroutines.delay
import net.minecraft.entity.EntityLiving
import kotlin.random.Random

object AutoFighter : Toggleable(true) {
    var target : EntityLiving? = null
        private set
    var justAttacked : Boolean = false

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

    fun update() {
        if(target?.isDead == true)
            onTargetLost()
        if(target == null)
            return
        AutoClicker.enable()
        PlayerControlHelper.lookAt(target!!)
        PlayerControlHelper.press(settings.keyBindForward)
    }

    suspend fun onAttack() {
        if(justAttacked) return
        Debug.Logger.regular("Attacked target..")
        justAttacked = true
        PlayerControlHelper.press(settings.keyBindUseItem)
        delay(50L)
        PlayerControlHelper.release(settings.keyBindUseItem)
        justAttacked = false
    }

    private fun onTargetLost() {
        target = null
        AutoClicker.disable()
        Debug.Logger.regular("Target was lost")
    }

    private fun isEntityReachable(entity : EntityLiving) : Boolean {
        // TODO
        return true
    }
}