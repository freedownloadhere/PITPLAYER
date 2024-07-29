package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i

val Vec3.x : Double
    get() = xCoord

val Vec3.y : Double
    get() = yCoord

val Vec3.z : Double
    get() = zCoord

fun Vec3.toVec3i() : Vec3i {
    return Vec3i(
        if(x >= 0) x.toInt() else x.toInt() - 1,
        if(y >= 0) y.toInt() else y.toInt() - 1,
        if(z >= 0) z.toInt() else z.toInt() - 1
    )
}