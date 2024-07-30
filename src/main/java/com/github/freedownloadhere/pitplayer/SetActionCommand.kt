package com.github.freedownloadhere.pitplayer

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class SetActionCommand : CommandBase() {
    override fun getCommandName(): String {
        return "setaction"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        if(args?.size != 1) return
        StateMachine.currentAction = when(args[0]) {
            "wandering" -> StateMachine.PlayerAction.Wandering
            "fighting" -> StateMachine.PlayerAction.Fighting
            else -> StateMachine.PlayerAction.Idle
        }
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}