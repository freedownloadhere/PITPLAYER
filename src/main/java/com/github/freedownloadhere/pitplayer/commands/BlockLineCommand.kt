package com.github.freedownloadhere.pitplayer.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.Vec3i

class BlockLineCommand : CommandBase() {
    override fun getCommandName(): String {
        return "blockline"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args == null || args.size < 3) return
        val dest = Vec3i(args[0].toInt(), args[1].toInt(), args[2].toInt())
//      Pathfinder.makeBlockLine(dest, player.blockBelow)
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}