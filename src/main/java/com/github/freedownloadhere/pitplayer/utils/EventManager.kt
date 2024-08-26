package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.GPS
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventManager {
    @SubscribeEvent
    fun onTick(e : ClientTickEvent) {
        if(!PlayerControlHelper.ingame) return
        if(e.phase != TickEvent.Phase.END) return

        GPS.updateRouteTraversal()
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!PlayerControlHelper.ingame) return

        GPS.renderPath()
        Debug.renderBresenham()
        Debug.renderPath()
        Debug.renderClosestValid()
        Debug.renderMotionVec()
        Debug.renderNextBlock()
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}