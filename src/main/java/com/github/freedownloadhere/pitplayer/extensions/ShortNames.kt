package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient

val mc: Minecraft = Minecraft.getMinecraft()

val player : EntityPlayerSP = mc.thePlayer

val world : WorldClient = mc.theWorld