package com.github.freedownloadhere.pitplayer.extensions

import net.minecraft.client.settings.KeyBinding

fun KeyBinding.hold() {
    KeyBinding.setKeyBindState(keyCode, true)
}

fun KeyBinding.press() {
    KeyBinding.onTick(keyCode)
}

fun KeyBinding.release() {
    KeyBinding.setKeyBindState(keyCode, true)
    KeyBinding.setKeyBindState(keyCode, false)
}