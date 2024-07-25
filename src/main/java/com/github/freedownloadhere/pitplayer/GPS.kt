package com.github.freedownloadhere.pitplayer

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP

object GPS {
    private val player : EntityPlayerSP = Minecraft.getMinecraft().thePlayer

    fun inArea(area : AreaRect) : Boolean {
        return (
        area.x1 <= player.posX && player.posX <= area.x2 &&
        area.y1 <= player.posY && player.posY <= area.y2 &&
        area.z1 <= player.posZ && player.posZ <= area.z2)
    }
}