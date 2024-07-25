package com.github.freedownloadhere.pitplayer

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "pitplayer", name = "Pit Player", version = "1.0.0")
class Start {
    @EventHandler
    fun init(e : FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(EventManager())
    }
}