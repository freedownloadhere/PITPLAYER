package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

class DebugCommand : CommandBase() {
    override fun getCommandName(): String {
        return "pdebug"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args == null || args.isEmpty()) return
        when(args[0]) {
            "simulate" -> {
                if(args.size < 4)
                    Debug.simulateNewPlayer()
                else
                    Debug.simulateMove(args[1], args[2], args[3])
            }
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): List<String> {

        if(args == null) return listOf()

        if(args.isEmpty())
            return getListOfStringsMatchingLastWord(args, listOf("bresenham", "path", "closest", "route", "option"))

        return listOf()
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}