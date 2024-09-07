package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.combat.AutoClicker
import com.github.freedownloadhere.pitplayer.combat.AutoFighter
import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.TerrainTraversal
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.time.Instant

class EventManager {
    @SubscribeEvent
    fun onTick(e : ClientTickEvent) {
        if(!KeyBindHelper.ingame) return
        if(e.phase != TickEvent.Phase.END) return

        TerrainTraversal.updateRouteTraversal()
        AutoFighter.update()
    }

    private var lastTime = Instant.now().toEpochMilli()

    @SubscribeEvent
    fun onRenderTick(e : TickEvent.RenderTickEvent) {
        val newTime = Instant.now().toEpochMilli()
        val deltaTimeMillis = newTime - lastTime

        AutoClicker.update(deltaTimeMillis)

        lastTime = newTime
    }

    @SubscribeEvent
    fun onAttackEntity(e : AttackEntityEvent) {
        if(e.target == null) return
        if(e.target == AutoFighter.target && AutoFighter.sprintResetTicks == 0)
            AutoFighter.justAttacked = true
    }

    @SubscribeEvent
    fun highlightBlock(e : DrawBlockHighlightEvent) {
        if(!KeyBindHelper.ingame) return

        TerrainTraversal.renderPath()
        Debug.renderBresenham()
        Debug.renderPath()
        Debug.renderClosestValid()
        Debug.renderMotionVec()
    }

    @SubscribeEvent
    fun renderText(e : RenderGameOverlayEvent.Text) {
        if(!KeyBindHelper.ingame) return
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}