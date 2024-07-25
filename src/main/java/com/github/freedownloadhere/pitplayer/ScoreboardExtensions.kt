package com.github.freedownloadhere.pitplayer

import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.Scoreboard

val scoreboard = Minecraft.getMinecraft().theWorld.scoreboard

fun Scoreboard.getFullContents() : String {
    var s = ""
    for(i in 0..18) {
        s += scoreboard.getObjectiveInDisplaySlot(i)?.displayName
        s += ';'
    }
    return s
}