package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient

val mc: Minecraft
    get() = Minecraft.getMinecraft()

val player : EntityPlayerSP
    get() = Minecraft.getMinecraft().thePlayer

val world : WorldClient
    get() = Minecraft.getMinecraft().theWorld