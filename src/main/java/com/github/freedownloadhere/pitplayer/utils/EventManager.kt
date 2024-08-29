package com.github.freedownloadhere.pitplayer.utils

import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventManager {
    @SubscribeEvent
    fun onTick(e : ClientTickEvent) {
        if(!PlayerControlHelper.ingame) return
        if(e.phase != TickEvent.Phase.END) return
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!PlayerControlHelper.ingame) return
    }

    @SubscribeEvent
    fun renderText(e : RenderGameOverlayEvent.Text) {
        if(!PlayerControlHelper.ingame) return
    }
}