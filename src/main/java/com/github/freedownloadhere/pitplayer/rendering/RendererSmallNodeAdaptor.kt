package com.github.freedownloadhere.pitplayer.rendering

import com.github.freedownloadhere.pitplayer.pathing.Pathfinder
import java.awt.Color

object RendererSmallNodeAdaptor {
    fun blocks(nodeList : List<Pathfinder.SmallNode>, color : Color = Color.WHITE) {
        for(node in nodeList)
            Renderer.block(node.pos, color)
    }
}