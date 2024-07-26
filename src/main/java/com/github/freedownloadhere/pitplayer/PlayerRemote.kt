package com.github.freedownloadhere.pitplayer

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

object PlayerRemote {
    fun message(s : String, prefix : String? = null){
        var msg = ""
        if(prefix != null) msg += "[$prefix] "
        msg += s
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(msg))
    }
}