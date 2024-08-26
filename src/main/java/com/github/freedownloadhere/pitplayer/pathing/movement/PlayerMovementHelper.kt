package com.github.freedownloadhere.pitplayer.pathing.movement

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement

object PlayerMovementHelper {
    private val nextPositionIsChangingBlock : Boolean
        get() {
            val pos1 = player.positionVector
            val pos2 = player.nextPosition
            return !pos1.toBlockPos().matches(pos2.toBlockPos())
        }

    fun shouldJump(to : Pathfinder.SmallNode) : Boolean {
        if(!to.flags.read(Movement.Flags.Jump)) return false
        if(to.flags.read(Movement.Flags.Adjacent)) return true
        return nextPositionIsChangingBlock
    }
}