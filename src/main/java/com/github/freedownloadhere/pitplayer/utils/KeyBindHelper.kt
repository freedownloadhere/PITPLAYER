package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.extensions.*
import com.github.freedownloadhere.pitplayer.interfaces.Toggleable
import com.github.freedownloadhere.pitplayer.mixin.AccessorKeyBinding
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.EntityLiving
import net.minecraft.util.Vec3
import kotlin.math.atan2
import kotlin.math.hypot

object KeyBindHelper : Toggleable("Keybind Helper",true) {
    val state : String
        get() = "\u00A7lRemote: \u00A7${if(toggled) "aYes" else "cNo"}"
    val ingame : Boolean
        get() = mc.thePlayer != null && mc.theWorld != null
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