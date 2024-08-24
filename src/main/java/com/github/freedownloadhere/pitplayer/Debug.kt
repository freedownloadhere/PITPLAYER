package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.rendering.Renderer
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import java.awt.Color

object Debug {
    var pathStep = true
    var showLineOfSight = true
    var showPathCurrent = true

    var pathStepCount = 1
    var lineOfSight : List<Vec3i>? = null
    var pathCurrent : List<Vec3>? = null

    fun renderLineOfSight() {
        if(!showLineOfSight || lineOfSight == null) return
        Renderer.blocksVec3i(lineOfSight!!, Color.MAGENTA)
    }

    fun renderPathCurrent() {
        if(!showPathCurrent || pathCurrent == null) return
        Renderer.blocks(pathCurrent!!, Color.GREEN)
    }
}