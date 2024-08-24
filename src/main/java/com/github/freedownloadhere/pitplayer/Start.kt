package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.commands.BlockLineCommand
import com.github.freedownloadhere.pitplayer.commands.DebugCommand
import com.github.freedownloadhere.pitplayer.commands.FightCommand
import com.github.freedownloadhere.pitplayer.commands.PathfindCommand
import com.github.freedownloadhere.pitplayer.utils.EventManager
import com.github.freedownloadhere.pitplayer.utils.Keybinds
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "pitplayer", name = "Pit Player", version = "1.0.0")
class Start {
    @EventHandler
    fun init(e : FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(EventManager())
        ClientCommandHandler.instance.registerCommand(PathfindCommand())
        ClientCommandHandler.instance.registerCommand(FightCommand())
        ClientCommandHandler.instance.registerCommand(BlockLineCommand())
        ClientCommandHandler.instance.registerCommand(DebugCommand())
        for(k in Keybinds.entries)
            ClientRegistry.registerKeyBinding(k.key)
    }
}