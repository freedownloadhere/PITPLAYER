package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.extensions.eyePosition
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.interfaces.Killable
import com.github.freedownloadhere.pitplayer.utils.KeyBindHelper
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import kotlin.math.max
import kotlin.random.Random

object AutoFighter : Killable("AutoFighter") {
    enum class SprintResetMethod(val key : KeyBinding) {
        WTap(settings.keyBindForward),
        STap(settings.keyBindBack),
        Blockhit(settings.keyBindUseItem),
        SneakTap(settings.keyBindSneak)
    }

    enum class StrafeDirection(val key : KeyBinding) {
        Left(settings.keyBindLeft),
        Right(settings.keyBindRight)
    }

    var target : Entity? = null
        private set
    var sprintResetTicks : Int = 0
        private set
    var justAttacked : Boolean = false

    private var sprintResetMethod : SprintResetMethod = SprintResetMethod.SneakTap
    private var strafeDirection : StrafeDirection = StrafeDirection.Left
    private var strafeSwapCooldown : Int = 10

    fun update() {
        if(killed) return
        if(target?.isDead == true) onTargetLost()
        if(target == null) return

        AimHelper.lookAt(target!!.eyePosition)

        doStrafe()

        if(justAttacked) {
            sprintResetTicks = 5
            justAttacked = false
        }

        if(sprintResetTicks > 0) {
            sprintReset()
            return
        }

        KeyBindHelper.press(settings.keyBindForward)
    }

    fun findTarget() {
        if(killed) return

        val entityList = world.loadedEntityList
        val playerPos = player.positionVector
        var bestDist = Double.MAX_VALUE

        target = null
        for(entity in entityList) {
            if(entity == null) continue
            if(entity == player) continue
            if(entity !is EntityLiving && entity !is EntityPlayer) continue
            if(entity.isDead) continue
            if(!isEntityReachable(entity)) continue

            val dist = playerPos.squareDistanceTo(entity.positionVector)
            if(dist > bestDist) continue

            bestDist = dist
            target = entity
        }

        if(target != null)
            onFoundTarget()
    }

    private fun onFoundTarget() {
        Debug.Logger.regular("Found target \u00A73${target!!.name}")
        AutoClicker.start()
    }

    private fun sprintReset() {
        KeyBindHelper.press(sprintResetMethod.key)
        sprintResetTicks--
    }

    private fun doStrafe() {
        if(strafeSwapCooldown == 0) {
            strafeDirection = pickStrafeDirection()
            strafeSwapCooldown = 10
        }
        KeyBindHelper.press(strafeDirection.key)
        strafeSwapCooldown = max(0, strafeSwapCooldown - 1)
    }

    private fun onTargetLost() {
        Debug.Logger.regular("Target was lost")
        target = null
        AutoClicker.stop()
    }

    private fun pickStrafeDirection() : StrafeDirection {
        return if(Random.nextBoolean()) StrafeDirection.Left else StrafeDirection.Right
    }

    // TODO
    private fun isEntityReachable(entity : Entity) : Boolean {
        return true
    }
}