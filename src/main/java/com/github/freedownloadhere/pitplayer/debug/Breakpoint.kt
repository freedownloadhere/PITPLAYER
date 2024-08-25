package com.github.freedownloadhere.pitplayer.debug

import kotlinx.coroutines.delay

class Breakpoint(val name : String, val onHit : suspend () -> Unit) {
    private var active = false
    private var trap = true
    private val activationState : String
        get() = "Currently ${if(active) "\u00A7aenabled" else "\u00A7cdisabled"}"

    override fun toString(): String {
        return "Breakpoint $name : $activationState"
    }

    fun activate() {
        active = true
        Debug.Logger.send("Activated breakpoint \u00A73$name.")
    }

    fun deactivate() {
        active = false
        Debug.Logger.send("Disabled breakpoint \u00A73$name.")
    }

    fun cont() {
        trap = false
    }

    fun toggle() {
        active = !active
        Debug.Logger.send("Toggled breakpoint \u00A73$name. $activationState")
    }

    suspend fun check(condition : () -> Boolean) {
        if(!active) return
        if(!condition()) return
        Debug.Logger.send("Hit breakpoint \u00A73$name.", Debug.Logger.Level.Alert)
        onHit()
        while(trap) { delay(100L) }
        Debug.Logger.send("Continuing past \u00A73$name...")
        trap = true
    }
}