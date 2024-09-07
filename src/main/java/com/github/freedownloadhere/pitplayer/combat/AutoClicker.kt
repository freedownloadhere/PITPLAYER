package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.extensions.settings
import com.github.freedownloadhere.pitplayer.interfaces.Killable
import com.github.freedownloadhere.pitplayer.utils.KeyBindHelper
import com.github.freedownloadhere.pitplayer.utils.RandomHelper

object AutoClicker : Killable("Auto Clicker") {
    // 10 cps default
    private var timeBeforeNextClick = 100L
    private var lastClickTime = 0L
    private var maxErrorTime = 10L
    private var shouldUpdate = false

    fun update(deltaTime : Long) {
        if(killed) return
        if(!shouldUpdate) return

        val errorTime = RandomHelper.fromRange(-maxErrorTime .. maxErrorTime)
        lastClickTime += deltaTime + errorTime

        if(lastClickTime >= timeBeforeNextClick) {
            KeyBindHelper.press(settings.keyBindAttack)
            lastClickTime = 0L
        }
    }

    fun cps(cps : Long) {
        timeBeforeNextClick = 1000L / cps
    }

    fun start() {
        shouldUpdate = true
    }

    fun stop() {
        shouldUpdate = false
    }
}