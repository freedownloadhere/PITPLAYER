package com.github.freedownloadhere.pitplayer

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

object PlayerRemote {
    fun message(s : String) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(ChatComponentText(s))
    }
}