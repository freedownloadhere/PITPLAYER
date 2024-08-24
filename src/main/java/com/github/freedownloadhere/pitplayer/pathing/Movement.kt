package com.github.freedownloadhere.pitplayer.pathing

import net.minecraft.util.Vec3i

data class Movement(
    val dir : Vec3i,
    val cost : Double,
    val name : String,
    val type : Type
) {
    enum class Type {
        Walk, Jump, Fall
    }

    val isDiagonal : Boolean
        get() = (dir.x != 0 && dir.z != 0)

    val isDown : Boolean
        get() = (dir.y < 0)

    val isUp : Boolean
        get() = (dir.y > 0)
}