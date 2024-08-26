package com.github.freedownloadhere.pitplayer.pathing.moveset

import net.minecraft.util.Vec3i

data class Movement(
    val dir : Vec3i,
    val cost : Double,
    val name : String,
    val flags : FlagInt
) {
    enum class Flags(val bit : Int) {
        Walk(1), Jump(2), Fall(4), Adjacent(8)
    }

    class FlagInt {
        private var f = 0
        fun add(b : Flags) : FlagInt {
            f = f or b.bit
            return this
        }
        fun clear(b : Flags) : FlagInt {
            f = f and b.bit.inv()
            return this
        }
        fun read(b : Flags) : Boolean {
            return (f and b.bit != 0)
        }
    }

    val isDiagonal : Boolean
        get() = (dir.x != 0 && dir.z != 0)

    val isDown : Boolean
        get() = (dir.y < 0)

    val isUp : Boolean
        get() = (dir.y > 0)
}