package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import net.minecraft.entity.EntityLiving

object AutoFighter : Toggleable(true) {
    var target : EntityLiving? = null
        private set
    var attackTicks : Int = 0
        private set
    var justAttacked : Boolean = false

    fun findTarget() {
        val entityList = world.loadedEntityList
        val playerPos = player.positionVector
        var bestDist = Double.MAX_VALUE

        target = null
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

        if(target != null)
            onFoundTarget()
    }

    private fun onFoundTarget() {
        Debug.Logger.regular("Found target \u00A73${target!!.name}")
        AutoClicker.enable()
    }

    fun update() {
        if(target?.isDead == true)
            onTargetLost()
        if(target == null)
            return
        PlayerControlHelper.lookAt(target!!)
        if(justAttacked) {
            attackTicks = 4
            justAttacked = false
        }
        if(attackTicks > 0) {
            onAttack()
            return
        }
        PlayerControlHelper.press(settings.keyBindForward)
    }

    fun onAttack() {
        Debug.Logger.regular("Attacked target..")
        PlayerControlHelper.press(settings.keyBindUseItem)
        attackTicks--
        if(attackTicks > 0) return
        PlayerControlHelper.release(settings.keyBindUseItem)
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