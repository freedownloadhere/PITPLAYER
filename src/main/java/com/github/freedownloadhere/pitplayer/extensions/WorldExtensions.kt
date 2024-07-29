package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.block.Block
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

fun WorldClient.blockAt(pos : Vec3i) : Block {
    return world.getBlockState(BlockPos(pos.x, pos.y, pos.z)).block
}