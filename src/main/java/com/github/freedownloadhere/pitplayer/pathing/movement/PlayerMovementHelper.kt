package com.github.freedownloadhere.pitplayer.pathing.movement

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import net.minecraft.util.Vec3

object PlayerMovementHelper {
    var motionVec = Vec3(0.0, 0.0, 0.0)

    private val nextPositionIsChangingBlock : Boolean
        get() {
            val pos1 = Vec3(player.prevPosX, player.prevPosY, player.prevPosZ)
            val pos2 = player.positionVector
            return !pos1.toBlockPos().matches(pos2.toBlockPos())
        }

    fun shouldJump(to : Pathfinder.SmallNode) : Boolean {
        if(!to.flags.read(Movement.Flags.Jump)) return false
        if(to.flags.read(Movement.Flags.Adjacent)) return true
        return nextPositionIsChangingBlock
    }
}