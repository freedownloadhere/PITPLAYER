package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import net.minecraft.util.Vec3i

object BotState {
    var path : MutableList<Pathfinder.SmallNode>? = null
    var dest : Vec3i? = null
}