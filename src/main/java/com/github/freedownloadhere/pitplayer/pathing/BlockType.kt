package com.github.freedownloadhere.pitplayer.pathing

import com.github.freedownloadhere.pitplayer.extensions.world
import com.github.freedownloadhere.pitplayer.pathing.BlockType.FullBlock
import com.github.freedownloadhere.pitplayer.pathing.BlockType.SlabBlock
import net.minecraft.block.BlockSlab
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

enum class BlockType(val moveset : ArrayList<AbsoluteDirection>) {
    FullBlock(AbsoluteDirectionArrayBuilder().normal().up().down().diagNormal().diagDown().diagUp().build()),
    SlabBlock(AbsoluteDirectionArrayBuilder().normal().down().diagNormal().diagDown().build());
}

fun blockTypeOf(pos : Vec3i) : BlockType {
    val blockstate = world.getBlockState(BlockPos(pos))
    val block = blockstate.block
    if(block is BlockSlab) {
        if(block.isDouble) return FullBlock
        if(blockstate.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM) return FullBlock
        return SlabBlock
    }
    return FullBlock
}