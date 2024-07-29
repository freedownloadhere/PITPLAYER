package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.mc
import com.github.freedownloadhere.pitplayer.extensions.player
import net.minecraft.client.Minecraft

object StateMachine {
    fun isIngame() : Boolean {
        if(Minecraft.getMinecraft().thePlayer == null) return false
        if(Minecraft.getMinecraft().theWorld == null) return false
        return true
    }

    fun isInHypixel() : Boolean {
        if(!isIngame()) return false
        if(mc.isSingleplayer) return false
        if(!mc.currentServerData.serverIP.lowercase().contains("hypixel")) return false;
        return true;
    }

    fun isInThePit() : Boolean {
        if(!isInHypixel()) return false
        return ScoreboardReader.contents.lowercase().contains("the hypixel pit")
    }

    fun currentArea() : PitArea {
        val playerPos = player.positionVector
        for(area in PitArea.entries)
            if(area.rect.contains(playerPos))
                return area;
        return PitArea.UNKNOWN
    }
}