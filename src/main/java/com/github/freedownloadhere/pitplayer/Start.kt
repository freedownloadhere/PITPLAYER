package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.combat.AutoClicker
import com.github.freedownloadhere.pitplayer.commands.DebugCommand
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import com.github.freedownloadhere.pitplayer.utils.EventManager
import com.github.freedownloadhere.pitplayer.utils.Keybinds
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "pitplayer", name = "Pit Player", version = "1.0.0")
class Start {
    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    fun init(e : FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(EventManager())
        ClientCommandHandler.instance.registerCommand(DebugCommand())
        for(k in Keybinds.entries)
            ClientRegistry.registerKeyBinding(k.key)
        GlobalScope.launch {
            while(true) {
                delay(1L)
                if(!PlayerControlHelper.ingame) continue
                AutoClicker.update()
            }
        }
    }
}