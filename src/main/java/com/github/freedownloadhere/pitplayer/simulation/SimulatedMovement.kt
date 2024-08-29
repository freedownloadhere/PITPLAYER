package com.github.freedownloadhere.pitplayer.simulation

import java.lang.StringBuilder

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
}