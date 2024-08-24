package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.Debug
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import com.github.freedownloadhere.pitplayer.extensions.partialPos
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.modules.GPS
import com.github.freedownloadhere.pitplayer.pathing.NeighbourCones
import com.github.freedownloadhere.pitplayer.state.StateMachine
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.awt.Color

class EventManager {
    @SubscribeEvent
    fun tick(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(!StateMachine.isIngame()) return
        StateMachine.makeDecision()
    }

    @SubscribeEvent
    fun drawText(e : RenderGameOverlayEvent.Text) {
        if(!StateMachine.isIngame()) return
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!StateMachine.isIngame()) return
        Debug.renderLineOfSight()
        Debug.renderPathCurrent()
        GPS.renderPath()
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}