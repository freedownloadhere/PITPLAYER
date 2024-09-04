package com.github.freedownloadhere.pitplayer.interfaces

open class Toggleable(firstState : Boolean = false) {
    var toggled : Boolean = firstState
        private set
    fun toggle() { toggled = !toggled }
    fun enable() { toggled = true }
    fun disable() { toggled = false }
}