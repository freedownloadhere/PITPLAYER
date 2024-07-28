package com.github.freedownloadhere.pitplayer

import net.minecraft.scoreboard.Score
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventManager {
    @SubscribeEvent
    fun tick(e : ClientTickEvent) {
        if(e.phase != TickEvent.Phase.END) return
        if(!StateMachine.isIngame()) return
        GPS.pathfindTo(Vec3i(0, 81, 0))
    }

    @SubscribeEvent
    fun drawText(e : RenderGameOverlayEvent.Text) {
        if(!StateMachine.isIngame()) return
        Renderer.text("\u00A7lArea: \u00A77${StateMachine.currentArea().str}", 20, 20)
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!StateMachine.isIngame()) return
        for(i in 0..<(GPS.lastPath.size - 1))
            Renderer.highlightLine(GPS.lastPath[i], GPS.lastPath[i + 1])
    }
}