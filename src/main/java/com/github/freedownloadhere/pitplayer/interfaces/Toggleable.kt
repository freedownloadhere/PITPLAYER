package com.github.freedownloadhere.pitplayer.interfaces

import com.github.freedownloadhere.pitplayer.debug.Debug

open class Toggleable(val moduleName : String, firstState : Boolean = false) {
    var toggled : Boolean = firstState
        private set
    fun toggle() {
        if(toggled) disable()
        else enable()
    }
    fun enable() {
        toggled = true
        Debug.Logger.regular("Enabled \u00A7a$moduleName")
    }
    fun disable() {
        toggled = false
        Debug.Logger.regular("Disabled \u00A7c$moduleName")
    }
}