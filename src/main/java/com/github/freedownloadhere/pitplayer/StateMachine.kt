package com.github.freedownloadhere.pitplayer

import com.github.freedownloadhere.pitplayer.event.EventFinishedPathing
import com.github.freedownloadhere.pitplayer.event.IEvent
import com.github.freedownloadhere.pitplayer.event.IObserver
import com.github.freedownloadhere.pitplayer.extensions.mc
import com.github.freedownloadhere.pitplayer.extensions.player

object StateMachine : IObserver {
    enum class PlayerAction {
        Idle,
        Wandering,
        Fighting
    }
    var action = PlayerAction.Idle

    override fun update(e: IEvent) {
        if(e is EventFinishedPathing)
            action = PlayerAction.Idle
    }

    fun isIngame() : Boolean {
        return mc.thePlayer != null && mc.theWorld != null
    }

    fun isInHypixel() : Boolean {
        if(!isIngame()) return false
        if(mc.isSingleplayer) return false
        if(!mc.currentServerData.serverIP.lowercase().contains("hypixel")) return false
        return true
    }

    fun isInThePit() : Boolean {
        if(!isInHypixel()) return false
        return ScoreboardReader.contents.lowercase().contains("the hypixel pit")
    }

    fun currentArea() : PitArea {
        val playerPos = player.positionVector
        for(area in PitArea.entries)
            if(area.rect.contains(playerPos))
                return area
        return PitArea.UNKNOWN
    }


}