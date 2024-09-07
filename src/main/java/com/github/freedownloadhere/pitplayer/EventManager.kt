package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.combat.AimHelper
import com.github.freedownloadhere.pitplayer.combat.AutoClicker
import com.github.freedownloadhere.pitplayer.combat.AutoFighter
import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.TerrainTraversal
import com.github.freedownloadhere.pitplayer.utils.Keybinds
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.time.Instant

class EventManager {
    private var lastTime = Instant.now().toEpochMilli()

    @SubscribeEvent
    fun fixedUpdate(e : ClientTickEvent) {
        if(!BotState.ingame) return
        if(e.phase != TickEvent.Phase.END) return

        TerrainTraversal.updateRouteTraversal()
        AutoFighter.update()
    }

    @SubscribeEvent
    fun nonFixedUpdate(e : TickEvent.RenderTickEvent) {
        if(!BotState.ingame) return

        val newTime = Instant.now().toEpochMilli()
        val deltaTimeMillis = newTime - lastTime

        AutoClicker.update(deltaTimeMillis)
        AimHelper.update(deltaTimeMillis)

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
        if(!BotState.ingame) return

        TerrainTraversal.renderPath()
        Debug.renderBresenham()
        Debug.renderPath()
        Debug.renderClosestValid()
        Debug.renderMotionVec()
    }

    @SubscribeEvent
    fun renderText(e : RenderGameOverlayEvent.Text) {
        if(!BotState.ingame) return
    }

    @SubscribeEvent
    fun keyInput(e : InputEvent.KeyInputEvent) {
        for(k in Keybinds.entries)
            if(k.key.isPressed)
                k.action()
    }
}