package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.mc
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import org.lwjgl.opengl.GL11
import java.awt.Color

object Renderer {
    fun text(s : String, x : Int, y : Int) {
        mc.fontRendererObj.drawStringWithShadow(s, x.toFloat(), y.toFloat(), 0x00ffffff)
    }

    private fun highlightLineBegin() {
        GL11.glPushMatrix()
        val partialPos = player.partialPos
        GL11.glTranslated(-partialPos.x, -partialPos.y, -partialPos.z)
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDepthFunc(GL11.GL_ALWAYS)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }

    private fun highlightLineEnd() {
        GL11.glPopAttrib()
        GL11.glDepthFunc(GL11.GL_LEQUAL)
        GL11.glPopMatrix()
    }

    fun highlightLine(p1 : Vec3, p2 : Vec3) {
        highlightLineBegin()
        val worldRenderer = Tessellator.getInstance().worldRenderer
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
        worldRenderer.pos(p1.x, p1.y, p1.z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        worldRenderer.pos(p2.x, p2.y, p2.z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        Tessellator.getInstance().draw()
        highlightLineEnd()
    }

    fun highlightNLines(posList : List<Vec3>) {
        highlightLineBegin()
        val worldRenderer = Tessellator.getInstance().worldRenderer
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
        for(i in 0 .. (posList.size - 2)) {
            worldRenderer.pos(posList[i].x, posList[i].y, posList[i].z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
            worldRenderer.pos(posList[i + 1].x, posList[i + 1].y, posList[i + 1].z).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex()
        }
        Tessellator.getInstance().draw()
        highlightLineEnd()
    }

    fun highlightBlock(pos : Vec3, color : Color = Color.WHITE) {
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
        highlightLineBegin()
        val worldRenderer = Tessellator.getInstance().worldRenderer
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)
        for(edge in indices) {
            val p1 = corners[edge.first]
            val p2 = corners[edge.second]
            worldRenderer.pos(p1.x, p1.y, p1.z).color(color.red, color.green, color.blue, color.alpha).endVertex()
            worldRenderer.pos(p2.x, p2.y, p2.z).color(color.red, color.green, color.blue, color.alpha).endVertex()
        }
        Tessellator.getInstance().draw()
        highlightLineEnd()
    }

    fun highlightNBlocks(posList : List<Vec3>, color : Color = Color.WHITE) {
        for(pos in posList)
            highlightBlock(pos.toBlockPos().toVec3(), color)
    }
}