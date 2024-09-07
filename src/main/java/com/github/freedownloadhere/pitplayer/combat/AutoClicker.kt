package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.utils.KeyBindHelper
import kotlinx.coroutines.delay

object AutoClicker : Toggleable("AutoClicker") {
    private var cps = 10L
        set(v) {
            field = v
            timeBeforeNextClick = 1000L / v
        }

    private var timeBeforeNextClick = 100L
    private var lastClickTime = 0L

    fun update(deltaTime : Long) {
        if(cps < 1) disable()
        if(!toggled) return

        lastClickTime += deltaTime

        if(lastClickTime >= timeBeforeNextClick) {
            KeyBindHelper.press(settings.keyBindAttack)
            lastClickTime = 0L
        }
    }
}