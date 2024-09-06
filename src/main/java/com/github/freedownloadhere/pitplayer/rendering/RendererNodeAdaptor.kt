package com.github.freedownloadhere.pitplayer.rendering

import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import net.minecraft.util.Vec3
import java.awt.Color

object RendererNodeAdaptor {
    fun lines(nodeList : List<Pathfinder.Node>, color : Color = Color.WHITE) {
        val vecList = mutableListOf<Vec3>()
        for(node in nodeList)
            vecList.add(node.pos)
        Renderer.lines(vecList, color)
    }
}