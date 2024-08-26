package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.pathing.moveset.NeighbourCones
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.Vec3i

class DebugCommand : CommandBase() {
    override fun getCommandName(): String {
        return "debug"
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
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean {
        return true
    }
}