package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.utils.KeyBindHelper
import kotlinx.coroutines.delay

object AutoClicker : Toggleable("AutoClicker") {
    private var cps = 10
        set(v) {
            field = v
            timeBeforeNextClick = 1000L / v
        }

    private var timeBeforeNextClick : Long = 100L
    private var lastClickTimeMillis : Long = 0L

    fun update(deltaTimeMillis : Long) {
        if(cps < 1) disable()
        if(!toggled) return

        lastClickTimeMillis += deltaTimeMillis

        if(lastClickTimeMillis >= timeBeforeNextClick) {
            KeyBindHelper.press(settings.keyBindAttack)
            lastClickTimeMillis = 0L
        }
    }
}