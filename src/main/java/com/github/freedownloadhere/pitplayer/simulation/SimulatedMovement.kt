package com.github.freedownloadhere.pitplayer.simulation

import kotlin.math.min

class SimulatedMovement(binaryString: String = "") {
    enum class Type(val v : Int) {
        Forward(1),
        Backward(2),
        Right(4),
        Left(8),
        Jumping(16),
        Sprinting(32),
        Sneaking(64)
    }

    private var f = 0

    init {
        for(i in 0..<min(7, binaryString.length))
            if(binaryString[i] == '1')
                f = f or (1 shl i)
    }

    fun read(t : Type) : Boolean {
        return (f and t.v != 0)
    }
}