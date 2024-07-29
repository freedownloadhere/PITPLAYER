package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.block.Block
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

fun WorldClient.blockAt(pos : Vec3i) : Block {
    return getBlockState(BlockPos(pos.x, pos.y, pos.z)).block
}

fun WorldClient.isSolid(pos : Vec3i) : Boolean {
    return blockAt(pos).material.isSolid
}

fun WorldClient.isObstructed(pos : Vec3i) : Boolean {
    return isSolid(pos.upOne) || isSolid(pos.upTwo)
}

fun WorldClient.isWalkable(pos : Vec3i) : Boolean {
    return isSolid(pos) && !isObstructed(pos)
}