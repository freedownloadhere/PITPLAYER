package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.extensions.plus
import com.github.freedownloadhere.pitplayer.extensions.times
import net.minecraft.util.Vec3i
import kotlin.math.abs

class MovementArrayBuilder {
    private val array = arrayListOf<Movement>()
    private var mdir = Vec3i(0, 0, 0)
    private var mcost = 0.0
    private var mname = "Movement"
    private var mtype = Movement.Type.Walk

    fun new() : MovementArrayBuilder {
        mdir = Vec3i(0, 0, 0)
        mcost = 0.0
        mname = "Movement"
        mtype = Movement.Type.Walk
        return this
    }

    fun add(absdir : AbsoluteDirection, times : Int = 1) : MovementArrayBuilder {
        mdir += absdir.dir * times
        mcost += absdir.cost
        if(abs(mdir.x) > 1 || abs(mdir.z) > 1)
            mtype = Movement.Type.Jump
        return this
    }

    fun add(arr : ArrayList<Movement>) : MovementArrayBuilder {
        return this
    }

    fun name(name : String) : MovementArrayBuilder {
        mname = name
        return this
    }

    fun push() : MovementArrayBuilder {
        if(mtype == Movement.Type.Jump)
            mcost *= 1.25
        array.add(Movement(mdir, mcost, mname, mtype))
        return this
    }

    // twist anti-clockwise
    fun twist90onXZ() : MovementArrayBuilder {
        val newArray = arrayListOf<Movement>()
        for(i in array) {
            val newDir = Vec3i(-i.dir.z, i.dir.y, i.dir.x)
            newArray.add(Movement(newDir, i.cost, i.name, i.type))
        }
        array.clear()
        array.addAll(newArray)
        return this
    }

    fun finish() : ArrayList<Movement> {
        return array
    }
}