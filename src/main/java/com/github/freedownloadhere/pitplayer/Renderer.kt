package com.github.freedownloadhere.pitplayer

import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11

object Renderer {
    fun text(s : String, x : Int, y : Int) {
        mc.fontRendererObj.drawStringWithShadow(s, x.toFloat(), y.toFloat(), 0x00ffffff)
    }

    fun highlightBlock(pos : Vec3) {
        val corners = arrayListOf(
            pos.add(Vec3(0.0, 0.0, 0.0)),
            pos.add(Vec3(1.0, 0.0, 0.0)),
            pos.add(Vec3(1.0, 0.0, 1.0)),
            pos.add(Vec3(0.0, 0.0, 1.0)),
            pos.add(Vec3(0.0, 1.0, 0.0)),
            pos.add(Vec3(1.0, 1.0, 0.0)),
            pos.add(Vec3(1.0, 1.0, 1.0)),
            pos.add(Vec3(0.0, 1.0, 1.0)),
        )
        val indices = arrayListOf(
            Pair(0, 1),
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 0),
            Pair(0, 4),
            Pair(1, 5),
            Pair(2, 6),
            Pair(3, 7),
            Pair(4, 5),
            Pair(5, 6),
            Pair(6, 7),
            Pair(7, 4)
        )
        GL11.glPushMatrix()
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDepthFunc(GL11.GL_ALWAYS)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glTranslated(-player.posX, -player.posY, -player.posZ)
        val worldRenderer = Tessellator.getInstance().worldRenderer
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
        for(edge in indices) {
            val p1 = corners[edge.first]
            val p2 = corners[edge.second]
            worldRenderer.pos(p1.xCoord, p1.yCoord, p1.zCoord).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(p2.xCoord, p2.yCoord, p2.zCoord).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        }
        Tessellator.getInstance().draw()
        GL11.glPopAttrib()
        GL11.glDepthFunc(GL11.GL_LEQUAL)
        GL11.glPopMatrix()
    }

}