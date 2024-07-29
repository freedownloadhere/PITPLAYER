package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.extensions.toBlockPos
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.Vec3i

class PathfindCommand : CommandBase() {
    override fun getCommandName(): String {
        return "pathfind"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args == null || args.size < 3) return
        val dest = Vec3i(args[0].toInt(), args[1].toInt(), args[2].toInt())
        val route = Pathfinder.pathfind(dest, player.positionVector.toBlockPos()) ?: return
        GPS.route = route
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}