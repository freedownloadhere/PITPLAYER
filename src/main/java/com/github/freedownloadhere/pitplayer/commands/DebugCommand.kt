package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.Debug
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class DebugCommand : CommandBase() {
    override fun getCommandName(): String {
        return "debug"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args == null || args.size != 2) return
        when(args[0]) {
            "step" -> Debug.pathStepCount = args[1].toInt()
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}