package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.moveset.NeighbourCones
import com.github.freedownloadhere.pitplayer.simulation.SimulatedMovement
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3i

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
            "bresenham" -> {
                if(args.size < 7) return
                val pos1 = Vec3i(args[1].toInt(), args[2].toInt(), args[3].toInt())
                val pos2 = Vec3i(args[4].toInt(), args[5].toInt(), args[6].toInt())
                Debug.computeBresenham(pos1, pos2)
            }
            "path" -> {
                if(args.size < 7) return
                val pos1 = Vec3i(args[1].toInt(), args[2].toInt(), args[3].toInt())
                val pos2 = Vec3i(args[4].toInt(), args[5].toInt(), args[6].toInt())
                Debug.computePath(pos1, pos2)
            }
            "closest" -> {
                if(args.size < 5) return
                val pos = Vec3i(args[1].toInt(), args[2].toInt(), args[3].toInt())
                val cone = NeighbourCones.entries[args[4].toInt()]
                Debug.computeClosestValid(pos, cone)
            }
            "route" -> {
                if(args.size < 4) return
                val pos = Vec3i(args[1].toInt(), args[2].toInt(), args[3].toInt())
                Debug.createRoute(pos)
            }
            "option" -> {
                if(args.size < 3) return
                val option = args[1]
                val value = args[2].toBoolean()
                Debug.setOption(option, value)
            }
            "simulate" -> {
                if(args.size == 1)
                    Debug.simulateNewPlayer()
                else
                    Debug.simulateMove(SimulatedMovement(args[1]))
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

        if(args[0] == "option")
            return getListOfStringsMatchingLastWord(args, Debug.Option.map.keys.toList())

        return listOf()
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}