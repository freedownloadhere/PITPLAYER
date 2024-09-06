package com.github.freedownloadhere.pitplayer.mixin;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayer.class)
public interface AccessorEntityPlayer {
    @Accessor("flyToggleTimer")
    int getFlyToggleTimer_pitplayer();

    @Accessor("flyToggleTimer")
    void setFlyToggleTimer_pitplayer(int flyToggleTimer_pitplayer);
}
