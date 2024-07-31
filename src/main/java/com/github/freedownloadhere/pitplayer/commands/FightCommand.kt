package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.CombatModule
import com.github.freedownloadhere.pitplayer.StateMachine
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class FightCommand : CommandBase() {
    override fun getCommandName(): String {
        return "fight"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        StateMachine.currentAction = StateMachine.PlayerAction.Fighting
        return
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}