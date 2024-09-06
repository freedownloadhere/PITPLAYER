package com.github.freedownloadhere.pitplayer.extensions

import com.github.freedownloadhere.pitplayer.utils.Vector3d
import net.minecraft.entity.player.EntityPlayer

val EntityPlayer.positionVector3d : Vector3d
    get() = Vector3d(posX, posY, posZ)