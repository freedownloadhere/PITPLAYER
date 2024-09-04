package com.github.freedownloadhere.pitplayer.extensions

import com.github.freedownloadhere.pitplayer.pathing.utils.PathBlockHelper
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

val Entity.blockBelow : Vec3i?
    get() = PathBlockHelper.firstBlockAtOrBelow(positionVector.toBlockPos())

val Entity.eyePosition : Vec3
    get() = Vec3(posX, posY + eyeHeight, posZ)