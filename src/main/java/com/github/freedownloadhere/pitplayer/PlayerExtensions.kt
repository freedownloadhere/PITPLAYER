package com.github.freedownloadhere.pitplayer

import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.Vec3

val EntityPlayerSP.headPosVector : Vec3
    get() = positionVector.addVector(0.0, 1.625, 0.0)