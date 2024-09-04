package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.combat.AutoFighter
import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.TerrainTraversal
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerMovementHelper
import com.github.freedownloadhere.pitplayer.rendering.Renderer
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

class EventManager {
    @SubscribeEvent
    fun onTick(e : ClientTickEvent) {
        if(!PlayerControlHelper.ingame) return
        if(e.phase != TickEvent.Phase.END) return

        TerrainTraversal.updateRouteTraversal()
        AutoFighter.attackTarget()
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!PlayerControlHelper.ingame) return

        TerrainTraversal.renderPath()
        Debug.renderBresenham()
        Debug.renderPath()
        Debug.renderClosestValid()
        Debug.renderMotionVec()
    }

    @SubscribeEvent
    fun renderText(e : RenderGameOverlayEvent.Text) {
        if(!PlayerControlHelper.ingame) return
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}