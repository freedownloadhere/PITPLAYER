package com.github.freedownloadhere.pitplayer.simulation

import com.github.freedownloadhere.pitplayer.extensions.positionVector3d
import com.github.freedownloadhere.pitplayer.mixin.AccessorEntityLivingBase
import net.minecraft.entity.player.EntityPlayer

object PlayerImageHelper {
    fun getState(entity : EntityPlayer) = PlayerImage(
        entity.positionVector3d,

        entity.rotationYaw,
        entity.rotationPitch,

        entity.motionX,
        entity.motionY,
        entity.motionZ,

        (entity as AccessorEntityLivingBase).jumpTicks_pitplayer,
        entity.jumpMovementFactor,
        entity.capabilities.walkSpeed,
    )
}