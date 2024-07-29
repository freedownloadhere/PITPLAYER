package com.github.freedownloadhere.pitplayer

import net.minecraft.util.Vec3i

fun Vec3i.add(other : Vec3i) : Vec3i {
    return Vec3i(x + other.x, y + other.y, z + other.z)
}

fun Vec3i.add(x1 : Int, y1 : Int, z1 : Int) : Vec3i {
    return Vec3i(x + x1, y + y1, z + z1)
}

val Vec3i.upOne : Vec3i
    get() = Vec3i(x, y + 1, z)

val Vec3i.upTwo : Vec3i
    get() = Vec3i(x, y + 2, z)

val Vec3i.upThree : Vec3i
    get() = Vec3i(x, y + 3, z)