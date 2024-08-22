package com.github.freedownloadhere.pitplayer.commands

import com.github.freedownloadhere.pitplayer.event.EventBeginFighting
import com.github.freedownloadhere.pitplayer.event.IObservable
import com.github.freedownloadhere.pitplayer.event.IObserver
import com.github.freedownloadhere.pitplayer.state.StateMachine
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class FightCommand : CommandBase(), IObservable {
    override fun getCommandName(): String {
        return "fight"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return ""
    }

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        dispatch(EventBeginFighting())
        return
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override val observers = arrayListOf<IObserver>(StateMachine)
}