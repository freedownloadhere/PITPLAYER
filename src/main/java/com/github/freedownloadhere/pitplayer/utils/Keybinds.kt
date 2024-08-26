package com.github.freedownloadhere.pitplayer.utils

import com.github.freedownloadhere.pitplayer.pathing.movement.PlayerControlHelper
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

enum class Keybinds(val key: KeyBinding, val action: () -> Unit) {
    ToggleRemote(KeyBinding("key.pitplayer.toggleremote", Keyboard.KEY_J, "key.category.pitplayer"), { PlayerControlHelper.toggle() });
}