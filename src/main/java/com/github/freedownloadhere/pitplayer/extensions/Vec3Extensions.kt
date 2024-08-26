package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import kotlin.math.floor

val Vec3.x : Double
    get() = xCoord

val Vec3.y : Double
    get() = yCoord

val Vec3.z : Double
    get() = zCoord

fun Vec3.toBlockPos() : Vec3i {
    return Vec3i(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
}

fun Vec3.toBlockTop() : Vec3 {
    return add(0.5, 1.0, 0.5)
}

fun Vec3.toPlayerHead() : Vec3 {
    return add(0.0, 1.625, 0.0)
}

operator fun Vec3.plus(other : Vec3i) : Vec3 {
    return this + other.toVec3()
}

operator fun Vec3.plus(other : Vec3) : Vec3 {
    return Vec3(x + other.x, y + other.y, z + other.z)
}

operator fun Vec3.times(scalar : Double) : Vec3 {
    return Vec3(scalar * x, scalar * y, scalar * z)
}

fun Vec3.add(x1 : Double, y1 : Double, z1 : Double) : Vec3 {
    return Vec3(x + x1, y + y1, z + z1)
}

fun Vec3.squareDistanceToXZ(pos : Vec3) : Double {
    return squareDistanceTo(Vec3(pos.x, y, pos.z))
}