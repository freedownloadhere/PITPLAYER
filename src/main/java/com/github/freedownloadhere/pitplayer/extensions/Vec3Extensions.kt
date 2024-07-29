package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

fun Vec3.toVec3i() : Vec3i {
    return Vec3i(
        if(xCoord >= 0) xCoord.toInt() else xCoord.toInt() - 1,
        if(yCoord >= 0) yCoord.toInt() else yCoord.toInt() - 1,
        if(zCoord >= 0) zCoord.toInt() else zCoord.toInt() - 1
    )
}

fun euclidean(v1 : Vec3, v2 : Vec3) : Double {
    return (v1.xCoord - v2.xCoord) * (v1.xCoord - v2.xCoord) + (v1.yCoord - v2.yCoord) * (v1.yCoord - v2.yCoord) + (v1.zCoord - v2.zCoord) * (v1.zCoord - v2.zCoord)
}