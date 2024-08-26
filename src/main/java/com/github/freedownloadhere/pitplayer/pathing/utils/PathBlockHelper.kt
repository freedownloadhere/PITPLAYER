package com.github.freedownloadhere.pitplayer.pathing.utils

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement
import com.github.freedownloadhere.pitplayer.pathing.moveset.Movement.*
import net.minecraft.block.Block
import net.minecraft.block.BlockSlab
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

object PathBlockHelper {
    enum class BlockType {
        Full, LowerSlab
    }

    private fun blockState(pos : Vec3i) : IBlockState {
        return world.getBlockState(BlockPos(pos))
    }

    private fun blockAt(pos : Vec3i) : Block {
        return blockState(pos).block
    }

    private fun isSolid(pos : Vec3i) : Boolean {
        return blockAt(pos).material.isSolid
    }

    private fun isObstructed(pos : Vec3i) : Boolean {
        return isSolid(pos.upOne) || isSolid(pos.upTwo)
    }

    private fun isDiagObstructed(curr : Vec3i, next : Vec3i) : Boolean {
        return (isObstructed(Vec3i(curr.x, curr.y + 1, next.z)) || isObstructed(Vec3i(next.x, curr.y + 1, curr.z)))
                && (isObstructed(Vec3i(curr.x, curr.y + 2, next.z)) || isObstructed(Vec3i(next.x, curr.y + 2, curr.z)))
    }

    private fun isWalkable(pos : Vec3i) : Boolean {
        return isSolid(pos) && !isObstructed(pos)
    }

    private fun isJumpable(pos : Vec3i) : Boolean {
        return !isSolid(pos.upOne) && !isSolid(pos.upTwo) && !isSolid(pos.upThree)
    }

    private fun hasLineOfSight(curr : Vec3i, next : Vec3i, move : Movement) : Boolean {
        if(move.flags.read(Flags.Walk) || move.flags.read(Flags.Adjacent))
            return isValidWalk(curr, next, move)

        if(!isWalkable(next))
            return false

        val posList = Bresenham.xz(curr, next)

        return isValidJump(posList)
    }

    private fun isValidJump(posList : ArrayList<Vec3i>) : Boolean {
        for(pos in posList)
            if(!isJumpable(pos))
                return false
        return true
    }

    private fun isValidWalk(curr : Vec3i, next : Vec3i, move : Movement) : Boolean {
        if(!isWalkable(next))
            return false

        if(move.isDiagonal && isDiagObstructed(curr, next))
            return false

        if(move.isDown && !isJumpable(next))
            return false

        if(move.isUp && !isJumpable(curr))
            return false

        return true
    }

    fun isValid(curr : Vec3i, next : Vec3i, move : Movement) : Boolean {
        return hasLineOfSight(curr, next, move)
    }

    private fun typeOf(pos : Vec3i) : BlockType {
        val bs = blockState(pos)
        val b = bs.block
        if(b is BlockSlab) {
            if(b.isDouble) return BlockType.Full
            if(bs.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM) return BlockType.Full
            return BlockType.LowerSlab
        }
        return BlockType.Full
    }

    fun firstBlockAtOrBelow(pos : Vec3i) : Vec3i? {
        var below = pos
        while(below.y > 0 && !isSolid(below))
            below = below.downOne
        return if(below.y > 0) below else null
    }

    // TODO restore
    //fun getMoveset(pos : Vec3i) : Set<AbsoluteDirection> {
        //return typeOf(pos).moveset
    //}

    //fun isInMoveset(block : Vec3i, move : Vec3i) : Boolean {
        //val btype = typeOf(block)
        //val d = AbsoluteDirection.fromPos(move) ?: return false
        //return btype.moveset.contains(d)
    //}

}