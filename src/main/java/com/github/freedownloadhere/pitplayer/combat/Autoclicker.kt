package com.github.freedownloadhere.pitplayer.combat

import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import java.time.Instant

object Autoclicker : Toggleable(true) {
    var cps = 10

    fun update() {
        if(!toggled) return
    }
}