package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.pathing.AbsoluteDirection.*

class AbsoluteDirectionArrayBuilder {
    private val array = arrayListOf<AbsoluteDirection>()

    fun normal() : AbsoluteDirectionArrayBuilder {
        array.add(PX)
        array.add(PZ)
        array.add(NX)
        array.add(NZ)
        return this
    }

    fun down() : AbsoluteDirectionArrayBuilder {
        array.add(DownPX)
        array.add(DownPZ)
        array.add(DownNX)
        array.add(DownNZ)
        return this
    }

    fun up() : AbsoluteDirectionArrayBuilder {
        array.add(UpPX)
        array.add(UpPZ)
        array.add(UpNX)
        array.add(UpNZ)
        return this
    }

    fun diagNormal() : AbsoluteDirectionArrayBuilder {
        array.add(DiagPXPZ)
        array.add(DiagPXNZ)
        array.add(DiagNXPZ)
        array.add(DiagNXNZ)
        return this
    }

    fun diagDown() : AbsoluteDirectionArrayBuilder {
        array.add(DiagDownPXPZ)
        array.add(DiagDownPXNZ)
        array.add(DiagDownNXPZ)
        array.add(DiagDownNXNZ)
        return this
    }

    fun diagUp() : AbsoluteDirectionArrayBuilder {
        array.add(DiagUpPXPZ)
        array.add(DiagUpPXNZ)
        array.add(DiagUpNXPZ)
        array.add(DiagUpNXNZ)
        return this
    }

    fun build() : ArrayList<AbsoluteDirection> {
        return array
    }
}