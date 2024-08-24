package com.github.freedownloadhere.pitplayer.extensions

import com.github.freedownloadhere.pitplayer.pathing.PathBlockHelper
import net.minecraft.entity.Entity
import net.minecraft.util.Vec3i

val Entity.blockBelow : Vec3i?
    get() = PathBlockHelper.firstBlockAtOrBelow(positionVector.toBlockPos())