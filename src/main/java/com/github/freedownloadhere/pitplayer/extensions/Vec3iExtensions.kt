package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import kotlin.math.abs
import kotlin.math.sqrt

fun Vec3i.add(other : Vec3i) : Vec3i {
    return Vec3i(x + other.x, y + other.y, z + other.z)
}

fun Vec3i.add(x1 : Int, y1 : Int, z1 : Int) : Vec3i {
    return Vec3i(x + x1, y + y1, z + z1)
}

fun Vec3i.subtract(other : Vec3i) : Vec3i {
    return Vec3i(x - other.x, y - other.y, z - other.z)
}

val Vec3i.downOne : Vec3i
    get() = Vec3i(x, y - 1, z)

val Vec3i.upOne : Vec3i
    get() = Vec3i(x, y + 1, z)

val Vec3i.upTwo : Vec3i
    get() = Vec3i(x, y + 2, z)

val Vec3i.upThree : Vec3i
    get() = Vec3i(x, y + 3, z)

fun Vec3i.toVec3() : Vec3 {
    return Vec3(x.toDouble(), y.toDouble(), z.toDouble())
}

fun Vec3i.manhattan(other : Vec3i) : Int {
    return (abs(x - other.x) + abs(y - other.y) + abs(z - other.z))
}

fun Vec3i.distance(other : Vec3i) : Double {
    return sqrt(((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z)).toDouble())
}

fun Vec3i.matches(other : Vec3i) : Boolean {
    return x == other.x && y == other.y && z == other.z
}