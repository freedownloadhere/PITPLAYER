package com.github.freedownloadhere.pitplayer.simulation

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class SimulatedMovement(val forward : Float, val left : Float, val jump : Boolean) {
    override fun toString(): String {
        return StringBuilder()
            .append("{ ")
            .append(if(forward > 0.0f) "Forward" else if(forward == 0.0f) "None" else "Backward")
            .append(", ")
            .append(if(left > 0.0f) "Left" else if(left == 0.0f) "None" else "Right")
            .append(", ")
            .append(if(jump) "Jump" else "No Jump")
            .append(" }")
            .toString()
    }

    // why did i hardcode this bro

    enum class Moveset(val sm : SimulatedMovement) {
        None(SimulatedMovement(0.0f, 0.0f, false)),
        Forward(SimulatedMovement(1.0f, 0.0f, false)),
        Backward(SimulatedMovement(-1.0f, 0.0f, false)),

        Left(SimulatedMovement(0.0f, 1.0f, false)),
        FL(SimulatedMovement(1.0f, 1.0f, false)),
        BL(SimulatedMovement(-1.0f, 1.0f, false)),

        Right(SimulatedMovement(0.0f, -1.0f, false)),
        FR(SimulatedMovement(1.0f, -1.0f, false)),
        BR(SimulatedMovement(-1.0f, -1.0f, false)),

        JumpNone(SimulatedMovement(0.0f, 0.0f, true)),
        JumpForward(SimulatedMovement(1.0f, 0.0f, true)),
        JumpBackward(SimulatedMovement(-1.0f, 0.0f, true)),

        JumpLeft(SimulatedMovement(0.0f, 1.0f, true)),
        JumpFL(SimulatedMovement(1.0f, 1.0f, true)),
        JumpBL(SimulatedMovement(-1.0f, 1.0f, true)),

        JumpRight(SimulatedMovement(0.0f, -1.0f, true)),
        JumpFR(SimulatedMovement(1.0f, -1.0f, true)),
        JumpBR(SimulatedMovement(-1.0f, -1.0f, true));

        val cost : Double =
            (1.4 * abs(min(sm.forward.toDouble(), sm.left.toDouble())) +
                abs(max(sm.forward.toDouble(), sm.left.toDouble()) - min(sm.forward.toDouble(), sm.left.toDouble())) +
                10.0 * if(sm.jump) 1.0 else 0.0) * 0.2
    }
}