package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.util.Vec3

class Vector3d(x : Double, y : Double, z : Double) : Vec3(x, y, z) {
    override fun equals(other: Any?): Boolean {
        if(other !is Vec3) return false
        return x == other.x && y == other.y && z == other.z
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun truncate() : Vector3d {
        return Vector3d(
            (x * 10).toInt() / 10.0,
            (y * 10).toInt() / 10.0,
            (z * 10).toInt() / 10.0,
        )
    }
}