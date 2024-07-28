package com.github.freedownloadhere.pitplayer

import net.minecraft.util.Vec3i

fun Vec3i.add(other : Vec3i) : Vec3i {
    return Vec3i(x + other.x, y + other.y, z + other.z)
}