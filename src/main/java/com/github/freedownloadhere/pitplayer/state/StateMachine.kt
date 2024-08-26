package com.github.freedownloadhere.pitplayer.state

import com.github.freedownloadhere.pitplayer.utils.PitArea
import com.github.freedownloadhere.pitplayer.utils.ScoreboardReader
import com.github.freedownloadhere.pitplayer.event.*
import com.github.freedownloadhere.pitplayer.extensions.mc
import com.github.freedownloadhere.pitplayer.extensions.player
import com.github.freedownloadhere.pitplayer.modules.CombatModule
import com.github.freedownloadhere.pitplayer.modules.GPS

object StateMachine : IObserver {
    enum class PlayerAction {
        Idle,
        Wandering,
        Pathing,
        Fighting
    }

    object State {
        var action = PlayerAction.Idle
    }

    private fun finishPathing() {
        if(State.action != PlayerAction.Pathing) return
        State.action = PlayerAction.Idle
    }

    private fun beginPathing(e : EventBeginPathing) {
        GPS.dest = e.pos
        GPS.makeRoute()
        if(GPS.route.isNullOrEmpty()) return
        State.action = PlayerAction.Pathing
    }

    private fun beginFighting() {
        CombatModule.findTarget()
        if(CombatModule.target == null) return
        State.action = PlayerAction.Fighting
    }

    override fun receiveEvent(e: IEvent) {
        when(e) {
            is EventFinishedPathing -> finishPathing()
            is EventBeginPathing -> beginPathing(e)
            is EventBeginFighting -> beginFighting()
        }
    }

    fun makeDecision() {
        when(State.action) {
            PlayerAction.Idle -> { }
            PlayerAction.Wandering -> { }
            PlayerAction.Pathing -> GPS.traverseRoute()
            PlayerAction.Fighting -> CombatModule.attackTarget()
        }
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