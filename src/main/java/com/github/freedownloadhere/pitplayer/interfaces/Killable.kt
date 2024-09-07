package com.github.freedownloadhere.pitplayer.interfaces

import com.github.freedownloadhere.pitplayer.debug.Debug

/*
This should only be used for debugging purposes.
It's meant to entirely halt any class that misbehaves during testing.
 */
open class Killable(private val moduleName : String, kill : Boolean = false) {
    var killed : Boolean = kill
        private set
    fun killToggle() {
        if(killed) unkill()
        else kill()
    }
    fun kill() {
        killed = true
        Debug.Logger.regular("Killed \u00A7a$moduleName")
    }
    fun unkill() {
        killed = false
        Debug.Logger.regular("Un-killed \u00A7c$moduleName")
    }
}