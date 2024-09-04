package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.debug.Debug
import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import kotlinx.coroutines.delay

object AutoClicker : Toggleable() {
    var cps = 10L

    suspend fun update() {
        if(cps < 1L) disable()
        if(!toggled) return
        PlayerControlHelper.press(settings.keyBindAttack)
        delay(1000L / cps)
        PlayerControlHelper.release(settings.keyBindAttack)
    }
}