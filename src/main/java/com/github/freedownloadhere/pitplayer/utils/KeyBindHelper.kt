package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.mixin.AccessorKeyBinding
import net.minecraft.client.settings.KeyBinding

object KeyBindHelper : Toggleable("Keybind Helper", true) {
    private val affectedKeys = mutableSetOf<AccessorKeyBinding>()

    fun reset() {
        for(key in affectedKeys) {
            key.pressed_pitplayer = false
            key.pressTime_pitplayer = 0
        }
        affectedKeys.clear()
    }

    fun press(key : KeyBinding) {
        if(!toggled) return
        val accessor = key as AccessorKeyBinding
        affectedKeys.add(accessor)
        accessor.pressed_pitplayer = true
        accessor.pressTime_pitplayer = 1
    }

    fun release(key : KeyBinding) {
        if(!toggled) return
        val accessor = key as AccessorKeyBinding
        affectedKeys.add(accessor)
        accessor.pressed_pitplayer = false
        accessor.pressTime_pitplayer = 0
    }
}