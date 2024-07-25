package com.github.freedownloadhere.pitplayer

import net.minecraft.client.Minecraft

object StateMachine {
    fun isOnPit() : Boolean {
        if(Minecraft.getMinecraft().thePlayer == null) return false
        if(Minecraft.getMinecraft().theWorld == null) return false
        if(!Minecraft.getMinecraft().currentServerData.serverIP.lowercase().contains("hypixel")) return false;
        return true;
    }

    fun currentArea() : PitArea? {
        for(area in PitArea.entries)
            if(GPS.inArea(area.rect))
                return area;
        return null
    }
}