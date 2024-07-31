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
        StateMachine.makeDecision()
    }

    @SubscribeEvent
    fun drawText(e : RenderGameOverlayEvent.Text) {
        if(!StateMachine.isIngame()) return
        Renderer.text("\u00A7lAction: \u00A77${StateMachine.State.action}", 10, 10)
        Renderer.text(PlayerRemote.state, 10, 20)
        Renderer.text(CombatModule.state, 10, 30)
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!StateMachine.isIngame()) return
        if(Pathfinder.blockLine.isEmpty()) return
        Renderer.highlightNBlocks(Pathfinder.blockLine)
        //if(GPS.route.isNullOrEmpty()) return
        //Renderer.highlightNBlocks(GPS.route!!)
        //Renderer.highlightLine(player.positionVector, GPS.route!!.last())
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}