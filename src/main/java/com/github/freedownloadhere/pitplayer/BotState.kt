package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.mc
import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import net.minecraft.util.Vec3i

object BotState {
    val ingame : Boolean
        get() = mc.thePlayer != null && mc.theWorld != null
    var path : MutableList<Pathfinder.SmallNode>? = null
    var dest : Vec3i? = null
}