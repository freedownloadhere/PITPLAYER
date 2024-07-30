package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.player
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventManager {
    @SubscribeEvent
    fun tick(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(!StateMachine.isIngame()) return
        GPS.traverseRoute()
    }

    @SubscribeEvent
    fun drawText(e : RenderGameOverlayEvent.Text) {
        if(!StateMachine.isIngame()) return
        //Renderer.text("\u00A7lArea: \u00A77${StateMachine.currentArea().str}", 20, 20)
        Renderer.text("\u00A7lAutoPilot: \u00A7${if(AutoPilot.isEnabled) "aEnabled" else "cDisabled"}", 20, 20)
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!StateMachine.isIngame()) return
        //Renderer.highlightNLines(GPS.route)
        if(GPS.route.isEmpty()) return
        Renderer.highlightNBlocks(GPS.route)
        Renderer.highlightLine(player.positionVector, GPS.route.last())
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}